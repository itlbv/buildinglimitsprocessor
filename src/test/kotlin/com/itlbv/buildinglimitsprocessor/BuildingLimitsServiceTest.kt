package com.itlbv.buildinglimitsprocessor

import com.itlbv.buildinglimitsprocessor.exceptions.BuildingLimitsParsingException
import com.itlbv.buildinglimitsprocessor.model.BuildingLimit
import com.itlbv.buildinglimitsprocessor.repository.BuildingLimitsRepository
import com.itlbv.buildinglimitsprocessor.service.BuildingLimitsService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

internal class BuildingLimitsServiceTest {

    private val buildingLimitsRepository = mockk<BuildingLimitsRepository>(relaxed = true)

    @Test
    fun `should save building limits`() {
        // given
        val buildingLimitsService = BuildingLimitsService(buildingLimitsRepository)

        val expectedBuildingLimit1 = BuildingLimit(
            points = listOf(
                Pair(BigDecimal("10.0"), BigDecimal("10.0")),
                Pair(BigDecimal("20.0"), BigDecimal("10.0")),
                Pair(BigDecimal("10.0"), BigDecimal("20.0")),
                Pair(BigDecimal("20.0"), BigDecimal("20.0")),
            )
        )
        val expectedBuildingLimit2 = BuildingLimit(
            points = listOf(
                Pair(BigDecimal("1.0"), BigDecimal("1.0")),
                Pair(BigDecimal("5.0"), BigDecimal("1.0")),
                Pair(BigDecimal("1.0"), BigDecimal("5.0")),
                Pair(BigDecimal("5.0"), BigDecimal("5.0")),
            )
        )

        every { buildingLimitsRepository.save(any()) } returns BuildingLimit(points = listOf())

        // when
        buildingLimitsService.save(BUILDING_LIMITS)

        // then
        verifyAll {
            buildingLimitsRepository.save(expectedBuildingLimit1)
            buildingLimitsRepository.save(expectedBuildingLimit2)
        }
    }

    @Test
    fun `should throw when can't parse input`() {
        // given
        val buildingLimitsService = BuildingLimitsService(buildingLimitsRepository)

        // when, then
        assertThrows<BuildingLimitsParsingException> { buildingLimitsService.save(BUILDING_LIMITS_MALFORMED_JSON) }
        assertThrows<BuildingLimitsParsingException> { buildingLimitsService.save(BUILDING_LIMITS_BAD_INTERNAL_STRUCTURE) }
    }

    @Test
    fun `should not save anything if one of geometries is not polygon`() {
        // given
        val buildingLimitsService = BuildingLimitsService(buildingLimitsRepository)

        // when, then
        assertThrows<IllegalArgumentException> { buildingLimitsService.save(BUILDING_LIMITS_NOT_POLYGON_GEO) }
        verify(exactly = 0) { buildingLimitsRepository.save(any()) }
    }

    companion object {
        private val BUILDING_LIMITS = """
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

        private val BUILDING_LIMITS_MALFORMED_JSON = """
            {
                "type"  MALFORMED,
                "geometries": [
                    {
                        "type": "Polygon",
                        "coordinates": [
                            [[10.0, 10.0], [20.0, 10.0], [10.0, 20.0], [20.0, 20.0]]
                        ]
                    }
                ]
            }
        """.trimIndent()

        private val BUILDING_LIMITS_NOT_POLYGON_GEO = """
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
                        "type": "NotPolygon",
                        "coordinates": [
                            [[1.0, 1.0], [5.0, 1.0], [1.0, 5.0], [5.0, 5.0]]
                        ]
                    }
                ]
            }
        """.trimIndent()

        private val BUILDING_LIMITS_BAD_INTERNAL_STRUCTURE = """
            {
                "type": "GeometryCollection",
                "geome_BAD_STRUCTURE_tries": [
                    {
                        "type": "Polygon",
                        "coordinates": [
                            [[10.0, 10.0], [20.0, 10.0], [10.0, 20.0], [20.0, 20.0]]
                        ]
                    }
                ]
            }
        """.trimIndent()
    }
}