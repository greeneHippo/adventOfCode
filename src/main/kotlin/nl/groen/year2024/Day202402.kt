package nl.groen.year2024

import nl.groen.println
import nl.groen.readInput

fun main() {

    fun part1 (input : List<String>): Long {

        val ints = input.map { it.split(" ").map { it.toInt() } }
        var previous : Int = 0
        val diffs = ints.map {
            val diffs : MutableList<Int> = mutableListOf()
            it.foldIndexed(diffs) { index, acc, int ->
                if (index == 0) {
                    previous = int
                } else {
                    acc.add(int - previous)
                    previous = int
                }
                acc
            }
        }

        val filtered = diffs.filter { it.min() >= -3 && it.max() <= 3 && !it.contains(0)}
        val filtered2 = filtered.filter { it.max() < 0 || it.min() > 0 }

        return filtered2.size.toLong()
    }

    class Report(val levels : List<Int>) {

        var isIncrease : Boolean = true
        var hasSkippedOne : Boolean = false

        fun isSafe(): Boolean {

            levels.forEachIndexed { index, level ->  } {
                if (index == 0) return@forEach
                val diff = level - levels[index-1]
            }
        }
    }

    fun part2 (input : List<String>): Long {

        val reports = input.map { Report(it.split(" ").map { it.toInt()})}


        return reports.filter { it.isSafe() }.count().toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day02_test")
    val input = readInput("2024","Day02")

    check(part1(testInput) == 2L)
    part1(input).println()

    check(part2(testInput) == 4L)
    part2(input).println()

}

