package com.itlbv.buildinglimitsprocessor

import com.itlbv.buildinglimitsprocessor.exceptions.HeightPlateausProcessingException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

internal class HeightPlateausServiceTest {

    private val heightPlateausRepository = mockk<HeightPlateausRepository>(relaxed = true)

    @Test
    fun `should save height plateaus`() {
        // given
        val heightPlateausService = HeightPlateausService(heightPlateausRepository)

        val expectedHeightPlateau1 = HeightPlateau(
            points = setOf(
                Pair(BigDecimal("15.0"), BigDecimal("15.0")),
                Pair(BigDecimal("25.0"), BigDecimal("15.0")),
                Pair(BigDecimal("15.0"), BigDecimal("25.0")),
                Pair(BigDecimal("25.0"), BigDecimal("25.0")),
            ),
            height = BigDecimal("10.0"),
        )
        val expectedHeightPlateau2 = HeightPlateau(
            points = setOf(
                Pair(BigDecimal("3.0"), BigDecimal("3.0")),
                Pair(BigDecimal("7.0"), BigDecimal("3.0")),
                Pair(BigDecimal("3.0"), BigDecimal("7.0")),
                Pair(BigDecimal("7.0"), BigDecimal("7.0")),
            ),
            height = BigDecimal("1.0")
        )

        every { heightPlateausRepository.save(any()) } returns
                HeightPlateau(points = setOf(), height = BigDecimal.ZERO)

        // when
        heightPlateausService.save(HEIGHT_PLATEAUS)

        // then
        verifyAll {
            heightPlateausRepository.save(expectedHeightPlateau1)
            heightPlateausRepository.save(expectedHeightPlateau2)
        }
    }

    @Test
    fun `should throw when can't parse input`() {
        // given
        val heightPlateausService = HeightPlateausService(heightPlateausRepository)

        // when, then
        assertThrows<HeightPlateausProcessingException> {
            heightPlateausService.save(
                HEIGHT_PLATEAUS_MALFORMED_JSON
            )
        }
        assertThrows<HeightPlateausProcessingException> {
            heightPlateausService.save(
                HEIGHT_PLATEAUS_BAD_INTERNAL_STRUCTURE
            )
        }
    }

    @Test
    fun `should not save anything if one of geometries is not polygon`() {
        // given
        val heightPlateausService = HeightPlateausService(heightPlateausRepository)

        // when, then
        assertThrows<IllegalArgumentException> {
            heightPlateausService.save(
                HEIGHT_PLATEAUS_NOT_POLYGON_GEO
            )
        }
        verify(exactly = 0) { heightPlateausRepository.save(any()) }
    }

    @Test
    fun `should not save anything if one of polygons has inconsistent height`() {
        // given
        val heightPlateausService = HeightPlateausService(heightPlateausRepository)

        // when, then
        assertThrows<HeightPlateausProcessingException> {
            heightPlateausService.save(
                HEIGHT_PLATEAUS_INCONSISTENT_HEIGHT
            )
        }
        verify(exactly = 0) { heightPlateausRepository.save(any()) }
    }

    companion object {
        private val HEIGHT_PLATEAUS = """
            {
                "type": "GeometryCollection",
                "geometries": [
                    {
                        "type": "Polygon",
                        "coordinates": [
                            [[15.0, 15.0, 10.0], [25.0, 15.0, 10.0], [15.0, 25.0, 10.0], [25.0, 25.0, 10.0]]
                        ]
                    },
                    {
                        "type": "Polygon",
                        "coordinates": [
                            [[3.0, 3.0, 1.0], [7.0, 3.0, 1.0], [3.0, 7.0, 1.0], [7.0, 7.0, 1.0]]
                        ]
                    }
                ]
            }
        """.trimIndent()

        private val HEIGHT_PLATEAUS_MALFORMED_JSON = """
            {
                "type"  MALFORMED,
                "geometries": [
                    {
                        "type": "Polygon",
                        "coordinates": [
                            [[15.0, 15.0, 10.0], [25.0, 15.0, 10.0], [15.0, 25.0, 10.0], [25.0, 25.0, 10.0]]
                        ]
                    }
                ]
            }
        """.trimIndent()

        private val HEIGHT_PLATEAUS_NOT_POLYGON_GEO = """
            {
                "type": "GeometryCollection",
                "geometries": [
                     {
                        "type": "Polygon",
                        "coordinates": [
                            [[15.0, 15.0, 10.0], [25.0, 15.0, 10.0], [15.0, 25.0, 10.0], [25.0, 25.0, 10.0]]
                        ]
                    },
                    {
                        "type": "NotPolygon",
                        "coordinates": [
                            [[3.0, 3.0, 1.0], [7.0, 3.0, 1.0], [3.0, 7.0, 1.0], [7.0, 7.0, 1.0]]
                        ]
                    }
                ]
            }
        """.trimIndent()

        private val HEIGHT_PLATEAUS_BAD_INTERNAL_STRUCTURE = """
            {
                "type": "GeometryCollection",
                "geome_BAD_STRUCTURE_tries": [
                    {
                        "type": "Polygon",
                        "coordinates": [
                            [[15.0, 15.0, 10.0], [25.0, 15.0, 10.0], [15.0, 25.0, 10.0], [25.0, 25.0, 10.0]]
                        ]
                    }
                ]
            }
        """.trimIndent()

        private val HEIGHT_PLATEAUS_INCONSISTENT_HEIGHT = """
            {
                "type": "GeometryCollection",
                "geometries": [
                    {
                        "type": "Polygon",
                        "coordinates": [
                            [[3.0, 3.0, 1.0], [7.0, 3.0, 1.0], [3.0, 7.0, 1.0], [7.0, 7.0, 1.0]]
                        ]
                    },
                    {
                        "type": "Polygon",
                        "coordinates": [
                            [[15.0, 15.0, 10.0], [25.0, 15.0, 999.0], [15.0, 25.0, 10.0], [25.0, 25.0, 10.0]]
                        ]
                    }
                ]
            }
        """.trimIndent()
    }
}