package nl.groen.year2024

import nl.groen.*

fun main() {

    data class LabTile(val isObstacle: Boolean, var isVisited: Boolean)
    data class LabTileV2(var isObstacle: Boolean, var isVisited: MutableList<Direction> = mutableListOf())

    fun part1 (input : List<String>): Long {

        val lab: MutableMap<Position, LabTile> = mutableMapOf()
        val neighbourGrid: MutableMap<Position, MutableList<MoveAction>> = mutableMapOf()
        parseGrid(input, lab, neighbourGrid) { s: String ->
            return@parseGrid LabTile(s == "#", s == "^")
        }

        var guardPosition = lab.filterValues { it.isVisited }.keys.first()
        var direction = Direction.NORTH

        while (true) {

            var nextMove = neighbourGrid[guardPosition]!!.filter { it.direction == direction }
            if (nextMove.isEmpty()) {
                return lab.filterValues { it.isVisited }.values.size.toLong()
            }
            if (lab[nextMove.first().position]!!.isObstacle) {
                direction = direction.turnRight()
                nextMove = neighbourGrid[guardPosition]!!.filter { it.direction == direction }
                if (nextMove.isEmpty()) {
                    return lab.filterValues { it.isVisited }.values.size.toLong()
                }
                if (lab[nextMove.first().position]!!.isObstacle) {
                    direction = direction.turnRight()
                    nextMove = neighbourGrid[guardPosition]!!.filter { it.direction == direction }
                    if (nextMove.isEmpty()) {
                        return lab.filterValues { it.isVisited }.values.size.toLong()
                    }
                }
            }

            guardPosition = nextMove.first().position
            lab[guardPosition]!!.isVisited = true
        }
    }

    fun parseInput(input: List<String>): Pair<MutableMap<Position, LabTileV2>, MutableMap<Position, MutableList<MoveAction>>> {
        val lab: MutableMap<Position, LabTileV2> = mutableMapOf()
        val neighbourGrid: MutableMap<Position, MutableList<MoveAction>> = mutableMapOf()
        parseGrid(input, lab, neighbourGrid) { s: String ->
            val list: MutableList<Direction> = mutableListOf()
            if (s == "^") {
                list.add(Direction.NORTH)
            }
            return@parseGrid LabTileV2(s == "#", list)
        }
        return Pair(lab, neighbourGrid)
    }

    fun Position.guardInLoop(input : List<String>): Boolean {

        val (lab: MutableMap<Position, LabTileV2>, neighbourGrid: MutableMap<Position, MutableList<MoveAction>>) = parseInput(input)

        println("test $this")
        var guardPosition = lab.filterValues { it.isVisited.isNotEmpty() }.keys.first()
        var direction = Direction.NORTH
        lab[this]!!.isObstacle = true

        while (true) {

            var nextMove = neighbourGrid[guardPosition]!!.filter { it.direction == direction }
            if (nextMove.isEmpty()) {
                return false
            }
            if (lab[nextMove.first().position]!!.isObstacle) {
                direction = direction.turnRight()
                nextMove = neighbourGrid[guardPosition]!!.filter { it.direction == direction }
                if (nextMove.isEmpty()) {
                    return false
                }
                if (lab[nextMove.first().position]!!.isObstacle) {
                    direction = direction.turnRight()
                    nextMove = neighbourGrid[guardPosition]!!.filter { it.direction == direction }
                    if (nextMove.isEmpty()) {
                        return false
                    }
                }
            }

            guardPosition = nextMove.first().position
            if (lab[guardPosition]!!.isVisited.isNotEmpty() && lab[guardPosition]!!.isVisited.contains(direction)) {
                return true
            }
            lab[guardPosition]!!.isVisited.add(direction)
        }
    }

    fun part2 (input : List<String>): Long {

        val (lab: MutableMap<Position, LabTileV2>, _: MutableMap<Position, MutableList<MoveAction>>) = parseInput(input)

        val possibleObstaclePositions = lab.filterValues { !it.isObstacle && it.isVisited.isEmpty()}.keys

        println("test ${possibleObstaclePositions.size}")
        val newObstaclePositions = possibleObstaclePositions.filter { it.guardInLoop(input) }
        println("test ${newObstaclePositions.size}")
        return newObstaclePositions.size.toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day06_test")
    val input = readInput("2024","Day06")

    check(part1(testInput) == 41L)
    part1(input).println()

    check(part2(testInput) == 6L)
    part2(input).println()

}

