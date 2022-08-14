package com.itlbv.buildinglimitsprocessor

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class GeoDataServiceTest {

    private val buildingLimitsService = mockk<BuildingLimitsService>(relaxed = true)

    private val geoDataService = GeoDataService(buildingLimitsService)

    @Test
    fun `should parse geoData and send building_limits for saving`() {
        // when
        geoDataService.parseAndSaveGeoData(GEODATA_JSON)

        // then
        verify { buildingLimitsService.save(BUILDING_LIMITS_JSON) }
    }


    @Test
    fun `should parse geoData and send height_plateaus for saving`() {
        // when
        geoDataService.parseAndSaveGeoData(GEODATA_JSON)

        // then
        verify { heightPlateausService.save(HEIGHT_PLATEAUS) }
    }

    companion object {
        val BUILDING_LIMITS_JSON = """
            {"type":"FeatureCollection","features":[{"type":"Feature","geometry":{"type":"Polygon","coordinates":[[[10.0,10.0],[20.0,10.0],[20.0,10.0],[20.0,20.0]]]}},{"type":"Feature","geometry":{"type":"Polygon","coordinates":[[[1.0,1.0],[5.0,1.0],[5.0,1.0],[5.0,5.0]]]}}]}
        """.trim()

        val HEIGHT_PLATEAUS = """
            {"type":"FeatureCollection","features":[{"type":"Feature","geometry":{"type":"Polygon","coordinates":[[[15.0,15.0,3.0],[25.0,15.0,3.0],[25.0,15.0,3.0],[25.0,25.0,3.0]]]}},{"type":"Feature","geometry":{"type":"Polygon","coordinates":[[[1.0,1.0,2.0],[5.0,1.0,2.0],[5.0,1.0,2.0],[5.0,5.0,2.0]]]}}]}
        """.trimIndent()

        val GEODATA_JSON = """
            {
                "building_limits": $BUILDING_LIMITS_JSON,
                "height_plateaus": $HEIGHT_PLATEAUS
            }
        """.trim()
    }
}
