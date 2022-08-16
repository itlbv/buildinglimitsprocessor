package com.itlbv.buildinglimitsprocessor.service

import com.itlbv.buildinglimitsprocessor.exceptions.BuildingLimitSplitsProcessingException
import com.itlbv.buildinglimitsprocessor.model.BuildingLimitSplit
import com.itlbv.buildinglimitsprocessor.repository.BuildingLimitSplitsRepository
import mu.KotlinLogging
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal

private val logger = KotlinLogging.logger {}

@Service
class BuildingLimitSplitsService(
    private val buildingLimitsService: BuildingLimitsService,
    private val heightPlateausService: HeightPlateausService,
    private val buildingLimitSplitsRepository: BuildingLimitSplitsRepository,
) {
    fun getAll(): Iterable<BuildingLimitSplit> = buildingLimitSplitsRepository.findAll()

    fun calculate(): List<BuildingLimitSplit> {
        logger.info { "Calculating building limit splits" }

        val buildingLimits = buildingLimitsService.getAll()
        logger.debug { "Building limits getAll(): $buildingLimits" }

        val heightPlateaus = heightPlateausService.getAll()
        logger.debug { "Height plateaus getAll(): $heightPlateaus" }

        val geometryFactory = GeometryFactory()

        val buildingLimitSplitsToSave = mutableListOf<BuildingLimitSplit>()

        buildingLimits.forEach { buildingLimit ->

            val coordBuildingLimitList = buildingLimit.points.map {
                Coordinate(
                    it.first.toDouble(),
                    it.second.toDouble()
                )
            }.toMutableList()
            coordBuildingLimitList.add(0, coordBuildingLimitList.last().copy())

            val ring1 = geometryFactory.createLinearRing(coordBuildingLimitList.toTypedArray())

            val buildingLimitPolygon = geometryFactory.createPolygon(
                ring1,
                null
            )

            if (!buildingLimitPolygon.isValid) {
                throw BuildingLimitSplitsProcessingException(
                    "Can't construct polygon from building limit $buildingLimit."
                )
            }

            heightPlateaus.forEach { heightPlateau ->

                val coordHeightPlateauList = heightPlateau.points.map {
                    Coordinate(
                        it.first.toDouble(),
                        it.second.toDouble()
                    )
                }.toMutableList()
                coordHeightPlateauList.add(0, coordHeightPlateauList.last().copy())

                val heightPlateauPolygon = geometryFactory.createPolygon(
                    geometryFactory.createLinearRing(coordHeightPlateauList.toTypedArray()),
                    null
                )

                if (!heightPlateauPolygon.isValid) {
                    throw BuildingLimitSplitsProcessingException(
                        "Can't construct polygon from height plateau $heightPlateau."
                    )
                }

                if (buildingLimitPolygon.intersects(heightPlateauPolygon)) {
                    val geometry = buildingLimitPolygon.intersection(heightPlateauPolygon)

                    val pointBuildingLimitSplit =
                        geometry.coordinates.map { Pair(BigDecimal(it.x), BigDecimal(it.y)) }.toMutableList()
                    pointBuildingLimitSplit.removeLast()

                    val buildingLimitSplit = BuildingLimitSplit(
                        points = pointBuildingLimitSplit,
                        height = heightPlateau.height,
                    )

                    buildingLimitSplitsToSave.add(buildingLimitSplit)
                }
            }
        }

        logger.info { "Saving building limit splits: $buildingLimitSplitsToSave" }

        return buildingLimitSplitsRepository
            .saveAll(buildingLimitSplitsToSave)
            .toList()
    }
}