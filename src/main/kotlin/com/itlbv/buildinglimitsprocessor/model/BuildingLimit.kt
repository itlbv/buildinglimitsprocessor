package com.itlbv.buildinglimitsprocessor.model

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
data class BuildingLimit(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "json")
    val points: List<Array<BigDecimal>>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BuildingLimit

        if (points != other.points) return false

        return true
    }

    override fun hashCode(): Int {
        return points.hashCode()
    }

    override fun toString(): String {
        return "BuildingLimit(id=$id, points=$points)"
    }
}