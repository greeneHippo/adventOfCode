package nl.groen.year2024

import nl.groen.println
import nl.groen.readInput

fun main() {

    class Report(val levels : List<Int>) {

        fun isReportSafe(tolerateSingleLevel: Boolean): Boolean {

            val allLevels = isSafe(levels)
            if (!tolerateSingleLevel) return allLevels

            for (i in levels.indices) {
                val isSafe = isSafe(levels.filterIndexed { index, _ -> index != i  })
                if (isSafe) return true

            }

            return false

        }

        private fun isSafe(input: List<Int>): Boolean {
            var previous = 0
            val diffs: MutableList<Int> = mutableListOf()
            input.foldIndexed(diffs) { index, acc, int ->
                if (index == 0) {
                    previous = int
                } else {
                    acc.add(int - previous)
                    previous = int
                }
                acc
            }

            return diffs.min() >= -3 &&
                    diffs.max() <= 3 &&
                    !diffs.contains(0) &&
                    (diffs.max() < 0 || diffs.min() > 0)
        }
    }

    fun part1 (input : List<String>): Long {

        val reports = input.map { Report(it.split(" ").map { it.toInt()})}

        val result = reports.filter { it.isReportSafe(false) }
        return result.count().toLong()
    }

    fun part2 (input : List<String>): Long {

        val reports = input.map { Report(it.split(" ").map { it.toInt()})}

        val result = reports.filter { it.isReportSafe(true) }
        return result.count().toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day02_test")
    val input = readInput("2024","Day02")

    check(part1(testInput) == 2L)
    part1(input).println()

    check(part2(testInput) == 4L)
    part2(input).println()

}

