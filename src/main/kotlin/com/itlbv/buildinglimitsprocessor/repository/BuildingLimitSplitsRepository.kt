package com.itlbv.buildinglimitsprocessor.repository

import com.itlbv.buildinglimitsprocessor.model.BuildingLimitSplit
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BuildingLimitSplitsRepository : CrudRepository<BuildingLimitSplit, Long>