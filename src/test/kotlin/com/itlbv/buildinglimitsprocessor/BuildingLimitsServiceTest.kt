package com.itlbv.buildinglimitsprocessor

import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class BuildingLimitsServiceTest {

    private val buildingLimitsRepository = mockk<BuildingLimitsRepository>(relaxed = true)

    @Test
    fun `should save building limits`() {
        // given
        val buildingLimitsService = BuildingLimitsService(buildingLimitsRepository)

        val expectedBuildingLimit1 = BuildingLimit(
            points = setOf(
                Pair(BigDecimal("10.0"), BigDecimal("10.0")),
                Pair(BigDecimal("20.0"), BigDecimal("10.0")),
                Pair(BigDecimal("10.0"), BigDecimal("20.0")),
                Pair(BigDecimal("20.0"), BigDecimal("20.0")),
            )
        )
        val expectedBuildingLimit2 = BuildingLimit(
            points = setOf(
                Pair(BigDecimal("1.0"), BigDecimal("1.0")),
                Pair(BigDecimal("5.0"), BigDecimal("1.0")),
                Pair(BigDecimal("1.0"), BigDecimal("5.0")),
                Pair(BigDecimal("5.0"), BigDecimal("5.0")),
            )
        )

        every { buildingLimitsRepository.save(any()) } returns BuildingLimit(points = setOf())

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
                "type": "GeometryCollection",
                "geometries": [
                    {
                        "type": "Polygon",
                        "coordinates": [
                            [[10.0, 10.0], [20.0, 10.0], [10.0, 20.0], [20.0, 20.0]]
                        ]
                    },
                    {
                        "type": "Polygon",
                        "coordinates": [
                            [[1.0, 1.0], [5.0, 1.0], [1.0, 5.0], [5.0, 5.0]]
                        ]
                    }
                ]
            }
        """.trimIndent()
    }
}