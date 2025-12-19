package nl.groen

import kotlin.math.abs

fun main() {

    data class Motion(val direction: Direction, val steps: Int)

    fun shouldTailMove(positionHead : Position, positionTail : Position) : Boolean {

        if (abs(positionHead.x - positionTail.x) > 1) {
            return true
        }

        if (abs(positionHead.y - positionTail.y) > 1) {
            return true
        }

        return false
    }

    fun determineNewPosition(positionHead : Position, positionTail : Position) : Position? {

        if (abs(positionHead.x - positionTail.x) <= 1 && abs(positionHead.y - positionTail.y) <= 1) {
            return null
        }

        val newX = positionTail.x + (positionHead.x - positionTail.x) / 2 + (positionHead.x - positionTail.x) % 2
        val newY = positionTail.y + (positionHead.y - positionTail.y) / 2 + (positionHead.y - positionTail.y) % 2
        return Position(newX, newY)
    }

    fun printResult(positionsTail: MutableSet<Position>) {
        println("-------------------------------------")
        for (y in positionsTail.maxOf { it.y } downTo positionsTail.minOf { it.y }) {
            for (x in positionsTail.minOf { it.x } .. positionsTail.maxOf { it.x }) {
                if (positionsTail.contains(Position(x, y))) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println()
        }
    }

    fun printResult(positionsTail: MutableList<Position>) {
        println("-------------------------------------")
        for (y in positionsTail.maxOf { it.y } downTo positionsTail.minOf { it.y }) {
            for (x in positionsTail.minOf { it.x } .. positionsTail.maxOf { it.x }) {
                if (positionsTail.contains(Position(x, y))) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println()
        }
    }

    fun part1 (input : List<String>): Long {

        val positionsTail : MutableSet<Position> = mutableSetOf()
        var positionTail = Position(0,0)
        val positionHead = Position(0,0)
        positionsTail.add(positionTail)

        input
            .map {
                val (direction, steps) = it.split(" ")
                Motion(Direction.getByULDR(direction), steps.toInt())
            }.fold(positionHead) { acc, motion ->

                val positionsHeads : List<Pair<Position, Position>> = when(motion.direction) {
                    Direction.NORTH -> IntRange(1, motion.steps).map { i -> Pair(Position(acc.x, acc.y+i-1), Position(acc.x, acc.y+i))}
                    Direction.EAST ->  IntRange(1, motion.steps).map { i -> Pair(Position(acc.x+i-1, acc.y), Position(acc.x+i, acc.y))}
                    Direction.SOUTH ->  IntRange(1, motion.steps).map { i -> Pair(Position(acc.x, acc.y-i+1), Position(acc.x, acc.y-i))}
                    Direction.WEST ->  IntRange(1, motion.steps).map { i -> Pair(Position(acc.x-i+1, acc.y), Position(acc.x-i, acc.y))}
                    else -> throw IllegalArgumentException("Unknown direction")
                }
                positionsHeads.forEach {
                    if (shouldTailMove(it.second, positionTail)) {
                        positionsTail.add(it.first)
                        positionTail = it.first
                    }
                }
                positionsHeads.last().second
            }

        //printResult(positionsTail)
        return positionsTail.size.toLong()
    }

    fun part2 (input : List<String>): Long {

        var positionsKnot : MutableList<Position> = mutableListOf()
        var positionTail = Position(0,0)
        val positionHead = Position(0,0)
        positionsKnot.add(positionTail)

        input
            .map {
                val (direction, steps) = it.split(" ")
                Motion(Direction.getByULDR(direction), steps.toInt())
            }.fold(positionHead) { acc, motion ->

                val positionsHeads : List<Pair<Position, Position>> = when(motion.direction) {
                    Direction.NORTH -> IntRange(1, motion.steps).map { i -> Pair(Position(acc.x, acc.y+i-1), Position(acc.x, acc.y+i))}
                    Direction.EAST ->  IntRange(1, motion.steps).map { i -> Pair(Position(acc.x+i-1, acc.y), Position(acc.x+i, acc.y))}
                    Direction.SOUTH ->  IntRange(1, motion.steps).map { i -> Pair(Position(acc.x, acc.y-i+1), Position(acc.x, acc.y-i))}
                    Direction.WEST ->  IntRange(1, motion.steps).map { i -> Pair(Position(acc.x-i+1, acc.y), Position(acc.x-i, acc.y))}
                    else -> throw IllegalArgumentException("Unknown direction")
                }
                positionsHeads.forEach {
                    if (shouldTailMove(it.second, positionTail)) {
                        positionsKnot.add(it.first)
                        positionTail = it.first
                    }
                }
                positionsHeads.last().second
            }

        printResult(positionsKnot)

        IntRange(2,9).forEach {
            var positionNextKnot = Position(0, 0)
            val positionsNextKnot: MutableList<Position> = mutableListOf(positionNextKnot)
            positionsKnot.foldIndexed(positionsNextKnot) { i: Int, acc, position ->

                val newPosition = determineNewPosition(position, positionNextKnot)
                if (newPosition != null) {
                    acc.add(newPosition)
                    positionNextKnot = newPosition
                }
                acc
            }

            printResult(positionsNextKnot)
            positionsKnot = positionsNextKnot.toMutableList()

        }

        // Reduce List to Set to remove duplicates.
        return positionsKnot.toMutableSet().size.toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    val testInputPart2 = readInput("Day09_testPart2")
    val input = readInput("Day09")

    check(part1(testInput) == 13L)
    part1(input).println()

    check(part2(testInput) == 1L)
    check(part2(testInputPart2) == 36L)
    part2(input).println()

}

