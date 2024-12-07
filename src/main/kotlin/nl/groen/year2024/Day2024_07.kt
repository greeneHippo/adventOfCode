package nl.groen.year2024

import nl.groen.println
import nl.groen.readInput

fun main() {

    fun isValid(result: Long, intermediateResult: Long, remainingElements :List<Long>, addConcat: Boolean ) : Boolean {

        val productResult = intermediateResult * remainingElements[0]
        val sumResult = intermediateResult + remainingElements[0]
        val concatResult = (intermediateResult.toString() + remainingElements[0].toString()).toLong()

        if (remainingElements.size == 1) {
            return productResult == result ||
                    sumResult == result ||
                    (addConcat && concatResult == result)
        }

        return isValid(result, productResult, remainingElements.drop(1), addConcat) ||
               isValid(result, sumResult, remainingElements.drop(1), addConcat) ||
               ( addConcat && isValid(result, concatResult, remainingElements.drop(1), addConcat))
    }

    fun part1 (input : List<String>): Long {

        val equations = input.map { it.split(": ", " ").map { it.toLong() } }
        val validEquations = equations.filter { isValid(it[0], it[1], it.drop(2), false) }
        return validEquations.map { it[0] }.sum()
    }

    fun part2 (input : List<String>): Long {

        val equations = input.map { it.split(": ", " ").map { it.toLong() } }
        val validEquations = equations.filter { isValid(it[0], it[1], it.drop(2), true) }
        return validEquations.map { it[0] }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day07_test")
    val input = readInput("2024","Day07")

    check(part1(testInput) == 3749L)
    part1(input).println()

    check(part2(testInput) == 11387L)
    part2(input).println()

}



