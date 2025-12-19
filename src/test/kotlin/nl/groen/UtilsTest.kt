package nl.groen

import kotlin.test.Test
import kotlin.test.assertEquals

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
}
