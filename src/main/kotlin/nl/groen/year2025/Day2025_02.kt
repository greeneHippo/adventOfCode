package nl.groen.year2025

import nl.groen.println
import nl.groen.readInput

fun main() {

    fun calculateStartForUneven(input: Long): Long {
        val numberCharStart = input.toString().length
        val numberZeroes = (numberCharStart / 2)
        var t = "1"
        for (i in 1..numberZeroes) {
            t += "0"
        }
        return t.toLong()
    }

    fun calculateMultiplier(input: Long): Long {
        val numberCharStart = input.toString().length
        val numberZeroes = (numberCharStart / 2)
        var t = "1"
        for (i in 1..numberZeroes) {
            t += "0"
        }
        return t.toLong()
    }

    fun part1 (input : List<String>): Long {

        val ranges = input[0].split(",").map { range ->
                val intRange = range.split("-")
                return@map LongRange(intRange[0].toLong(), intRange[1].toLong())
            }

        var result = 0L
        ranges.forEach {

            val numberCharStart = it.first.toString().length
            var start: Long
            if (numberCharStart % 2 == 0) {
                start = it.first.toString().substring(0, numberCharStart / 2).toLong()
                val lastHalf = it.first.toString().substring(numberCharStart / 2, numberCharStart).toLong()
                if (start < lastHalf) {
                    start++
                }
            } else {
                start = calculateStartForUneven(it.first)
            }

            while (true) {
                val doublure = (start.toString() + start.toString()).toLong()
                if (it.contains(doublure)) {
//                    println("$it : $doublure")
                    result += doublure
                } else {
                    return@forEach
                }
                start++
            }
        }

        return result
    }

    fun part2 (input : List<String>): Long {

        val ranges = input[0].split(",").map { range ->
            val intRange = range.split("-")
            return@map LongRange(intRange[0].toLong(), intRange[1].toLong())
        }

        var result = 0L
        var invalidProductCodes = mutableListOf<Long>()

//        println(ranges.maxOfOrNull { it.last() })
//        ranges.forEach{ if (it.first.toString().length != it.last().toString().length ) println(it) }

        fun createInvalidProductCode(repeatedElement: Int, occurrences: Int) : Long {
            var tmpStr = repeatedElement.toString()
            for (k in 1..<occurrences) {
                tmpStr += repeatedElement.toString()
            }
            return tmpStr.toLong()
        }

        fun checkElement(i: Int, j: Int) {
            val invalidProductCode = createInvalidProductCode(i, j)
            if (invalidProductCodes.contains(invalidProductCode)) {
                return
            }
            ranges.forEach {
                if (it.contains(invalidProductCode)) {
                    println("$it : $invalidProductCode")
                    invalidProductCodes.add(invalidProductCode)
                    result += invalidProductCode
                }
            }
        }

        for (i in 1..9) {
            for (j in 2..11) {
                checkElement(i, j)
            }
        }

        for (i in 10..99) {
            for (j in 2..5) {
                checkElement(i, j)
            }
        }

        for (i in 100..999) {
            for (j in 2..3) {
                checkElement(i, j)
            }
        }

        for (i in 1000..9999) {
            for (j in 2..2) {
                checkElement(i, j)
            }
        }

        for (i in 10000..99999) {
            for (j in 2..2) {
                checkElement(i, j)
            }
        }

        println(result)
        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2025","Day02_test")
    val input = readInput("2025","Day02")

    check(part1(testInput) == 1227775554L)
    part1(input).println()

    check(part2(testInput) == 4174379265L)
    part2(input).println()
}

