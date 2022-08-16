package com.itlbv.buildinglimitsprocessor.repository

import com.itlbv.buildinglimitsprocessor.model.HeightPlateau
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HeightPlateausRepository : CrudRepository<HeightPlateau, Long>