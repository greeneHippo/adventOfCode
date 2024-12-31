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
}
