package nl.groen.year2024

import nl.groen.*

enum class Type(val symbol: String) {
    WALL("#"),
    BOX("O"),
    ROBOT("@"),
    EMPTY(".");

    override fun toString() :String {
        return this.symbol
    }
}

fun printWarehouse(area :MutableMap<Position,Type>, positionRobot :Position) {
    area[positionRobot] = Type.ROBOT
    printGridOfPoints(area)
    area[positionRobot] = Type.EMPTY
}
fun main() {

    fun parseMoves(strings: List<List<String>>) = strings[1].joinToString(separator = "").toList().map {
        val direction = when (it.toString()) {
            "^" -> Direction.NORTH
            ">" -> Direction.EAST
            "<" -> Direction.WEST
            "v" -> Direction.SOUTH
            else -> {
                error("Unexpected direction $it")
            }
        }
        return@map direction
    }

    fun moveNextBox(move: MoveAction, area: MutableMap<Position, Type>, neighbourGrid: MutableMap<Position, MutableList<MoveAction>>): Boolean {

        val next = neighbourGrid[move.position]!!.first { it.direction == move.direction }
        return when (area[next.position]!!) {
            Type.WALL -> false
            Type.BOX -> {
                if (moveNextBox(next, area, neighbourGrid)) {
                    area[next.position] = Type.BOX
                    return true
                } else {
                    return false
                }
            }
            Type.ROBOT -> {error("not possible")}
            Type.EMPTY -> {
                area[next.position] = Type.BOX
                area[move.position] = Type.EMPTY
                return true
            }
        }
    }

    fun part1 (input : List<String>): Long {

        val strings = groupStringsOnEmptyLine(input)

        val area: MutableMap<Position, Type> = mutableMapOf()
        val neighbourGrid: MutableMap<Position, MutableList<MoveAction>> = mutableMapOf()
        parseGrid(strings[0], area, neighbourGrid) { s: String ->
            return@parseGrid Type.entries.firstOrNull { part -> s == part.symbol }!!
        }

        val moves = parseMoves(strings)

        var positionRobot = area.filterValues { Type.ROBOT == it }.keys.first()
        area[positionRobot] = Type.EMPTY

        moves.forEach { move ->
            println(move)
            val next = neighbourGrid[positionRobot]!!.first { it.direction == move }
            when (area[next.position]!!) {
                Type.WALL -> {}
                Type.BOX -> {
                    if (moveNextBox(next, area, neighbourGrid)) {
                        positionRobot = next.position
                    }
                }
                Type.ROBOT -> {error("not possible")}
                Type.EMPTY -> {positionRobot = next.position }
            }
            area[positionRobot] = Type.EMPTY
//            printWarehouse(area, positionRobot)
        }

        printWarehouse(area, positionRobot)
        return area.filter { it.value == Type.BOX }.map { 100*it.key.y + it.key.x}.sum().toLong()
    }

    fun part2 (input : List<String>): Long {

        return input.size.toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day15_test")
    val input = readInput("2024","Day15")

    check(part1(testInput) == 10092L)
    part1(input).println()

    check(part2(testInput) == 9021L)
    part2(input).println()

}

