package nl.groen.year2025

import nl.groen.println
import nl.groen.readInput

fun main() {

    fun getHighestElementAtIndex(inputString: String, numberCharacterToIgnore: Int) : Pair<Int, Int> {
        for (element in 9 downTo 0) {
            val index = inputString.indexOf(element.toString())
            if (index == -1) {
                continue
            }
            if (numberCharacterToIgnore > 0 && index >= inputString.length - numberCharacterToIgnore) {
                continue
            }

            return Pair(element, index)
        }

        throw IllegalArgumentException("Invalid input")
    }

    fun determineHighestValueForBank(numberOfBatteries: Int, string: String): Long {
        var jolt = ""
        var index = -1
        for (i in numberOfBatteries - 1 downTo 0) {
            val foundElement = getHighestElementAtIndex(string.substring(index + 1), i)
            jolt += foundElement.first.toString()
            index += foundElement.second + 1
        }
        println("$string - $jolt")
        return jolt.toLong()
    }

    fun part1 (input : List<String>): Long {

        val highestValuesEachBank = input.map {
            return@map determineHighestValueForBank(2, it)
        }.toList()

        return highestValuesEachBank.sum()
    }

    fun part2 (input : List<String>): Long {

        val highestValuesEachBank = input.map {
            return@map determineHighestValueForBank(12, it)
        }.toList()

        println(highestValuesEachBank.forEach { println(it) })
        return highestValuesEachBank.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2025","Day03_test")
    val input = readInput("2025","Day03")

    check(part1(testInput) == 357L)
    part1(input).println()

    check(part2(testInput) == 3121910778619L)
    part2(input).println()

}

