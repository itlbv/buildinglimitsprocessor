package com.itlbv.buildinglimitsprocessor

import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class BuildingLimitSplitsServiceTest {
    private val buildingLimitsService = mockk<BuildingLimitsService>()
    private val heightPlateausService = mockk<HeightPlateausService>()
    private val buildingLimitSplitsRepository = mockk<BuildingLimitSplitsRepository>()

    @Test
    fun `should recalculate building limit splits`() {
        // given
        val buildingLimitSplitsService = BuildingLimitSplitsService(buildingLimitsService, heightPlateausService)
        val expectedBuildingSplit1 = BuildingLimitSplit(
            points = setOf(
                Pair(BigDecimal("1.0"), BigDecimal("1.0")),
                Pair(BigDecimal("1.0"), BigDecimal("5.0")),
                Pair(BigDecimal("3.0"), BigDecimal("1.0")),
                Pair(BigDecimal("3.0"), BigDecimal("5.0")),
            ),
            height = BigDecimal("10.0")
        )
        val expectedBuildingSplit2 = BuildingLimitSplit(
            points = setOf(
                Pair(BigDecimal("3.0"), BigDecimal("1.0")),
                Pair(BigDecimal("5.0"), BigDecimal("1.0")),
                Pair(BigDecimal("3.0"), BigDecimal("5.0")),
                Pair(BigDecimal("5.0"), BigDecimal("5.0")),
            ),
            height = BigDecimal("15.0")
        )
        val expectedBuildingSplit3 = BuildingLimitSplit(
            points = setOf(
                Pair(BigDecimal("10.0"), BigDecimal("10.0")),
                Pair(BigDecimal("20.0"), BigDecimal("10.0")),
                Pair(BigDecimal("10.0"), BigDecimal("12.0")),
                Pair(BigDecimal("20.0"), BigDecimal("12.0")),
            ),
            height = BigDecimal("15.0")
        )
        val expectedBuildingSplit4 = BuildingLimitSplit(
            points = setOf(
                Pair(BigDecimal("10.0"), BigDecimal("12.0")),
                Pair(BigDecimal("20.0"), BigDecimal("12.0")),
                Pair(BigDecimal("10.0"), BigDecimal("18.0")),
                Pair(BigDecimal("20.0"), BigDecimal("18.0")),
            ),
            height = BigDecimal("1.0")
        )

        every { buildingLimitsService.getAll() } returns setOf(
            BuildingLimit(
                points = setOf(
                    Pair(BigDecimal("10.0"), BigDecimal("10.0")),
                    Pair(BigDecimal("20.0"), BigDecimal("10.0")),
                    Pair(BigDecimal("10.0"), BigDecimal("20.0")),
                    Pair(BigDecimal("20.0"), BigDecimal("20.0")),
                )
            ),
            BuildingLimit(
                points = setOf(
                    Pair(BigDecimal("1.0"), BigDecimal("1.0")),
                    Pair(BigDecimal("5.0"), BigDecimal("1.0")),
                    Pair(BigDecimal("1.0"), BigDecimal("5.0")),
                    Pair(BigDecimal("5.0"), BigDecimal("5.0")),
                )
            )
        )

        every { heightPlateausService.getAll() } returns setOf(
            HeightPlateau(
                points = setOf(
                    Pair(BigDecimal("0.0"), BigDecimal("0.0")),
                    Pair(BigDecimal("3.0"), BigDecimal("0.0")),
                    Pair(BigDecimal("0.0"), BigDecimal("12.0")),
                    Pair(BigDecimal("3.0"), BigDecimal("12.0")),
                ),
                height = BigDecimal("10.0")
            ),
            HeightPlateau(
                points = setOf(
                    Pair(BigDecimal("0.0"), BigDecimal("12.0")),
                    Pair(BigDecimal("22.0"), BigDecimal("12.0")),
                    Pair(BigDecimal("0.0"), BigDecimal("18.0")),
                    Pair(BigDecimal("22.0"), BigDecimal("18.0")),
                ),
                height = BigDecimal("1.0")
            ),
            HeightPlateau(
                points = setOf(
                    Pair(BigDecimal("3.0"), BigDecimal("0.0")),
                    Pair(BigDecimal("21.0"), BigDecimal("0.0")),
                    Pair(BigDecimal("3.0"), BigDecimal("12.0")),
                    Pair(BigDecimal("21.0"), BigDecimal("12.0")),
                ),
                height = BigDecimal("15.0")
            )
        )

        // when
        buildingLimitSplitsService.recalculate()

        // then
        verifyAll {
            buildingLimitSplitsRepository.save(expectedBuildingSplit1)
            buildingLimitSplitsRepository.save(expectedBuildingSplit2)
            buildingLimitSplitsRepository.save(expectedBuildingSplit3)
            buildingLimitSplitsRepository.save(expectedBuildingSplit4)
        }
    }
}