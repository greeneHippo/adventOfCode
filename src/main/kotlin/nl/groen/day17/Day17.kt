package nl.groen.day17

import nl.groen.*
import org.apache.commons.lang3.tuple.ImmutableTriple
import java.util.*

/**
 * The data structure to store the coordinates of the unit square and the cost of path from the top left.
 * */
data class State(val position: Position, val cost: Int, val noSteps: Int, val historyOfMoves: MutableList<Direction>)

fun partOne (lines : List<String>): Long {

    lines.forEach { println(it) }
    val costGrid : MutableMap<Position, Int> = mutableMapOf()
    val neighbourGrid: MutableMap<Position, MutableList<MoveAction>> = mutableMapOf()
    parseGrid(lines, costGrid, neighbourGrid) { s: String -> s.toInt() }

    return calculateMinimum(costGrid, neighbourGrid, Position(lines.first().length-1, lines.size-1), 0,3).toLong()

}

private fun partTwo (lines : List<String>): Long {

    val costGrid : MutableMap<Position, Int> = mutableMapOf()
    val neighbourGrid : MutableMap<Position, MutableList<MoveAction>> = mutableMapOf()
    parseGrid(lines, costGrid, neighbourGrid) { s: String -> s.toInt() }

    return calculateMinimum(costGrid, neighbourGrid, Position(lines.first().length-1, lines.size-1), 4,10).toLong()
}

/**
 * Minimum cost
 */
fun calculateMinimum(costGrid: MutableMap<Position, Int>, neighbourGrid :MutableMap<Position, MutableList<MoveAction>>, target :Position, minimumSteps : Int, maximumSteps: Int): Int {

    // The map to record whether this state has been visited.
    val visited: MutableMap<ImmutableTriple<Position, Direction, Int>, Boolean> = mutableMapOf()

    // Define the priority queue
    val pq : PriorityQueue<State> = PriorityQueue<State> { left: State, right: State -> left.cost - right.cost }

    // Initialize the two starting moves from the top left unit with the cost and add it to the queue as the first moves to evaluate.
    pq.add(State(Position(0,0), 0, 0, mutableListOf(Direction.EAST)))
    pq.add(State(Position(0,0), 0, 0, mutableListOf(Direction.SOUTH)))

    while (!pq.isEmpty()) {
        // Pop a move from the queue, ignore the units already visited
        val state = pq.peek()
        pq.remove()

        if (state.position == target && state.noSteps >= minimumSteps) {
            // We have reached the target position. Print the history of moves and return the cost.
            state.historyOfMoves.removeAt(0)
            println(state.historyOfMoves)
            return state.cost
        }

        // Check if we have already visited this state (position / from direction / steps
        // Important to have the current direction in it and the number of steps!
        // If we have visited this position from another direction, or with other amount of steps in this direction, it might be a shorter route!
        if (visited[ImmutableTriple(state.position, state.historyOfMoves.last(), state.noSteps)] == true) {
            continue
        }

        // Mark the state as visited.
        visited[ImmutableTriple(state.position, state.historyOfMoves.last(), state.noSteps)] = true

        // Examine all adjacent positions. The list op neighbours border safe!
        neighbourGrid[state.position]?.stream()?.forEach { moveAction ->
            val next = moveAction.position

            // Stop the move if it would result in too many steps forward
            if (moveAction.direction == state.historyOfMoves.last() && state.noSteps == maximumSteps) {
                return@forEach
            }
            // Stop the move if we have not yet moved enough steps in the same direction
            if (moveAction.direction != state.historyOfMoves.last() && state.noSteps < minimumSteps ) {
                return@forEach
            }
            // Stop the move if it means we turn completely
            if (Direction.isOppositeDirection(state.historyOfMoves.last(), moveAction.direction)) {
                return@forEach
            }

            // Calculate the next state.
            val cost = state.cost + costGrid[moveAction.position]!!
            // Keep track of the moves for debugging purposes
            val moves : MutableList<Direction> = state.historyOfMoves.toMutableList()
            moves.add(moveAction.direction)

            if (moveAction.direction == state.historyOfMoves.last()) {
                pq.add(State(next, cost, state.noSteps+1, moves))
            } else {
                pq.add(State(next, cost, 1, moves))
            }
        }
    }

    return -1
}

fun main(args : Array<String>) {

    val lines = readLines("day17")
    println(partOne(lines))
    println(partTwo(lines))
}

