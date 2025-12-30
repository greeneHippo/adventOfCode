package nl.groen.year2024

import nl.groen.*
import java.util.*
import kotlin.math.abs

fun main() {

    data class State(val score: Int, val position: Position, val visited :Set<Position>, val cheatUsed: Boolean)

    fun init(input: List<String>): Pair<MutableMap<Position, String>, MutableMap<Position, MutableList<MoveAction>>> {
        val area: MutableMap<Position, String> = mutableMapOf()
        val neighbourGrid: MutableMap<Position, MutableList<MoveAction>> = mutableMapOf()
        parseGrid(input, area, neighbourGrid) { s: String ->
            return@parseGrid s
        }

        return Pair(area, neighbourGrid)
    }

    fun calculatePathWithoutCheat(
        neighbourGrid: MutableMap<Position, MutableList<MoveAction>>,
        area: MutableMap<Position, String>,
    ): Map<Position, Int> {

        val start = area.filter { it.value == "S" }.keys.first()
        val endPosition = area.filter { it.value == "E" }.keys.first()
        val queue = PriorityQueue { state1: State, state2: State -> state1.score.compareTo(state2.score) }
        queue.add(State(0, start, mutableSetOf(start), false))

        while (!queue.isEmpty()) {
            val current = queue.poll()

            if (current.position == endPosition) {
                return current.visited.reversed().mapIndexed { index, position -> position to index}.toMap()
            }

            val neighbours = neighbourGrid[current.position]!!
            for (neighbour in neighbours) {

                if (area[neighbour.position]!! != "#" && current.visited.none { it == neighbour.position }) {
                    queue.add(State(current.score + 1, neighbour.position, current.visited + neighbour.position, current.cheatUsed))
                }
            }
        }

        error("not possible")
    }

    fun calculatePathWithCheat(
        neighbourGrid: MutableMap<Position, MutableList<MoveAction>>,
        area: MutableMap<Position, String>,
        map: Map<Position, Int>,
        cheatGain: Int
    ): Int {

        val start = area.filter { it.value == "S" }.keys.first()
        val queue = PriorityQueue { state1: State, state2: State -> state1.score.compareTo(state2.score) }
        queue.add(State(0, start, mutableSetOf(start), false))
        var paths = 0
        val maxSteps = map.size-1 - cheatGain

        while (!queue.isEmpty()) {
            val current = queue.poll()

            if (current.score > maxSteps) {
                return paths
            }

            val neighbours = neighbourGrid[current.position]!!
            for (neighbour in neighbours) {

                if (area[neighbour.position]!! != "#" && current.visited.none { it == neighbour.position }) {
                    queue.add(State(current.score + 1, neighbour.position, current.visited + neighbour.position, current.cheatUsed))
                }

                if (area[neighbour.position]!! == "#" && !current.cheatUsed) {

                    // Check if neighbour of neighbour is accessible
                    val nextNext = neighbourGrid[neighbour.position]!!.filter { it.direction == neighbour.direction }
                    if (nextNext.isNotEmpty() && area[nextNext.first().position]!! != "#" && current.visited.none { it == nextNext.first().position }) {

                        val score = current.score + 2 + map[nextNext.first().position]!!
                        if (score <= maxSteps) {
//                            println("Add to path: $score")
                            paths++
                        }
                    }
                }
            }


        }

        error("not possible")
    }

    fun part1 (input : List<String>, cheatGain: Int): Long {

        val (area: MutableMap<Position, String>, neighbourGrid: MutableMap<Position, MutableList<MoveAction>>) = init(input)

        val pathWithoutCheat = calculatePathWithoutCheat(neighbourGrid, area)
        println("scoreWithoutCheat: ${pathWithoutCheat.size-1}")
        val paths = calculatePathWithCheat(neighbourGrid, area, pathWithoutCheat, cheatGain)



        return paths.toLong()
    }

    fun calculateCheatOptions(it: Map.Entry<Position, Int>, possibleEndPositions: Map<Position, Int>, maxSteps: Int, stepsWithoutCheat: Int) : Int {

        var result = 0
        val stepsFromStart = stepsWithoutCheat - it.value
        possibleEndPositions.forEach { (endPosition, stepsToEnd) ->

            val distanceBetweenPoints = abs(it.key.x - endPosition.x) + abs(it.key.y - endPosition.y)
            if (distanceBetweenPoints <= 20 && (stepsFromStart + distanceBetweenPoints + stepsToEnd ) <= maxSteps ) {
                result++
            }

        }

        return result
    }

    fun part2 (input : List<String>, cheatGain: Int): Long {

        val (area: MutableMap<Position, String>, neighbourGrid: MutableMap<Position, MutableList<MoveAction>>) = init(input)

        val pathWithoutCheat = calculatePathWithoutCheat(neighbourGrid, area)
        println("scoreWithoutCheat: ${pathWithoutCheat.size-1}")

        val possibleStartPositions = pathWithoutCheat.filter { it.value >= cheatGain + 2 }
        val possibleEndPositions = pathWithoutCheat.filter { it.value <= pathWithoutCheat.size - cheatGain-3 }

        val result = possibleStartPositions.map { calculateCheatOptions(it, possibleEndPositions, pathWithoutCheat.size-1 - cheatGain, pathWithoutCheat.size-1) }.sum()
        return result.toLong()

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day20_test")
    val input = readInput("2024","Day20")

    check(part1(testInput, 64) == 1L)
    part1(input, 100).println()

    check(part2(testInput, 76) == 3L)
    check(part2(testInput, 72) == 29L)
    part2(input, 100).println()

}

