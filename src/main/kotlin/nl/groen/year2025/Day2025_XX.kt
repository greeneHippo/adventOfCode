package nl.groen.year2025

import nl.groen.println
import nl.groen.readInput

fun main() {

    fun part1 (input : List<String>): Long {

        return input.size.toLong()
    }

    fun part2 (input : List<String>): Long {

        return input.size.toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2025","DayXX_test")
    val input = readInput("2025","DayXX")

    check(part1(testInput) == 1L)
    part1(input).println()

    check(part2(testInput) == 1L)
    part2(input).println()

}

