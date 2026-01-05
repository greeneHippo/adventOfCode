package nl.groen.year2023.day1

import nl.groen.println
import nl.groen.readInput
import org.apache.commons.lang3.StringUtils

fun main() {

    fun solution (input : List<String>, includeWords: Boolean): Long {

        val digits = input.stream().map {
            val characters = it.toCharArray()
            val twoDigitString = CharArray(2)
            twoDigitString[0] = extractFistDigit(characters, includeWords)
            twoDigitString[1] = extractLastDigit(characters, includeWords)

            return@map twoDigitString.concatToString().toInt()
        }

        val sum = digits.reduce{sum, element -> sum + element}
        return sum.get().toLong()
    }

    fun part1 (input : List<String>): Long {
        return solution(input, false)
    }

    fun part2 (input : List<String>): Long {
        return solution(input, true)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2023","Day01_test")
    val testInput2 = readInput("2023","Day01_test2")
    val input = readInput("2023","Day01")

    check(part1(testInput) == 142L)
    part1(input).println()

    check(part2(testInput2) == 281L)
    part2(input).println()
}

private fun extractFistDigit(characters: CharArray, includeWords: Boolean): Char {
    for (i in characters.indices) {

        if (characters[i].isDigit()) {
            return characters[i]
        }

        if (!includeWords) {
            continue
        }

        val digitInWord = digitInWord(i, characters.concatToString())
        if (digitInWord != null) {
            return digitInWord
        }

    }

    throw RuntimeException("No digit")
}

private fun extractLastDigit(characters: CharArray, includeWords: Boolean): Char {
    for (i in characters.indices) {
        if (characters[characters.size-i-1].isDigit()) {
            return characters[characters.size-i-1]
        }

        if (!includeWords) {
            continue
        }

        val digitInWord = digitInWordRecursive(characters.size - i, characters.concatToString())
        if (digitInWord != null) {
            return digitInWord
        }
    }

    throw RuntimeException("No digit")
}

private fun digitInWord(i: Int, string: String): Char? {
    if ("one" == StringUtils.substring(string, i, i+3)) {
        return "1".toCharArray()[0]
    }
    if ("two" == StringUtils.substring(string, i, i+3)) {
        return "2".toCharArray()[0]
    }
    if ("three" == StringUtils.substring(string, i, i+5)) {
        return "3".toCharArray()[0]
    }
    if ("four" == StringUtils.substring(string, i, i+4)) {
        return "4".toCharArray()[0]
    }
    if ("five" == StringUtils.substring(string, i, i+4)) {
        return "5".toCharArray()[0]
    }
    if ("six" == StringUtils.substring(string, i, i+3)) {
        return "6".toCharArray()[0]
    }
    if ("seven" == StringUtils.substring(string, i, i+5)) {
        return "7".toCharArray()[0]
    }
    if ("eight" == StringUtils.substring(string, i, i+5)) {
        return "8".toCharArray()[0]
    }
    if ("nine" == StringUtils.substring(string, i, i+4)) {
        return "9".toCharArray()[0]
    }

    return null
}

fun digitInWordRecursive(i: Int, string: String): Char? {
    if ("one" == StringUtils.substring(string, i-3, i)) {
        return "1".toCharArray()[0]
    }
    if ("two" == StringUtils.substring(string, i-3, i)) {
        return "2".toCharArray()[0]
    }
    if ("three" == StringUtils.substring(string, i-5, i)) {
        return "3".toCharArray()[0]
    }
    if ("four" == StringUtils.substring(string, i-4, i)) {
        return "4".toCharArray()[0]
    }
    if ("five" == StringUtils.substring(string, i-4, i)) {
        return "5".toCharArray()[0]
    }
    if ("six" == StringUtils.substring(string, i-3, i)) {
        return "6".toCharArray()[0]
    }
    if ("seven" == StringUtils.substring(string, i-5, i)) {
        return "7".toCharArray()[0]
    }
    if ("eight" == StringUtils.substring(string, i-5, i)) {
        return "8".toCharArray()[0]
    }
    if ("nine" == StringUtils.substring(string, i-4, i)) {
        return "9".toCharArray()[0]
    }

    return null
}

