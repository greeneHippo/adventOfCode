package nl.groen.year2024

import nl.groen.println
import nl.groen.readInput

fun main() {

    fun Long.calculateNext() :Long {
        var secret = this
        secret = secret xor (secret * 64)
        secret %= 16777216
        secret = secret xor (secret / 32)
        secret %= 16777216
        secret = secret xor (secret * 2048)
        secret %= 16777216

        return secret
    }

    fun part1 (input : List<String>): Long {

        var secretNumbers = input.map { it.toLong() }
        IntRange(1, 2000).forEach { _ ->
            secretNumbers = secretNumbers.map { it.calculateNext() }
        }
        return secretNumbers.sum()
    }

    data class PreviousFourDifferences(val first: Long?, val second: Long?, val third: Long?, val fourth: Long?) {

        fun createNew(newValue :Long) : PreviousFourDifferences {
            return PreviousFourDifferences(newValue, first, second, third )
        }

    }
    val result = HashMap<Pair<Int, PreviousFourDifferences>, Long>()
    fun part2 (input : List<String>): Long {

        val secretNumbers = input.map { it.toLong() }
        secretNumbers.forEachIndexed { index, initialPrice ->
            var history = PreviousFourDifferences(null, null, null, null)
            var secretNumber = initialPrice
            IntRange(1, 2000).forEach { iteration ->
                val newValue = secretNumber.calculateNext()
                val price = newValue % 10
                history = history.createNew(price - secretNumber % 10)
                if (result[Pair(index, history)] == null && iteration >= 4) {
                    result[Pair(index, history)] = newValue % 10
                }
                secretNumber = newValue
            }
        }

        val maxPrice = result.entries.groupBy { it.key.second }.values.maxOf { list -> list.sumOf { it.value } }
        return maxPrice
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day22_test")
    val testInputPart2 = readInput("2024","Day22_test2")
    val input = readInput("2024","Day22")

    check(part1(testInput) == 37327623L)
    part1(input).println()

    check(part2(testInputPart2) == 23L)
    part2(input).println()

}

