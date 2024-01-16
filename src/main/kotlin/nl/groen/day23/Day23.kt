package nl.groen.day23

import nl.groen.*
import java.util.PriorityQueue

enum class Type (val symbol :String) {
    PATH("."), FOREST("#"), SLOPE_SOUTH("v"), SLOPE_EAST(">");

    companion object {
        fun getBySymbol(input: String): Type {
            return Type.entries.firstOrNull { status -> input == status.symbol }!!
        }
    }

    override fun toString() : String {
        return symbol
    }
}

data class State(var steps :Int, val currentPosition :Position, val visited :MutableList<Position>)
private fun partOne (lines : List<String>): Long {

    val grid : MutableMap<Position, Type> = mutableMapOf()
    val neighbourGrid: MutableMap<Position, MutableList<MoveAction>> = mutableMapOf()
    parseGrid(lines, grid, neighbourGrid) {s :String -> Type.getBySymbol(s)}

    val maxId = grid.maxOf { it.key.x }
//    for (y in 0..maxId) {
//        for (x in 0..maxId) {
//            print(grid[Position(x,y)])
//        }
//        println()
//    }

    val routes :MutableList<Int> = mutableListOf()
    val pq = PriorityQueue { s1: State, s2: State -> s1.steps - s2.steps}
    pq.add(State(0, Position(1,0), mutableListOf(Position(1,0)) ))
    while(pq.isNotEmpty()) {

        val state = pq.poll()
        val currentPosition = state.currentPosition
        //println("$currentPosition - ${state.steps}")

        if (currentPosition == Position(maxId -1, maxId)) {
            //println("End point reached in ${state.steps} steps.")
            routes.add(state.steps)
            continue
        }

        val nextPoints = neighbourGrid[currentPosition]!!.filter { grid[it.position]!! != Type.FOREST && !state.visited.contains(it.position)}

        nextPoints.forEach {

            if (grid[it.position]!! == Type.SLOPE_SOUTH && it.direction != Direction.SOUTH ) {
                return@forEach
            }
            if (grid[it.position]!! == Type.SLOPE_EAST && it.direction != Direction.EAST ) {
                return@forEach
            }

            val newVisited : MutableList<Position> = state.visited.toMutableList()
            newVisited.add(it.position)
            pq.add(State(state.steps + 1, it.position, newVisited))
        }


    }
    return routes.max().toLong()
}

data class NextNode(val position :Position, val steps :Int)

private fun partTwo (lines : List<String>): Long {

    val grid : MutableMap<Position, Type> = mutableMapOf()
    val neighbourGrid: MutableMap<Position, MutableList<MoveAction>> = mutableMapOf()
    parseGrid(lines, grid, neighbourGrid) {s :String -> Type.getBySymbol(s)}

    val maxId = grid.maxOf { it.key.x }

    val nodeList = neighbourGrid.filter {
        (grid[it.key]!! != Type.FOREST && it.value.count { action -> grid[action.position]!! != Type.FOREST } > 2) ||
                it.key == Position(1,0) ||
                it.key == Position(maxId - 1, maxId)
    }
    val nodes: MutableMap<Position, MutableList<NextNode>> = nodeList.keys.associateWith {
        val nodes = nodeList[it]!!.filter { action -> grid[action.position]!! != Type.FOREST }
        nodes.map { next -> determineNextNode(it, next, nodeList.keys, grid, neighbourGrid)}.toMutableList()
    }.toMutableMap()

    val routes :MutableList<Int> = mutableListOf()
    val pq = PriorityQueue { s1: State, s2: State -> s1.steps - s2.steps}
    pq.add(State(0, Position(1, 0), mutableListOf(Position(1, 0))))
    while(pq.isNotEmpty()) {

        val state = pq.poll()
        val currentPosition = state.currentPosition
        //println("$currentPosition - ${state.steps}")

        if (currentPosition == Position(maxId - 1, maxId)) {
            //println("End point reached in ${state.steps} steps.")
            routes.add(state.steps)
            continue
        }

        val nextPoints = nodes[currentPosition]!!.filter { !state.visited.contains(it.position) }

        nextPoints.forEach {

            val newVisited: MutableList<Position> = state.visited.toMutableList()
            newVisited.add(it.position)
            pq.add(State(state.steps + it.steps, it.position, newVisited))
        }

    }
    return routes.max().toLong()
}

fun determineNextNode(first: Position, next: MoveAction, nodelist: Set<Position>, grid : Map<Position, Type>, neighbourGrid :Map<Position, MutableList<MoveAction>>) : NextNode {

    val pq = PriorityQueue { s1: State, s2: State -> s1.steps - s2.steps}
    pq.add(State(1, next.position, mutableListOf(first, next.position)))
    while(pq.isNotEmpty()) {

        val state = pq.poll()
        val currentPosition = state.currentPosition

        if (nodelist.contains(currentPosition)) {
            return NextNode(currentPosition, state.steps)
        }

        val nextPoints = neighbourGrid[currentPosition]!!.filter { grid[it.position]!! != Type.FOREST && !state.visited.contains(it.position) }

        nextPoints.forEach {

            val newVisited: MutableList<Position> = state.visited.toMutableList()
            newVisited.add(it.position)
            pq.add(State(state.steps + 1, it.position, newVisited))
        }
    }

    throw Error("wrong")
}

fun main(args : Array<String>) {

    val lines = readLines("day23")
    println(partOne(lines))
    println(partTwo(lines))

}

