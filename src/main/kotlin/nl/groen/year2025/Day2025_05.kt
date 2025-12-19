package nl.groen.year2025

import nl.groen.groupStringsOnEmptyLine
import nl.groen.mergeRanges
import nl.groen.println
import nl.groen.readInput

fun main() {

    fun part1 (input : List<String>): Long {

        val groupedStrings = groupStringsOnEmptyLine(input)
        val rangesFreshIngredients = groupedStrings[0].map {
            val splitted = it.split("-")
            return@map LongRange(splitted[0].toLong(), splitted[1].toLong())
        }
        val ingredientIds = groupedStrings[1].map { it.toLong() }

        return ingredientIds.count { ingredientId -> rangesFreshIngredients.any { range -> range.contains(ingredientId) } }.toLong()
    }

    fun part2 (input : List<String>): Long {

        val groupedStrings = groupStringsOnEmptyLine(input)
        val rangesFreshIngredients = groupedStrings[0].map {
            val splitted = it.split("-")
            return@map LongRange(splitted[0].toLong(), splitted[1].toLong())
        }

        val mergedRanges = rangesFreshIngredients.mergeRanges()

        return mergedRanges.sumOf { it.endInclusive - it.start + 1L }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2025","Day05_test")
    val input = readInput("2025","Day05")

    check(part1(testInput) == 3L)
    part1(input).println()

    check(part2(testInput) == 14L)
    part2(input).println()

}
