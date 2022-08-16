package com.itlbv.buildinglimitsprocessor

import com.itlbv.buildinglimitsprocessor.repository.BuildingLimitSplitsRepository
import com.itlbv.buildinglimitsprocessor.service.BuildingLimitsService
import com.itlbv.buildinglimitsprocessor.service.HeightPlateausService
import io.mockk.mockk

internal class BuildingLimitSplitsServiceTest {
    private val buildingLimitsService = mockk<BuildingLimitsService>()
    private val heightPlateausService = mockk<HeightPlateausService>()
    private val buildingLimitSplitsRepository = mockk<BuildingLimitSplitsRepository>(relaxed = true)

//    @Test
//    fun `should recalculate building limit splits`() {
//        // given
//        val buildingLimitSplitsService = BuildingLimitSplitsService(buildingLimitsService, heightPlateausService, buildingLimitSplitsRepository)
//        val expectedBuildingSplit1 = BuildingLimitSplit(
//            points = listOf(
//                Pair(BigDecimal("1.0"), BigDecimal("1.0")),
//                Pair(BigDecimal("3.0"), BigDecimal("1.0")),
//                Pair(BigDecimal("3.0"), BigDecimal("5.0")),
//                Pair(BigDecimal("1.0"), BigDecimal("5.0")),
//            ),
//            height = BigDecimal("10.0")
//        )
//        val expectedBuildingSplit2 = BuildingLimitSplit(
//            points = listOf(
//                Pair(BigDecimal("3.0"), BigDecimal("1.0")),
//                Pair(BigDecimal("5.0"), BigDecimal("1.0")),
//                Pair(BigDecimal("5.0"), BigDecimal("5.0")),
//                Pair(BigDecimal("3.0"), BigDecimal("5.0")),
//            ),
//            height = BigDecimal("15.0")
//        )
//        val expectedBuildingSplit3 = BuildingLimitSplit(
//            points = listOf(
//                Pair(BigDecimal("10.0"), BigDecimal("10.0")),
//                Pair(BigDecimal("20.0"), BigDecimal("10.0")),
//                Pair(BigDecimal("20.0"), BigDecimal("12.0")),
//                Pair(BigDecimal("10.0"), BigDecimal("12.0")),
//            ),
//            height = BigDecimal("15.0")
//        )
//        val expectedBuildingSplit4 = BuildingLimitSplit(
//            points = listOf(
//                Pair(BigDecimal("10.0"), BigDecimal("12.0")),
//                Pair(BigDecimal("20.0"), BigDecimal("12.0")),
//                Pair(BigDecimal("20.0"), BigDecimal("18.0")),
//                Pair(BigDecimal("10.0"), BigDecimal("18.0")),
//            ),
//            height = BigDecimal("1.0")
//        )
//
//        every { buildingLimitsService.getAll() } returns setOf(
//            BuildingLimit(
//                points = listOf(
//                    Pair(BigDecimal("10.0"), BigDecimal("10.0")),
//                    Pair(BigDecimal("10.0"), BigDecimal("20.0")),
//                    Pair(BigDecimal("20.0"), BigDecimal("20.0")),
//                    Pair(BigDecimal("20.0"), BigDecimal("10.0")),
//                )
//            ),
//            BuildingLimit(
//                points = listOf(
//                    Pair(BigDecimal("1.0"), BigDecimal("1.0")),
//                    Pair(BigDecimal("5.0"), BigDecimal("1.0")),
//                    Pair(BigDecimal("5.0"), BigDecimal("5.0")),
//                    Pair(BigDecimal("1.0"), BigDecimal("5.0")),
//                )
//            )
//        )
//
//        every { heightPlateausService.getAll() } returns setOf(
//            HeightPlateau(
//                points = listOf(
//                    Pair(BigDecimal("0.0"), BigDecimal("0.0")),
//                    Pair(BigDecimal("3.0"), BigDecimal("0.0")),
//                    Pair(BigDecimal("3.0"), BigDecimal("12.0")),
//                    Pair(BigDecimal("0.0"), BigDecimal("12.0")),
//                ),
//                height = BigDecimal("10.0")
//            ),
//            HeightPlateau(
//                points = listOf(
//                    Pair(BigDecimal("0.0"), BigDecimal("12.0")),
//                    Pair(BigDecimal("22.0"), BigDecimal("12.0")),
//                    Pair(BigDecimal("22.0"), BigDecimal("18.0")),
//                    Pair(BigDecimal("0.0"), BigDecimal("18.0")),
//                ),
//                height = BigDecimal("1.0")
//            ),
//            HeightPlateau(
//                points = listOf(
//                    Pair(BigDecimal("3.0"), BigDecimal("0.0")),
//                    Pair(BigDecimal("21.0"), BigDecimal("0.0")),
//                    Pair(BigDecimal("21.0"), BigDecimal("12.0")),
//                    Pair(BigDecimal("3.0"), BigDecimal("12.0")),
//                ),
//                height = BigDecimal("15.0")
//            )
//        )
//
//        every { buildingLimitSplitsRepository.save(any()) } returns
//                BuildingLimitSplit(points = listOf(), height = BigDecimal.ZERO)
//
//        // when
//        buildingLimitSplitsService.calculate()
//
//        // then
//        verifyAll {
//            buildingLimitSplitsRepository.save(expectedBuildingSplit1)
//            buildingLimitSplitsRepository.save(expectedBuildingSplit2)
//            buildingLimitSplitsRepository.save(expectedBuildingSplit3)
//            buildingLimitSplitsRepository.save(expectedBuildingSplit4)
//        }
//    }
}