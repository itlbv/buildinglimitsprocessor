package com.itlbv.buildinglimitsprocessor

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BuildingLimitsRepository : CrudRepository<BuildingLimit, Long>