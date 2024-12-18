package nl.groen.year2024

import nl.groen.*
import java.util.*

fun main() {

    data class State(val score: Long, val move: MoveAction, val path :MutableSet<Position>)
    data class Result(val score: Long, val tiles: Long)

    fun init(input: List<String>): Pair<MutableMap<Position, String>, MutableMap<Position, MutableList<MoveAction>>> {
        val area: MutableMap<Position, String> = mutableMapOf()
        val neighbourGrid: MutableMap<Position, MutableList<MoveAction>> = mutableMapOf()
        parseGrid(input, area, neighbourGrid) { s: String ->
            return@parseGrid s
        }

        return Pair(area, neighbourGrid)
    }

    fun part1 (input : List<String>): Result {

        val (area: MutableMap<Position, String>, neighbourGrid: MutableMap<Position, MutableList<MoveAction>>) = init(input)
        val start = MoveAction(area.filter { it.value == "S" }.keys.first(), Direction.EAST)
        val endPosition = area.filter { it.value == "E" }.keys.first()

        val queue = PriorityQueue { state1: State, state2: State -> state1.score.compareTo(state2.score) }
        // The map to record whether this state has been visited.
        val visited: MutableSet<Pair<MoveAction, Long>> = mutableSetOf()
        val allPaths: MutableSet<Position> = mutableSetOf()
        queue.add(State(0L, start, mutableSetOf(start.position)))
        visited.add(Pair(start, 0L))

        var finalScore = 100000L
        while (!queue.isEmpty()) {
            val current = queue.poll()

            if (current.move.position == endPosition) {
                finalScore = current.score
                allPaths.addAll(current.path)
            }

            if (current.score > finalScore) {
                return Result(finalScore, allPaths.size.toLong())
            }

            val neighbours = neighbourGrid[current.move.position]!!
            for (neighbour in neighbours) {

                if (area[neighbour.position]!! != "#") {

                    var turns = 1
                    if (neighbour.direction == current.move.direction) {
                        turns = 0
                    } else if (Direction.isOppositeDirection(neighbour.direction, current.move.direction)) {
                        turns = 2
                    }
                    val newPath = current.path.toMutableSet()
                    newPath.add(neighbour.position)
                    val newScore = current.score+1L + turns*1000L
                    if (turns < 2 && (visited.none { it.first == neighbour } || visited.filter { it.first == neighbour }.minOf { it.second } >= newScore)) {
                        visited.add(Pair(neighbour, newScore))
                        queue.add(State(newScore, neighbour, newPath))
                    }
                }
            }
        }

        error("not possible")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day16_test")
    val input = readInput("2024","Day16")

    check(part1(testInput) == Result(11048L, 64L))
    part1(input).println()

}

