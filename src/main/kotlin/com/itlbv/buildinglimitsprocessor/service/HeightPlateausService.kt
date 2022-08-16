package com.itlbv.buildinglimitsprocessor.service

import com.itlbv.buildinglimitsprocessor.exceptions.HeightPlateausParsingException
import com.itlbv.buildinglimitsprocessor.model.HeightPlateau
import com.itlbv.buildinglimitsprocessor.repository.HeightPlateausRepository
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
    fun getAll(): Iterable<HeightPlateau> = heightPlateausRepository.findAll()

    fun save(heightPlateausString: String) {
        logger.info { "Parsing height plateaus: $heightPlateausString" }

        val heightPlateausJsonObject = try {
            Json.parseToJsonElement(heightPlateausString)
        } catch (e: Exception) {
            throw HeightPlateausParsingException("Can't parse height plateaus. Nested exception is: $e")
        }.jsonObject

        val geometries = (heightPlateausJsonObject["geometries"]
            ?: throw HeightPlateausParsingException(
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
                ?: throw HeightPlateausParsingException(
                    "Can't find coordinates object when parsing height plateaus: $heightPlateausString"
                ))
                .jsonArray

            (0 until coordinates.size).forEach { coordId ->
                val points = coordinates[coordId].jsonArray

                val polygonPoints = mutableListOf<Array<BigDecimal>>()

                var height: BigDecimal? = null
                (0 until points.size).forEach { pointId ->
                    val point = points[pointId].jsonArray

                    polygonPoints.add(
                        arrayOf(
                            BigDecimal(point[0].toString()),
                            BigDecimal(point[1].toString()),
                        )
                    )

                    if (height == null) {
                        height = BigDecimal(point[2].toString())
                    } else if (height != BigDecimal(point[2].toString())) {
                        throw HeightPlateausParsingException(
                            "Height in one of the provided polygons does not match the others! Aborting."
                        )
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

        if (heightPlateausToSave.isEmpty()) {
            logger.warn { "No height plateaus to save! Check the input." }
        } else {
            logger.info { "Saving height plateaus: $heightPlateausToSave" }
            heightPlateausRepository.saveAll(heightPlateausToSave)
        }
    }
}