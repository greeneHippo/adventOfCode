package nl.groen.year2023.day21

import nl.groen.*
import java.util.PriorityQueue

enum class Status(val symbol: String) {
    GARDEN("."), ROCK("#"), START("S"), DESTINATION("O");

    companion object {
        fun getBySymbol(input: String): Status {
            return Status.entries.firstOrNull { status -> input == status.symbol }!!
        }
    }

    override fun toString() : String {
        return symbol
    }
}

private fun partOne (lines : List<String>, totalSteps: Int): Long {

    lines.forEach { println(it) }
    val grid : MutableMap<Position, Status> = mutableMapOf()
    val neighbourGrid: MutableMap<Position, MutableList<MoveAction>> = mutableMapOf()
    parseGrid(lines, grid, neighbourGrid) { s: String -> Status.getBySymbol(s) }

    var steps = 0
    while (steps < totalSteps) {

        val currentDestinations = grid.filter { it.value == Status.START || it.value == Status.DESTINATION }
        currentDestinations.forEach{
            val newDestinations = neighbourGrid[it.key]!!
                .map{mv -> mv.position}
                .filter { position -> grid[position] == Status.GARDEN }

            newDestinations.forEach { position ->
                grid[position] = Status.DESTINATION
            }
            grid[it.key] = Status.GARDEN
        }


        steps++
        println("$steps - ${grid.count { it.value == Status.DESTINATION }}")
    }

    val finalDestinations = grid.filter { it.value == Status.START || it.value == Status.DESTINATION }

    for (y in lines.indices) {
        for (x in lines[0].indices) {
            print(grid[Position(x,y)])
        }
        println("")
    }

    return finalDestinations.size.toLong()
}

private fun partTwoTest (lines : List<String>, totalSteps: Int): Long {

    val multipliedLines : MutableList<String> = mutableListOf()
    for (i in IntRange(1,5)) {
        lines.forEach {
            multipliedLines.add(it.repeat(5))
        }
    }
    multipliedLines[327] = multipliedLines[327].replaceRange(IntRange(327,327), "S")

    multipliedLines.forEach { println(it) }
    val grid : MutableMap<Position, Status> = mutableMapOf()
    val neighbourGrid: MutableMap<Position, MutableList<MoveAction>> = mutableMapOf()
    parseGrid(multipliedLines, grid, neighbourGrid) { s: String -> Status.getBySymbol(s) }

    var steps = 0
    while (steps < totalSteps) {

        val currentDestinations = grid.filter { it.value == Status.START || it.value == Status.DESTINATION }
        currentDestinations.forEach{
            val newDestinations = neighbourGrid[it.key]!!
                .map{mv -> mv.position}
                .filter { position -> grid[position] == Status.GARDEN }

            newDestinations.forEach { position ->
                grid[position] = Status.DESTINATION
            }
            grid[it.key] = Status.GARDEN
        }


        steps++
        println("$steps - ${grid.count { it.value == Status.DESTINATION }}")
    }

    val finalDestinations = grid.filter { it.value == Status.START || it.value == Status.DESTINATION }

    for (y in multipliedLines.indices) {
        for (x in multipliedLines[0].indices) {
            print(grid[Position(x,y)])
        }
        println("")
    }

    println(grid.filter{it.value == Status.DESTINATION }.count { it.key.x in IntRange(0,130) && it.key.y in IntRange(0,130)})
    println(grid.filter{it.value == Status.DESTINATION }.count { it.key.x in IntRange(131,261) && it.key.y in IntRange(131,261)})
    println(grid.filter{it.value == Status.DESTINATION }.count { it.key.x in IntRange(262,392) && it.key.y in IntRange(262,392)})
    println(grid.filter{it.value == Status.DESTINATION }.count { it.key.x in IntRange(393,523) && it.key.y in IntRange(393,523)})
    println(grid.filter{it.value == Status.DESTINATION }.count { it.key.x in IntRange(524,654) && it.key.y in IntRange(524,654)})

    return finalDestinations.size.toLong()
}

val cache : MutableMap<Position, Long> = mutableMapOf()
data class State(val numberOfSteps: Int, val position: Position, val result: Long)
private fun partTwo (lines : List<String>): Long {

    val grid : MutableMap<Position, Status> = mutableMapOf()
    val neighbourGrid: MutableMap<Position, MutableList<MoveAction>> = mutableMapOf()
    parseGrid(lines, grid, neighbourGrid) { s: String -> Status.getBySymbol(s) }

    val posW = Position(0,lines.size / 2)
    val posE = Position(lines.size-1,lines.size / 2)
    val posS = Position(lines.size / 2,lines.size-1)
    val posN = Position(lines.size / 2,0)
    val steps = lines.size-1

    val resultE = calculate(grid, listOf(posW), steps)
    val resultNE = calculate(grid, listOf(posS, posW), steps)
    val resultN = calculate(grid, listOf(posS), steps)
    val resultNW = calculate(grid, listOf(posS, posE), steps)
    val resultW = calculate(grid, listOf(posE), steps)
    val resultSW = calculate(grid, listOf(posN, posE), steps)
    val resultS = calculate(grid, listOf(posN) , steps)
    val resultSE = calculate(grid, listOf(posN, posW), steps)

    val resultEvenNE = calculate(grid, listOf(Position(0, steps)), (steps / 2)-1)
    val resultEvenNW = calculate(grid, listOf(Position(steps, steps)), (steps / 2)-1)
    val resultEvenSW = calculate(grid, listOf(Position(steps, 0)), (steps / 2)-1)
    val resultEvenSE = calculate(grid, listOf(Position(0, 0)), (steps / 2)-1)

    println("$resultE - $resultW - $resultN - $resultS")
    println("$resultNE - $resultNW - $resultSW - $resultSE")
    println("$resultEvenNE - $resultEvenNW - $resultEvenSW - $resultEvenSE")

    val fullOdd = calculate(grid, listOf(posS), 2 * steps + 1)
    val fullEven = calculate(grid, listOf(posS), 2 * steps)
    println("$fullOdd - $fullEven")

//    val iterations = (327L-65L)/131L
    val iterations = (26501365L-65L)/131L
    var result = 0L
    result += iterations*iterations*fullOdd
    result += (iterations-1)*(iterations-1)*fullEven
    result += resultE + resultW + resultN + resultS
    result += (resultNE + resultNW + resultSW + resultSE) * (iterations-1)
    result += (resultEvenNE + resultEvenNW + resultEvenSW + resultEvenSE) * (iterations)

    println("${calculate(grid, listOf(Position(lines.size / 2,lines.size / 2)), 64)}")
    
    return result
}

fun calculate(grid: MutableMap<Position, Status>, positions: List<Position>, numberSteps: Int) : Long {

    val queue = PriorityQueue { s1 : State, s2 : State -> s2.numberOfSteps - s1.numberOfSteps }
    positions.forEach {
        queue.add(State(numberSteps, it, 0L))
    }

    var result = 0L
    var steps = numberSteps
    val resultList = mutableListOf<Long>()
    while (queue.isNotEmpty()) {
        val state = queue.poll()
        if (state.numberOfSteps < 0) {
            continue
        }
        if (steps != state.numberOfSteps) {
            resultList.add(result)
            if (numberSteps-steps >= 1) {
                //println("After ${numberSteps - steps} steps -> ${result} -> difference ${result - resultList[numberSteps - steps - 1]}")
            }
            steps = state.numberOfSteps
        }

        if (grid[state.position] == null) {
            println(state.position)
        }
        if (cache[state.position] == null && grid[state.position]!! != Status.ROCK) {

            val marked = if (state.numberOfSteps % 2L == 0L) 1L else 0L
            cache[state.position] = marked
            result += marked

            if (state.position.x != 0) {
                queue.add(State(state.numberOfSteps - 1, Position(state.position.x - 1, state.position.y), 0L))
            }
            if (state.position.x != 130) {
                queue.add(State(state.numberOfSteps - 1, Position(state.position.x + 1, state.position.y), 0L))
            }
            if (state.position.y != 0) {
                queue.add(State(state.numberOfSteps - 1, Position(state.position.x, state.position.y - 1), 0L))
            }
            if (state.position.y != 130) {
                queue.add(State(state.numberOfSteps - 1, Position(state.position.x, state.position.y + 1), 0L))
            }
        }
    }

    cache.clear()
    return result
}

fun main(args : Array<String>) {

    val lines = readInput("day21_noS")
//    println(partOne(lines, 130))
//    println(partTwoTest(lines, 327))
    val linesSingle = readInput("day21")
    println(partTwo(lines))

}

