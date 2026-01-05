package nl.groen

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UtilsTest {

    @Test
    fun testPower() {
        assertEquals(1L, power(8L, 0))
        assertEquals(8L, power(8L, 1))
        assertEquals(64L, power(8L, 2))
        assertEquals(512L, power(8L, 3))
    }

    @Test
    fun testRanges() {
        val ranges = listOf(
            LongRange(0, 1),
            LongRange(4, 7),
            LongRange(3, 8)
        )

        val expected = listOf(
            LongRange(0, 1),
            LongRange(3, 8)
        )
        val result = ranges.mergeRanges()

        assertEquals(expected, result)

    }

    @Test
    fun testRanges2() {
        val ranges = listOf(
            LongRange(3, 5),
            LongRange(10, 14),
            LongRange(16, 19),
            LongRange(12, 20)
        )

        val expected = listOf(
            LongRange(3, 5),
            LongRange(10, 20)
        )
        val result = ranges.mergeRanges()

        assertEquals(expected, result)

    }

    @Test
    fun doIntersect() {
        assertTrue(
            doIntersect(
                Pair(PositionDouble(0.toDouble(),0.toDouble()), PositionDouble(2.toDouble(),0.toDouble())),
                Pair(PositionDouble(1.toDouble(), (-1).toDouble()), PositionDouble(1.toDouble(), 1.toDouble()))
            )
        )
        assertFalse(
            doIntersect(
                Pair(PositionDouble(0.toDouble(),0.toDouble()), PositionDouble(2.toDouble(),0.toDouble())),
                Pair(PositionDouble(3.toDouble(), -1.toDouble()), PositionDouble(3.toDouble(), 1.toDouble()))
            )
        )
        assertTrue(
            doIntersect(
                Pair(PositionDouble(0.toDouble(), 0.toDouble()), PositionDouble(2.toDouble(), 0.toDouble())),
                Pair(PositionDouble(2.toDouble(), 0.toDouble()), PositionDouble(4.toDouble(), 0.toDouble()))
            )
        )
        assertTrue(
            doIntersect(
                Pair(PositionDouble(0.toDouble(),0.toDouble()), PositionDouble(2.toDouble(),0.toDouble())),
                Pair(PositionDouble(1.toDouble(), 0.toDouble()), PositionDouble(3.toDouble(), 0.toDouble()))
            )
        )
        assertFalse(
            doIntersect(
                Pair(PositionDouble(0.toDouble(),2.toDouble()), PositionDouble(2.toDouble(),2.toDouble())),
                Pair(PositionDouble(0.toDouble(), 3.toDouble()), PositionDouble(100.toDouble(), 3.toDouble()))
            )
        )
        assertTrue(
            doIntersect(
                Pair(PositionDouble(0.toDouble(),2.toDouble()), PositionDouble(0.toDouble(),4.toDouble())),
                Pair(PositionDouble(0.toDouble(), 2.toDouble()), PositionDouble(20.toDouble(), 20.toDouble()))
            )
        )

    }
}
