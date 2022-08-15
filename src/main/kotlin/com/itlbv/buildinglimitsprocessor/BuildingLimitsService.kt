package com.itlbv.buildinglimitsprocessor

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
    fun save(buildingLimitsString: String) {

        val buildingLimitsJsonObject = Json.parseToJsonElement(buildingLimitsString).jsonObject

        val geometries = buildingLimitsJsonObject["geometries"]?.jsonArray

        (0 until (geometries?.size ?: 0)).forEach {
            val geometry = geometries?.get(it)?.jsonObject

            val coordinates = geometry?.get("coordinates")?.jsonArray

            (0 until (coordinates?.size ?: 0)).forEach { coordId ->
                val points = coordinates?.get(coordId)?.jsonArray

                val polygonPoints = mutableSetOf<Pair<BigDecimal, BigDecimal>>()

                (0 until (points?.size ?: 0)).forEach { pointId ->
                    val point = points?.get(pointId)?.jsonArray

                    polygonPoints.add(
                        Pair(
                            BigDecimal(point?.get(0).toString()),
                            BigDecimal(point?.get(1).toString()),
                        )
                    )
                }

                val buildingLimit = BuildingLimit(
                    points = polygonPoints
                )

                buildingLimitsRepository.save(buildingLimit)
            }
        }
    }
}