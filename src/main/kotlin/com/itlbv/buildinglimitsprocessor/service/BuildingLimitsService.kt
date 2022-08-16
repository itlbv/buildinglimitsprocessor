package com.itlbv.buildinglimitsprocessor.service

import com.itlbv.buildinglimitsprocessor.exceptions.BuildingLimitsParsingException
import com.itlbv.buildinglimitsprocessor.model.BuildingLimit
import com.itlbv.buildinglimitsprocessor.repository.BuildingLimitsRepository
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.math.BigDecimal

private val logger = KotlinLogging.logger {}

@Service
class BuildingLimitsService(
    private val buildingLimitsRepository: BuildingLimitsRepository,
) {
    fun getAll(): Iterable<BuildingLimit> = buildingLimitsRepository.findAll()

    fun save(buildingLimitsString: String) {
        logger.info { "Parsing building limits: $buildingLimitsString" }

        val buildingLimitsJsonObject = try {
            Json.parseToJsonElement(buildingLimitsString)
        } catch (e: Exception) {
            throw BuildingLimitsParsingException("Can't parse building limits. Nested exception is: $e")
        }.jsonObject

        val geometries = (buildingLimitsJsonObject["geometries"]
            ?: throw BuildingLimitsParsingException(
                "Can't find geometries object when parsing building limits: $buildingLimitsString"
            ))
            .jsonArray

        val buildingLimitsToSave = mutableSetOf<BuildingLimit>()

        (0 until geometries.size).forEach { geoId ->
            val geometry = geometries[geoId].jsonObject

            if ("\"Polygon\"" != geometry["type"].toString()) {
                throw IllegalArgumentException(
                    "Only Polygon geometry types are accepted. Aborting."
                )
            }

            val coordinates = (geometry["coordinates"]
                ?: throw BuildingLimitsParsingException(
                    "Can't find coordinates object when parsing building limits: $buildingLimitsString"
                ))
                .jsonArray

            (0 until coordinates.size).forEach { coordId ->
                val points = coordinates[coordId].jsonArray

                val polygonPoints = mutableListOf<Array<BigDecimal>>()

                (0 until points.size).forEach { pointId ->
                    val point = points[pointId].jsonArray

                    polygonPoints.add(
                        arrayOf(
                            BigDecimal(point[0].toString()),
                            BigDecimal(point[1].toString()),
                        )
                    )
                }

                buildingLimitsToSave.add(
                    BuildingLimit(
                        points = polygonPoints
                    )
                )
            }
        }

        if (buildingLimitsToSave.isEmpty()) {
            logger.warn { "No building limits to save! Check the input." }
        } else {
            logger.info { "Saving building limits: $buildingLimitsToSave" }
            buildingLimitsRepository.saveAll(buildingLimitsToSave)
        }
    }
}