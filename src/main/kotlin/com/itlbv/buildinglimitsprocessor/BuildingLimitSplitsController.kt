package com.itlbv.buildinglimitsprocessor

import com.itlbv.buildinglimitsprocessor.model.BuildingLimitSplit
import com.itlbv.buildinglimitsprocessor.service.BuildingLimitSplitsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/buildinglimitsplits")
class BuildingLimitSplitsController(
    private val buildingLimitSplitsService: BuildingLimitSplitsService,
) {
    @GetMapping()
    fun getAllBuildingLimitSplits(): Iterable<BuildingLimitSplit> = buildingLimitSplitsService.getAll()

    @PostMapping("/calculate")
    fun calculateBuildingLimitSplits() = buildingLimitSplitsService.calculate()
}
