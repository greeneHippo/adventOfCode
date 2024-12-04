package nl.groen.year2024

import nl.groen.*

enum class TravelDirection(val moves: List<Direction>) {
    NORTH(mutableListOf(Direction.NORTH)),
    EAST(mutableListOf(Direction.EAST)),
    SOUTH(mutableListOf(Direction.SOUTH)),
    WEST(mutableListOf(Direction.WEST)),
    NE(mutableListOf(Direction.NORTH, Direction.EAST)),
    NW(mutableListOf(Direction.NORTH, Direction.WEST)),
    SW(mutableListOf(Direction.SOUTH, Direction.WEST)),
    SE(mutableListOf(Direction.SOUTH, Direction.EAST));
}

fun main() {

    fun determineNextPosition(travelDirection: TravelDirection, neighbourGrid: MutableMap<Position, MutableList<MoveAction>>, nextPosition: Position): Position? {
        var nextPosition1 = nextPosition
        for (direction in travelDirection.moves) {
            val moveActions = neighbourGrid[nextPosition1]!!.filter { it.direction == direction }
            // Reached the end before making XMAS
            if (moveActions.isEmpty()) return null
            nextPosition1 = moveActions.first().position
        }
        return nextPosition1
    }

    fun countXMAS(key: Position?, neighbourGrid: MutableMap<Position, MutableList<MoveAction>>, letterGrid: MutableMap<Position, String>) : Int {

        var count = 0
        for (travelDirection in TravelDirection.entries) {
            var nextPosition = key
            nextPosition = determineNextPosition(travelDirection, neighbourGrid, nextPosition!!)
            if (nextPosition == null) continue
            if (letterGrid[nextPosition] != "M") continue
            nextPosition = determineNextPosition(travelDirection, neighbourGrid, nextPosition!!)
            if (nextPosition == null) continue
            if (letterGrid[nextPosition] != "A") continue
            nextPosition = determineNextPosition(travelDirection, neighbourGrid, nextPosition!!)
            if (nextPosition == null) continue
            if (letterGrid[nextPosition] != "S") continue
            count +=1
        }
        return count
    }

    fun countMAS(key: Position, neighbourGrid: MutableMap<Position, MutableList<MoveAction>>, letterGrid: MutableMap<Position, String>) : Int {

        val positionNE = determineNextPosition(TravelDirection.NE, neighbourGrid, key)
        val positionNW = determineNextPosition(TravelDirection.NW, neighbourGrid, key)
        val positionSE = determineNextPosition(TravelDirection.SE, neighbourGrid, key)
        val positionSW = determineNextPosition(TravelDirection.SW, neighbourGrid, key)

        if (positionNE == null || positionNW == null || positionSE == null || positionSW == null) return 0

        val lettersClockWise: String = letterGrid[positionNE] +  letterGrid[positionSE] +  letterGrid[positionSW] +  letterGrid[positionNW]

        return if (lettersClockWise == "SSMM" || lettersClockWise == "MSSM" || lettersClockWise == "MMSS" || lettersClockWise == "SMMS") {
            1
        } else {
            0
        }
    }


    fun part1 (input : List<String>): Long {

        val letterGrid: MutableMap<Position, String> = mutableMapOf()
        val neighbourGrid: MutableMap<Position, MutableList<MoveAction>> = mutableMapOf()
        parseGrid(input, letterGrid, neighbourGrid) { s: String -> s }

        // Filter on the position with the first letter
        val xPositions = letterGrid.filterValues { it == "X" }
        // Count if there is a XMAS in each of the eight directions.
        val count = xPositions.map { countXMAS(it.key, neighbourGrid, letterGrid) }



        return count.sum().toLong()
    }


    fun part2 (input : List<String>): Long {

        val letterGrid: MutableMap<Position, String> = mutableMapOf()
        val neighbourGrid: MutableMap<Position, MutableList<MoveAction>> = mutableMapOf()
        parseGrid(input, letterGrid, neighbourGrid) { s: String -> s }

        // Filter on the position with the first letter
        val xPositions = letterGrid.filterValues { it == "A" }
        // Count if there is a XMAS in each of the eight directions.
        val count = xPositions.map { countMAS(it.key, neighbourGrid, letterGrid) }

        return count.sum().toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day04_test")
    val input = readInput("2024","Day04")

    check(part1(testInput) == 18L)
    part1(input).println()

    check(part2(testInput) == 9L)
    part2(input).println()

}

