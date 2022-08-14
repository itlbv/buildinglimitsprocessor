package com.itlbv.buildinglimitsprocessor

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import mu.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class GeoDataService(
    private val buildingLimitsService: BuildingLimitsService,
    private val heightPlateausService: HeightPlateausService,
) {
    fun parseAndSaveGeoData(geoData: String) {
        logger.info { "Parsing geoData: $geoData" }
        val geoDataJsonElement = Json.parseToJsonElement(geoData).jsonObject
        buildingLimitsService.save(geoDataJsonElement["building_limits"]!!.toString())
        heightPlateausService.save(geoDataJsonElement["height_plateaus"]!!.toString())
    }
}