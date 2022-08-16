package com.itlbv.buildinglimitsprocessor.model

import java.math.BigDecimal

@kotlinx.serialization.Serializable
data class Coordinate(
    val x: BigDecimal,
    val z: BigDecimal,
)
