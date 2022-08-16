package com.itlbv.buildinglimitsprocessor.repository

import com.itlbv.buildinglimitsprocessor.model.BuildingLimit
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BuildingLimitsRepository : CrudRepository<BuildingLimit, Long>