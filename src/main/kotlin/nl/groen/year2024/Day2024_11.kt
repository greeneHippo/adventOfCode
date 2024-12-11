package nl.groen.year2024

import nl.groen.println
import nl.groen.readInput
import java.util.stream.IntStream

fun main() {

    fun fillMap(count: Long, newMap: MutableMap<Long, Long>, key: Long) {
        if (newMap[key] == null) {
            newMap[key] = count
        } else {
            newMap[key] = newMap[key]!! + count
        }
    }

    fun Map.Entry<Long, Long>.countAfterBlinking(iterations: Int): Long {

        var stones = mutableMapOf(Pair(this.key, this.value))
        for (i in 1..iterations) {
            val newMap = mutableMapOf<Long, Long>()
            for (key in stones.keys) {
                val count = stones[key]!!
                if (key == 0L) {
                    fillMap(count, newMap, 1L)
                } else if (key.toString().length % 2 == 0) {
                    var division = 1L
                    IntStream.range(0, key.toString().length / 2).forEach { division *= 10L }
                    fillMap(count, newMap, key % division)
                    fillMap(count, newMap, key / division)
                } else {
                    fillMap(count, newMap, key * 2024L)
                }

            }
            stones = newMap
        }

        return stones.values.sum()
    }

    fun part1 (input : List<String>): Long {

        val stones = input[0].split(" ").associate { it.toLong() to 1L }
        val stonesAfterBlinks :List<Long> = stones.map { it.countAfterBlinking(25) }

        return stonesAfterBlinks.sum()
    }

    fun part2 (input : List<String>): Long {

        val stones = input[0].split(" ").associate { it.toLong() to 1L }
        val stonesAfterBlinks :List<Long> = stones.map { it.countAfterBlinking(75) }

        return stonesAfterBlinks.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day11_test")
    val input = readInput("2024","Day11")

    check(part1(testInput) == 55312L)
    part1(input).println()

//    check(part2(testInput) == 1L)
    part2(input).println()

}

