package nl.groen.year2024

import nl.groen.println
import nl.groen.readInput

enum class PatternType{
    KEY,
    LOCK;
}
fun main() {

    data class Pattern(var hashtags :MutableList<Int>) {

        lateinit var type :PatternType

        fun add(input :List<Boolean>) {

            if (hashtags.isEmpty()) {
                hashtags.addAll(input.map { if (it) { 1 } else 0 } )
                if (input[0]) {
                    type = PatternType.LOCK
                } else {
                    type = PatternType.KEY
                }
                return
            }

            input.forEachIndexed { index, b ->
                hashtags[index] += if (b) { 1 } else 0
            }
        }
    }

    fun part1 (input : List<String>): Long {


        val patterns: MutableList<Pattern> = mutableListOf(Pattern(mutableListOf()))

        input.fold(patterns) { acc, s ->
            if (s.isEmpty()) {
                acc.add(Pattern(mutableListOf()))
            } else {
                acc.last().add(s.map { it.toString() == "#" })
            }
            acc
        }

        val locks = patterns.filter { it.type == PatternType.LOCK }
        val keys = patterns.filter { it.type == PatternType.KEY }

        var result = 0L
        keys.forEach { key ->
            locks.forEach { lock ->
                val sum = key.hashtags.mapIndexed { index, hashtagsKey ->  hashtagsKey + lock.hashtags[index]}
                if (sum.all { it <= 7 }) {
                    result++
                }

            }
        }

        return result
    }

    fun part2 (input : List<String>): Long {

        return input.size.toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day25_test")
    val input = readInput("2024","Day25")

    check(part1(testInput) == 3L)
    part1(input).println()

    check(part2(testInput) == 1L)
    part2(input).println()

}

