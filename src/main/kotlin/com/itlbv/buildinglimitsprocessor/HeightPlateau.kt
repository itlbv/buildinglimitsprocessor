package com.itlbv.buildinglimitsprocessor

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
@TypeDefs(TypeDef(name = "jsonb", typeClass = JsonBinaryType::class))
data class HeightPlateau(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "json")
    val points: Set<Pair<BigDecimal, BigDecimal>>,

    val height: BigDecimal
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HeightPlateau

        if (points != other.points) return false
        if (height != other.height) return false

        return true
    }

    override fun hashCode(): Int {
        var result = points.hashCode()
        result = 31 * result + height.hashCode()
        return result
    }

    override fun toString(): String {
        return "HeightPlateau(id=$id, points=$points, height=$height)"
    }
}