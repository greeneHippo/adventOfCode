package nl.groen.year2024

import nl.groen.*
import java.util.*

data class State(val value: Int, val position: Position)

fun main() {

    fun determinePaths(position: Position, area: MutableMap<Position, Int>, neighbourGrid: MutableMap<Position, MutableList<MoveAction>>, includeRating: Boolean) :Int {

        val queue = PriorityQueue { state1: State, state2: State -> state1.value.compareTo(state2.value) }
        // The map to record whether this state has been visited.
        val visited: MutableSet<Position> = mutableSetOf()
        queue.add(State(0, position))

        var count = 0
        while (!queue.isEmpty()) {
            val current = queue.poll()
            val neighbours = neighbourGrid[current.position]!!
            for (neighbour in neighbours) {
                val newValue = area[neighbour.position]

                if (newValue == (current.value + 1)) {
                    if (current.value == 8 && (includeRating || !visited.contains(neighbour.position))) {
                        visited.add(neighbour.position)
                        count+=1
                    } else {
                        queue.add(State(newValue, neighbour.position))
                    }
                }
            }
        }

        return count
    }

    fun init(input: List<String>): Triple<MutableMap<Position, Int>, MutableMap<Position, MutableList<MoveAction>>, Set<Position>> {
        val area: MutableMap<Position, Int> = mutableMapOf()
        val neighbourGrid: MutableMap<Position, MutableList<MoveAction>> = mutableMapOf()
        parseGrid(input, area, neighbourGrid) { s: String ->
            return@parseGrid s.toInt()
        }

        val startpositions = area.filterValues { it == 0 }.keys
        return Triple(area, neighbourGrid, startpositions)
    }

    fun part1 (input : List<String>): Long {

        val (area: MutableMap<Position, Int>, neighbourGrid: MutableMap<Position, MutableList<MoveAction>>, startpositions) = init(input)

        val trailheads : List<Int> = startpositions.map { determinePaths(it, area, neighbourGrid, false) }

        return trailheads.sumOf { it.toLong() }
    }

    fun part2 (input : List<String>): Long {

        val (area: MutableMap<Position, Int>, neighbourGrid: MutableMap<Position, MutableList<MoveAction>>, startpositions) = init(input)

        val trailheads : List<Int> = startpositions.map { determinePaths(it, area, neighbourGrid, true) }

        return trailheads.sumOf { it.toLong() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day10_test")
    val input = readInput("2024","Day10")

    check(part1(testInput) == 36L)
    part1(input).println()

    check(part2(testInput) == 81L)
    part2(input).println()

}

