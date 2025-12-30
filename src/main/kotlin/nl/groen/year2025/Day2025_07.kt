package nl.groen.year2025

import nl.groen.*
import java.util.*

data class State(val position: Position, val accumulatedTimeLines: Long)

enum class ManifoldValue(val symbol: String) {
    START("S"),
    SPLITTER("^"),
    BEAM("|"),
    EMPTY(".");

    override fun toString() :String {
        return this.symbol
    }
}

fun main() {

    fun init(input: List<String>): Pair<MutableMap<Position, ManifoldValue>, MutableMap<Position, MutableList<MoveAction>>> {
        val area: MutableMap<Position, ManifoldValue> = mutableMapOf()
        val neighbourGrid: MutableMap<Position, MutableList<MoveAction>> = mutableMapOf()
        parseGrid(input, area, neighbourGrid, true) { s: String ->
            return@parseGrid ManifoldValue.entries.firstOrNull { part -> s == part.symbol }!!
        }
        return Pair(area, neighbourGrid)
    }

    fun part1 (input : List<String>): Long {

        val (area: MutableMap<Position, ManifoldValue>, neighbourGrid: MutableMap<Position, MutableList<MoveAction>>) = init(
            input
        )

        val queue = PriorityQueue { state1: State, state2: State -> state1.position.y.compareTo(state2.position.y) }
        // The map to record whether this state has been visited.
        queue.add(State(area.filterValues { value -> value == ManifoldValue.START }.keys.first(), 1))

        var count = 0L
        while (!queue.isEmpty()) {
            val current = queue.poll()
            val downwardNeighbour = neighbourGrid[current.position]!!.firstOrNull{ it.direction == Direction.SOUTH }

            if (downwardNeighbour == null) {
                // We have reached the bottom
                continue
            }

            if (area[downwardNeighbour.position] == ManifoldValue.EMPTY) {
                area[downwardNeighbour.position] = ManifoldValue.BEAM
                queue.add(State(downwardNeighbour.position, 1))
                continue
            }

            if (area[downwardNeighbour.position] == ManifoldValue.SPLITTER) {

                count++
                val beamPositions = listOf(
                    Position(downwardNeighbour.position.x-1, downwardNeighbour.position.y),
                    Position(downwardNeighbour.position.x+1, downwardNeighbour.position.y)
                )
                beamPositions.forEach { beamPosition ->
                    if (area[beamPosition] == ManifoldValue.EMPTY) {
                        area[beamPosition] = ManifoldValue.BEAM
                        queue.add(State(beamPosition, 1))
                    }
                }
                continue

            }
        }

        return count

    }

    fun part2 (input : List<String>): Long {

        val (area: MutableMap<Position, ManifoldValue>, neighbourGrid: MutableMap<Position, MutableList<MoveAction>>) = init(
            input
        )

        val queue = PriorityQueue { state1: State, state2: State -> state1.position.y.compareTo(state2.position.y) }
        // The map to record whether this state has been visited.
        queue.add(State(area.filterValues { value -> value == ManifoldValue.START }.keys.first(), 1))

        var count = 0L
        while (!queue.isEmpty()) {
            val current = queue.poll()
            val downwardNeighbour = neighbourGrid[current.position]!!.firstOrNull{ it.direction == Direction.SOUTH }

            if (downwardNeighbour == null) {
                // We have reached the bottom, this is the end of this timeline
                count+=current.accumulatedTimeLines
                continue
            }

            if (area[downwardNeighbour.position] == ManifoldValue.EMPTY || area[downwardNeighbour.position] == ManifoldValue.BEAM) {
                area[downwardNeighbour.position] = ManifoldValue.BEAM
                queue.add(State(downwardNeighbour.position, current.accumulatedTimeLines))
                continue
            }

            if (area[downwardNeighbour.position] == ManifoldValue.SPLITTER) {

                val beamPositions = listOf(
                    Position(downwardNeighbour.position.x-1, downwardNeighbour.position.y),
                    Position(downwardNeighbour.position.x+1, downwardNeighbour.position.y)
                )
                beamPositions.forEach { beamPosition ->
                    val currentStateInQueue = queue.firstOrNull{ it.position == beamPosition }
                    if (currentStateInQueue != null) {
                        queue.remove(currentStateInQueue)
                        queue.add(State(beamPosition, currentStateInQueue.accumulatedTimeLines+current.accumulatedTimeLines))
                    } else {
                        area[beamPosition] = ManifoldValue.BEAM
                        queue.add(State(beamPosition, current.accumulatedTimeLines))
                    }
                }
                continue

            }

            throw IllegalStateException("")
        }

        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2025","Day07_test")
    val input = readInput("2025","Day07")

    check(part1(testInput) == 21L)
    part1(input).println()

    check(part2(testInput) == 40L)
    part2(input).println()

}