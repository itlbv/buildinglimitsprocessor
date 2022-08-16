package com.itlbv.buildinglimitsprocessor

import com.itlbv.buildinglimitsprocessor.model.BuildingLimit
import com.itlbv.buildinglimitsprocessor.model.BuildingLimitSplit
import com.itlbv.buildinglimitsprocessor.model.HeightPlateau
import com.itlbv.buildinglimitsprocessor.repository.BuildingLimitSplitsRepository
import com.itlbv.buildinglimitsprocessor.service.BuildingLimitSplitsService
import com.itlbv.buildinglimitsprocessor.service.BuildingLimitsService
import com.itlbv.buildinglimitsprocessor.service.HeightPlateausService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import org.junit.jupiter.api.Test
import java.math.BigDecimal

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
//                arrayOf(BigDecimal("1.0"), BigDecimal("1.0")),
//                arrayOf(BigDecimal("3.0"), BigDecimal("1.0")),
//                arrayOf(BigDecimal("3.0"), BigDecimal("5.0")),
//                arrayOf(BigDecimal("1.0"), BigDecimal("5.0")),
//            ),
//            height = BigDecimal("10.0")
//        )
//        val expectedBuildingSplit2 = BuildingLimitSplit(
//            points = listOf(
//                arrayOf(BigDecimal("3.0"), BigDecimal("1.0")),
//                arrayOf(BigDecimal("5.0"), BigDecimal("1.0")),
//                arrayOf(BigDecimal("5.0"), BigDecimal("5.0")),
//                arrayOf(BigDecimal("3.0"), BigDecimal("5.0")),
//            ),
//            height = BigDecimal("15.0")
//        )
//        val expectedBuildingSplit3 = BuildingLimitSplit(
//            points = listOf(
//                arrayOf(BigDecimal("10.0"), BigDecimal("10.0")),
//                arrayOf(BigDecimal("20.0"), BigDecimal("10.0")),
//                arrayOf(BigDecimal("20.0"), BigDecimal("12.0")),
//                arrayOf(BigDecimal("10.0"), BigDecimal("12.0")),
//            ),
//            height = BigDecimal("15.0")
//        )
//        val expectedBuildingSplit4 = BuildingLimitSplit(
//            points = listOf(
//                arrayOf(BigDecimal("10.0"), BigDecimal("12.0")),
//                arrayOf(BigDecimal("20.0"), BigDecimal("12.0")),
//                arrayOf(BigDecimal("20.0"), BigDecimal("18.0")),
//                arrayOf(BigDecimal("10.0"), BigDecimal("18.0")),
//            ),
//            height = BigDecimal("1.0")
//        )
//
//        every { buildingLimitsService.getAll() } returns setOf(
//            BuildingLimit(
//                points = listOf(
//                    arrayOf(BigDecimal("10.0"), BigDecimal("10.0")),
//                    arrayOf(BigDecimal("10.0"), BigDecimal("20.0")),
//                    arrayOf(BigDecimal("20.0"), BigDecimal("20.0")),
//                    arrayOf(BigDecimal("20.0"), BigDecimal("10.0")),
//                )
//            ),
//            BuildingLimit(
//                points = listOf(
//                    arrayOf(BigDecimal("1.0"), BigDecimal("1.0")),
//                    arrayOf(BigDecimal("5.0"), BigDecimal("1.0")),
//                    arrayOf(BigDecimal("5.0"), BigDecimal("5.0")),
//                    arrayOf(BigDecimal("1.0"), BigDecimal("5.0")),
//                )
//            )
//        )
//
//        every { heightPlateausService.getAll() } returns setOf(
//            HeightPlateau(
//                points = listOf(
//                    arrayOf(BigDecimal("0.0"), BigDecimal("0.0")),
//                    arrayOf(BigDecimal("3.0"), BigDecimal("0.0")),
//                    arrayOf(BigDecimal("3.0"), BigDecimal("12.0")),
//                    arrayOf(BigDecimal("0.0"), BigDecimal("12.0")),
//                ),
//                height = BigDecimal("10.0")
//            ),
//            HeightPlateau(
//                points = listOf(
//                    arrayOf(BigDecimal("0.0"), BigDecimal("12.0")),
//                    arrayOf(BigDecimal("22.0"), BigDecimal("12.0")),
//                    arrayOf(BigDecimal("22.0"), BigDecimal("18.0")),
//                    arrayOf(BigDecimal("0.0"), BigDecimal("18.0")),
//                ),
//                height = BigDecimal("1.0")
//            ),
//            HeightPlateau(
//                points = listOf(
//                    arrayOf(BigDecimal("3.0"), BigDecimal("0.0")),
//                    arrayOf(BigDecimal("21.0"), BigDecimal("0.0")),
//                    arrayOf(BigDecimal("21.0"), BigDecimal("12.0")),
//                    arrayOf(BigDecimal("3.0"), BigDecimal("12.0")),
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