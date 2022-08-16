package com.itlbv.buildinglimitsprocessor

import com.itlbv.buildinglimitsprocessor.exceptions.GeoDataProcessingException
import com.itlbv.buildinglimitsprocessor.service.BuildingLimitSplitsService
import com.itlbv.buildinglimitsprocessor.service.BuildingLimitsService
import com.itlbv.buildinglimitsprocessor.service.GeoDataService
import com.itlbv.buildinglimitsprocessor.service.HeightPlateausService
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class GeoDataServiceTest {

    private val buildingLimitsService = mockk<BuildingLimitsService>(relaxed = true)
    private val heightPlateausService = mockk<HeightPlateausService>(relaxed = true)
    private val buildingLimitSplitsService = mockk<BuildingLimitSplitsService>(relaxed = true)

    @Test
    fun `should parse geoData and send building_limits for saving`() {
        // given
        val geoDataService = GeoDataService(buildingLimitsService, heightPlateausService)

        // when
        geoDataService.parseAndSaveGeoData(GEODATA_JSON)

        // then
        verify { buildingLimitsService.save(BUILDING_LIMITS_STRING) }
    }

    @Test
    fun `should parse geoData and send height_plateaus for saving`() {
        // given
        val geoDataService = GeoDataService(buildingLimitsService, heightPlateausService)

        // when
        geoDataService.parseAndSaveGeoData(GEODATA_JSON)

        // then
        verify { heightPlateausService.save(HEIGHT_PLATEAUS_STRING) }
    }

    @Test
    fun `should throw when can't parse input`() {
        // given
        val geoDataService = GeoDataService(buildingLimitsService, heightPlateausService)

        // when, then
        assertThrows<GeoDataProcessingException> { geoDataService.parseAndSaveGeoData(GEODATA_BUILDING_LIMITS_MALFORMED) }
        assertThrows<GeoDataProcessingException> { geoDataService.parseAndSaveGeoData(GEODATA_NO_BUILDING_LIMITS_FIELD) }
        assertThrows<GeoDataProcessingException> { geoDataService.parseAndSaveGeoData(GEODATA_HEIGHT_PLATEAUS_MALFORMED) }
        assertThrows<GeoDataProcessingException> { geoDataService.parseAndSaveGeoData(GEODATA_NO_HEIGHT_PLATEAUS_FIELD) }
    }

    companion object {
        private const val BUILDING_LIMITS_STRING = "\"BUILDING LIMITS\""

        private const val HEIGHT_PLATEAUS_STRING = "\"HEIGHT PLATEAUS\""

        private val GEODATA_JSON = """
            {
                "building_limits": $BUILDING_LIMITS_STRING,
                "height_plateaus": $HEIGHT_PLATEAUS_STRING
            }
        """.trim()

        private val GEODATA_BUILDING_LIMITS_MALFORMED = """
            {
                "building_limits"  - MISSING SEMICOLON, MALFORMED JSON
                "height_plateaus": $HEIGHT_PLATEAUS_STRING
            }
        """.trim()

        private val GEODATA_NO_BUILDING_LIMITS_FIELD = """
            {
                "buil_FIELD NOT FOUND_mits": $BUILDING_LIMITS_STRING,
                "height_plateaus": $HEIGHT_PLATEAUS_STRING
            }
        """.trim()

        private val GEODATA_HEIGHT_PLATEAUS_MALFORMED = """
            {
                "building_limits": $BUILDING_LIMITS_STRING,
                "height_plateaus"  - MISSING SEMICOLON, MALFORMED JSON
            }
        """.trim()

        private val GEODATA_NO_HEIGHT_PLATEAUS_FIELD = """
            {
                "building_limits": $BUILDING_LIMITS_STRING,
                "heig_FIELD NOT FOUND_teaus": $HEIGHT_PLATEAUS_STRING
            }
        """.trim()
    }
}
