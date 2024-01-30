package nl.groen

import kotlin.math.max

fun main() {

    fun determineHeight(elevationGrid :MutableMap<Position, Int> = mutableMapOf(), neighbourGrid :MutableMap<Position, MutableList<MoveAction>>, moveAction :MoveAction) : Int {

        val nextTree = neighbourGrid[moveAction.position]!!.filter { it.direction == moveAction.direction }
        if (nextTree.isEmpty()) {
            return elevationGrid[moveAction.position]!!
        } else {
            return max(
                elevationGrid[moveAction.position]!!,
                determineHeight(
                    elevationGrid,
                    neighbourGrid,
                    nextTree[0]
                ) )
        }
    }

    fun part1 (input : List<String>): Long {

        val elevationGrid: MutableMap<Position, Int> = mutableMapOf()
        val neighbourGrid: MutableMap<Position, MutableList<MoveAction>> = mutableMapOf()
        parseGrid(input, elevationGrid, neighbourGrid) {s: String -> s.toInt() }

        val visibleTrees = elevationGrid.map {

            if (neighbourGrid[it.key]!!.size != 4) {
                return@map true
            }
            val neighbours : List<Int> = neighbourGrid[it.key]!!.map {moveAction ->
                determineHeight(elevationGrid, neighbourGrid, moveAction)
            }.toList()

            neighbours.min() < elevationGrid[it.key]!!
        }

        return visibleTrees.count { it == true }.toLong()
    }

    fun determineViewingDistance(elevationGrid :MutableMap<Position, Int> = mutableMapOf(), neighbourGrid :MutableMap<Position, MutableList<MoveAction>>, moveAction :MoveAction, initialTreeSize: Int) : Long {

        val currentTreeSize = elevationGrid[moveAction.position]!!
        val nextTree = neighbourGrid[moveAction.position]!!.filter { it.direction == moveAction.direction }
        if (nextTree.isEmpty()) {
            return 0
        } else if (currentTreeSize >= initialTreeSize) {
            return 0
        } else {
            return 1 + determineViewingDistance(elevationGrid, neighbourGrid, nextTree[0], initialTreeSize)
        }
    }

    fun part2 (input : List<String>): Long {

        val elevationGrid: MutableMap<Position, Int> = mutableMapOf()
        val neighbourGrid: MutableMap<Position, MutableList<MoveAction>> = mutableMapOf()
        parseGrid(input, elevationGrid, neighbourGrid) {s: String -> s.toInt() }

        val visibleTrees = elevationGrid.map {

            if (neighbourGrid[it.key]!!.size != 4) {
                return@map 0L
            }
            val neighbours : List<Long> = neighbourGrid[it.key]!!.map {moveAction ->
                1 + determineViewingDistance(elevationGrid, neighbourGrid, moveAction, elevationGrid[it.key]!!)
            }.toList()

            neighbours.reduce { acc, i -> acc * i }
        }

        return visibleTrees.max()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    val input = readInput("Day08")

    check(part1(testInput) == 21L)
    part1(input).println()

    check(part2(testInput) == 8L)
    part2(input).println()

}

