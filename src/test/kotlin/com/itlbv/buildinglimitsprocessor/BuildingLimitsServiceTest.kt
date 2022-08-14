package com.itlbv.buildinglimitsprocessor

import io.mockk.mockk
import io.mockk.verifyAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BuildingLimitsServiceTest {

    private val buildingLimitsRepository = mockk<BuildingLimitsRepository>()

    @Test
    fun `should save building limits`() {
        // given
        val buildingLimitsService = BuildingLimitsService()
        val expectedBuildingLimit1 = BuildingLimit(listOf(
            arrayOf(10.0, 10.0),
            arrayOf(20.0, 10.0),
            arrayOf(10.0, 20.0),
            arrayOf(20.0, 20.0),
        ))
        val expectedBuildingLimit2 = BuildingLimit(listOf(
            arrayOf(1.0, 5.0),
            arrayOf(5.0, 1.0),
            arrayOf(1.0, 5.0),
            arrayOf(5.0, 5.0),
        ))

        // when
        buildingLimitsService.save(BUILDING_LIMITS_JSON)

        // then
        verifyAll {
            buildingLimitsRepository.save(expectedBuildingLimit1)
            buildingLimitsRepository.save(expectedBuildingLimit2)
        }
    }

    companion object {
        private val BUILDING_LIMITS_JSON = """
            {
                "type": "FeatureCollection",
                "features":
                [
                    {
                        "type": "Feature",
                        "geometry":
                        {
                            "type": "Polygon",
                            "coordinates":
                            [
                                [
                                    [
                                        10.0,
                                        10.0
                                    ],
                                    [
                                        20.0,
                                        10.0
                                    ],
                                    [
                                        10.0,
                                        20.0
                                    ],
                                    [
                                        20.0,
                                        20.0
                                    ]
                                ]
                            ]
                        }
                    },
                    {
                        "type": "Feature",
                        "geometry":
                        {
                            "type": "Polygon",
                            "coordinates":
                            [
                                [
                                    [
                                        1.0,
                                        1.0
                                    ],
                                    [
                                        5.0,
                                        1.0
                                    ],
                                    [
                                        1.0,
                                        5.0
                                    ],
                                    [
                                        5.0,
                                        5.0
                                    ]
                                ]
                            ]
                        }
                    }
                ]
            }
        """.trim()
    }
}