package nl.groen.year2024

import nl.groen.groupStringsOnEmptyLine
import nl.groen.println
import nl.groen.readInput

private val cache = hashMapOf<String, Long>()
fun main() {

    fun String.checkDesign(patterns :List<String>) : Boolean {

        if (this.isEmpty()) {
            return true
        }

        patterns.forEach { pattern ->
            if (this.startsWith(pattern)) {
                val subString = substring(pattern.length)
                val valid = subString.checkDesign(patterns)
                if (valid) {
                    return true
                }
            }
        }

        return false
    }

    fun String.countDesign(patterns :List<String>, count: Long) : Long {

        return cache.getOrPut(this) {

            var result = count
            if (this.isEmpty()) {
                result++
            } else {

                patterns.forEach { pattern ->
                    if (this.startsWith(pattern)) {
                        val subString = substring(pattern.length)
                        result += subString.countDesign(patterns, count)
                    }
                }
            }
            result
        }
    }

    fun part1 (input : List<String>): Long {

        val strings = groupStringsOnEmptyLine(input)
        val towelPatterns = strings[0][0].split(", ")
        val designs = strings[1].toList()

        return designs.mapIndexed { index, s ->
            println("Evaluate design $index")
            return@mapIndexed s.checkDesign(towelPatterns)}.count { it }.toLong()
    }

    fun part2 (input : List<String>): Long {

        val strings = groupStringsOnEmptyLine(input)
        val towelPatterns = strings[0][0].split(", ")
        val designs = strings[1].toList()

        val result = designs.mapIndexed { index, s ->
            println("Evaluate design $index")
            return@mapIndexed s.countDesign(towelPatterns, 0L) }.sum()
        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day19_test")
    val input = readInput("2024","Day19")

    check(part1(testInput) == 6L)
    part1(input).println()

    check(part2(testInput) == 16L)
    cache.clear()
    part2(input).println()

}

