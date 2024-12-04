package nl.groen.year2024

import nl.groen.println
import nl.groen.readInput

fun main() {

    fun part1 (input : List<String>): Long {
        val regex = Regex("mul\\((\\d+),(\\d*)\\)")
        val memory = input.reduce { acc, s -> acc + s  }
        val regexResult = regex.findAll(memory).toList()
        val multiplies = regexResult.map { matchResult -> matchResult.groupValues[1].toInt() * matchResult.groupValues[2].toInt() }
        return multiplies.sum().toLong()
    }

    fun part2 (input : List<String>): Long {

        val regex = Regex("mul\\((\\d+),(\\d*)\\)")
        val regexDo = Regex("do\\(\\)")
        val regexDont = Regex("don't\\(\\)")
        val memory = input.reduce { acc, s -> acc + s  }
        val resultDo = regexDo.findAll(memory).toList()
        val resultDont = regexDont.findAll(memory).toList()

        val indexDo = resultDo.map { matchResult -> matchResult.range.last }
        val indexDont = resultDont.map { matchResult -> matchResult.range.last }

        val regexResult = regex.findAll(memory).toList()
        val multiplies = regexResult
            .filter { matchResult ->
                val placeLastDo = indexDo.filter { it < matchResult.range.first}.maxOfOrNull { it }
                val placeLastDont = indexDont.filter { it < matchResult.range.first}.maxOfOrNull { it }
                return@filter placeLastDont == null || (placeLastDo != null && placeLastDo > placeLastDont)
            }
            .map { matchResult -> matchResult.groupValues[1].toInt() * matchResult.groupValues[2].toInt() }

        return multiplies.sum().toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day03_test")
    val input = readInput("2024","Day03")

    check(part1(testInput) == 161L)
    part1(input).println()

    check(part2(testInput) == 48L)
    part2(input).println()

}

