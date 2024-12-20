package nl.groen

import nl.groen.Direction.Companion.isNextDirection
import kotlin.test.Test
import kotlin.test.assertEquals

class DirectionTest {

    @Test
    fun testIsNextDirection() {
        assertEquals(false, isNextDirection(Direction.NORTH, Direction.NORTH))
        assertEquals(true, isNextDirection(Direction.NORTH, Direction.EAST))
        assertEquals(false, isNextDirection(Direction.NORTH, Direction.SOUTH))
        assertEquals(true, isNextDirection(Direction.NORTH, Direction.WEST))

        assertEquals(true, isNextDirection(Direction.EAST, Direction.NORTH))
        assertEquals(false, isNextDirection(Direction.EAST, Direction.EAST))
        assertEquals(true, isNextDirection(Direction.EAST, Direction.SOUTH))
        assertEquals(false, isNextDirection(Direction.EAST, Direction.WEST))

        assertEquals(false, isNextDirection(Direction.SOUTH, Direction.NORTH))
        assertEquals(true, isNextDirection(Direction.SOUTH, Direction.EAST))
        assertEquals(false, isNextDirection(Direction.SOUTH, Direction.SOUTH))
        assertEquals(true, isNextDirection(Direction.SOUTH, Direction.WEST))

        assertEquals(true, isNextDirection(Direction.WEST, Direction.NORTH))
        assertEquals(false, isNextDirection(Direction.WEST, Direction.EAST))
        assertEquals(true, isNextDirection(Direction.WEST, Direction.SOUTH))
        assertEquals(false, isNextDirection(Direction.WEST, Direction.WEST))

    }
}