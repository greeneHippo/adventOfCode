package nl.groen.year2024

import nl.groen.*
import java.util.*
import java.util.stream.IntStream

fun main() {

    data class State(val steps: Int, val position: Position)

    fun part1 (input : List<String>, numBytes: Int, maxCoordinate: Int): Long {

        val corruptedPoints = input.subList(0, numBytes).map { it.split(",").map { it.toInt() }}.map { Position(it[0], it[1]) }

        val neighbourGrid: MutableMap<Position, MutableList<MoveAction>> = mutableMapOf()
        IntStream.range(0, maxCoordinate+1).forEachOrdered { y ->
            IntStream.range(0, maxCoordinate+1).forEachOrdered { x ->
                if (neighbourGrid[Position(x, y)] == null) {
                    neighbourGrid[Position(x, y)] = mutableListOf()
                }
                if (x != maxCoordinate) {
                    neighbourGrid[Position(x, y)]!!.add(MoveAction(Position(x + 1, y), Direction.EAST))
                }
                if (x != 0) {
                    neighbourGrid[Position(x, y)]!!.add(MoveAction(Position(x - 1, y), Direction.WEST))
                }
                if (y != maxCoordinate) {
                    neighbourGrid[Position(x, y)]!!.add(MoveAction(Position(x, y + 1), Direction.SOUTH))
                }
                if (y != 0) {
                    neighbourGrid[Position(x, y)]!!.add(MoveAction(Position(x, y - 1), Direction.NORTH))
                }
            }
        }

        val queue = PriorityQueue { state1: State, state2: State -> state1.steps.compareTo(state2.steps) }
        // The map to record whether this state has been visited.
        val visited: MutableSet<Position> = mutableSetOf()
        queue.add(State(0, Position(0, 0)))

        while (!queue.isEmpty()) {
            val current = queue.poll()

            if (current.position == Position(maxCoordinate, maxCoordinate)) {
                return current.steps.toLong()
            }

            val neighbours = neighbourGrid[current.position]!!
            for (neighbour in neighbours) {

                if (!visited.contains(neighbour.position) && !corruptedPoints.contains(neighbour.position)) {
                    visited.add(neighbour.position)
                    queue.add(State(current.steps+1, neighbour.position))
                }
            }
        }

        return Long.MAX_VALUE
    }

    fun part2 (input : List<String>, numBytes: Int, maxCoordinate: Int): String {

        var iteration = numBytes+1
        while (part1(input, iteration, maxCoordinate) != Long.MAX_VALUE) {
            println("Path after iteration $iteration")
            iteration++
        }

        return input[iteration-1]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day18_test")
    val input = readInput("2024","Day18")

    check(part1(testInput, 12, 6) == 22L)
    part1(input, 1024, 70).println()

    check(part2(testInput,12, 6) == "6,1")
    part2(input, 1024, 70).println()

}

