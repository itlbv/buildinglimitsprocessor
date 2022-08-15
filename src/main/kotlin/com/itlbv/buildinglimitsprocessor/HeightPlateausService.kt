package com.itlbv.buildinglimitsprocessor

import com.itlbv.buildinglimitsprocessor.exceptions.HeightPlateausProcessingException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.math.BigDecimal

private val logger = KotlinLogging.logger {}

@Service
class HeightPlateausService(
    private val heightPlateausRepository: HeightPlateausRepository,
) {
    fun save(heightPlateausString: String) {
        val heightPlateausJsonObject = try {
            Json.parseToJsonElement(heightPlateausString)
        } catch (e: Exception) {
            throw HeightPlateausProcessingException("Can't parse height plateaus. Underlying exception is: $e")
        }.jsonObject

        val geometries = (heightPlateausJsonObject["geometries"]
            ?: throw HeightPlateausProcessingException(
                "Can't find geometries object when parsing height plateaus: $heightPlateausString"
            ))
            .jsonArray

        val heightPlateausToSave = mutableSetOf<HeightPlateau>()

        (0 until geometries.size).forEach { geoId ->
            val geometry = geometries[geoId].jsonObject

            if ("\"Polygon\"" != geometry["type"].toString()) {
                throw IllegalArgumentException(
                    "Only Polygon geometry types are accepted. Aborting."
                )
            }

            val coordinates = (geometry["coordinates"]
                ?: throw HeightPlateausProcessingException(
                    "Can't find coordinates object when parsing building limits: $heightPlateausString"
                ))
                .jsonArray

            (0 until coordinates.size).forEach { coordId ->
                val points = coordinates[coordId].jsonArray

                val polygonPoints = mutableSetOf<Pair<BigDecimal, BigDecimal>>()

                var height: BigDecimal? = null
                (0 until points.size).forEach { pointId ->
                    val point = points[pointId].jsonArray

                    polygonPoints.add(
                        Pair(
                            BigDecimal(point[0].toString()),
                            BigDecimal(point[1].toString()),
                        )
                    )

                    if (height == null) {
                        height = BigDecimal(point[2].toString())
                    } else if (height != BigDecimal(point[2].toString())) {
                        throw HeightPlateausProcessingException("Height in one of provided polygons does not match! Aborting.")
                    }

                }

                heightPlateausToSave.add(
                    HeightPlateau(
                        points = polygonPoints,
                        height = height!!
                    )
                )
            }
        }

        heightPlateausToSave.forEach {
            logger.info { "Saving height plateau: $it" }
            heightPlateausRepository.save(it)
        }
    }
}