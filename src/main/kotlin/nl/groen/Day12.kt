package nl.groen

import org.apache.commons.lang3.tuple.ImmutablePair
import java.util.*

fun main() {

    data class State(val noSteps: Long, val currentHeight: Int, val position: Position, val historyOfMoves: MutableList<Direction>)
    data class HeightInfo(val height: Int, val begin: Boolean, val eind: Boolean)

    fun init(input: List<String>): Pair<MutableMap<Position, HeightInfo>, MutableMap<Position, MutableList<MoveAction>>> {
        val elevationGrid: MutableMap<Position, HeightInfo> = mutableMapOf()
        val neighbourGrid: MutableMap<Position, MutableList<MoveAction>> = mutableMapOf()
        parseGrid(input, elevationGrid, neighbourGrid) { s: String ->
            when (s) {
                "S" -> HeightInfo(1, begin = true, eind = false)
                "a" -> HeightInfo(1, begin = false, eind = false)
                "b" -> HeightInfo(2, begin = false, eind = false)
                "c" -> HeightInfo(3, begin = false, eind = false)
                "d" -> HeightInfo(4, begin = false, eind = false)
                "e" -> HeightInfo(5, begin = false, eind = false)
                "f" -> HeightInfo(6, begin = false, eind = false)
                "g" -> HeightInfo(7, begin = false, eind = false)
                "h" -> HeightInfo(8, begin = false, eind = false)
                "i" -> HeightInfo(9, begin = false, eind = false)
                "j" -> HeightInfo(10,begin = false, eind = false)
                "k" -> HeightInfo(11,begin = false, eind = false)
                "l" -> HeightInfo(12,begin = false, eind = false)
                "m" -> HeightInfo(13,begin = false, eind = false)
                "n" -> HeightInfo(14,begin = false, eind = false)
                "o" -> HeightInfo(15,begin = false, eind = false)
                "p" -> HeightInfo(16,begin = false, eind = false)
                "q" -> HeightInfo(17,begin = false, eind = false)
                "r" -> HeightInfo(18,begin = false, eind = false)
                "s" -> HeightInfo(19,begin = false, eind = false)
                "t" -> HeightInfo(20,begin = false, eind = false)
                "u" -> HeightInfo(21,begin = false, eind = false)
                "v" -> HeightInfo(22,begin = false, eind = false)
                "w" -> HeightInfo(23,begin = false, eind = false)
                "x" -> HeightInfo(24,begin = false, eind = false)
                "y" -> HeightInfo(25,begin = false, eind = false)
                "z" -> HeightInfo(26,begin = false, eind = false)
                "E" -> HeightInfo(26,begin = false, eind = true)
                else -> {
                    HeightInfo(1000, begin = false, eind =  false)
                }
            }
        }
        return Pair(elevationGrid, neighbourGrid)
    }

    fun calculateSteps(
        startPosition: Position,
        elevationGrid: MutableMap<Position, HeightInfo>,
        neighbourGrid: MutableMap<Position, MutableList<MoveAction>>,
    ): Long {
        val queue = PriorityQueue { state1: State, state2: State -> state1.noSteps.compareTo(state2.noSteps) }
        // The map to record whether this state has been visited.
        val visited: MutableMap<ImmutablePair<Position, Direction>, Boolean> = mutableMapOf()
        queue.add(State(0, 1, startPosition, mutableListOf(Direction.EAST)))

        while (!queue.isEmpty()) {

            val state = queue.peek()
            queue.remove()

            if (elevationGrid[state.position]!!.eind) {
                return state.noSteps
            }

            // Check if we have already visited this state (position / from direction)
            // Important to have the current direction in it
            // If we have visited this position from another direction, it might be a shorter route!
            if (visited[ImmutablePair(state.position, state.historyOfMoves.last())] == true) {
                continue
            }
            visited[ImmutablePair(state.position, state.historyOfMoves.last())] = true

            // Examine all adjacent positions. The list op neighbours border safe!
            neighbourGrid[state.position]?.stream()?.forEach { moveAction ->
                val next = moveAction.position

                // Stop the move if the new height is not allowed
                if ((elevationGrid[next]!!.height > state.currentHeight.plus(1))) {
                    return@forEach
                }
                // Stop the move if it means we turn completely
                if (Direction.isOppositeDirection(state.historyOfMoves.last(), moveAction.direction)) {
                    return@forEach
                }

                // Keep track of the moves for debugging purposes
                val moves: MutableList<Direction> = state.historyOfMoves.toMutableList()
                moves.add(moveAction.direction)

                queue.add(State(state.noSteps.plus(1), elevationGrid[next]!!.height, next, moves))
            }

        }

        return 1000L
    }

    fun part1 (input : List<String>): Long {

        val (elevationGrid: MutableMap<Position, HeightInfo>, neighbourGrid: MutableMap<Position, MutableList<MoveAction>>) = init(input)

        val startPosition = elevationGrid.filter { pair -> pair.value.begin }.keys.first()
        return calculateSteps(startPosition, elevationGrid, neighbourGrid)
    }

    fun part2 (input : List<String>): Long {

        val (elevationGrid: MutableMap<Position, HeightInfo>, neighbourGrid: MutableMap<Position, MutableList<MoveAction>>) = init(input)

        val startPositions = elevationGrid.filter { pair -> pair.value.height == 1}.keys.toMutableSet()

        val numberOfSteps = startPositions.map { position: Position ->  calculateSteps(position, elevationGrid, neighbourGrid)}.toList()
        return numberOfSteps.min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    val input = readInput("Day12")

    check(part1(testInput) == 31L)
    part1(input).println()

    check(part2(testInput) == 29L)
    part2(input).println()

}

