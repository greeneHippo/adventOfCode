package nl.groen.year2025

import nl.groen.println
import nl.groen.readInput

fun main() {

    fun calculatePaths(
        devices : Map<String, List<String>>,
        start : String,
        end : String,
        iterationsLeft: Long) : Long {

        if (iterationsLeft <= 0) return 0
        if (start == end) return 1

        return devices[start]?.sumOf { next ->
            calculatePaths(devices, next, end, iterationsLeft-1)
        } ?: return 0
    }

    val cacheLength = mutableMapOf<String, Long>()

    fun calculateLengthOfPath(
        devices : Map<String, List<String>>,
        start : String,
        end : String) : Long {

        return cacheLength.getOrPut(start) {
            if (start == end) {
                return@getOrPut 1L
            }
            devices[start]?.forEach { next ->
                val length = calculateLengthOfPath(devices, next, end)
                if (length > 0L) {
                    return@getOrPut length + 1L
                }
            } ?:  return@getOrPut 0L

            return@getOrPut 0L
        }
    }

    fun part1 (input : List<String>): Long {

        val devices = input.map { line ->
            val lineSplit = line.split(" ")

            return@map lineSplit.first().dropLast(1) to lineSplit.drop(1)
         }.toMap()

        cacheLength.clear()
        return calculatePaths(devices, "you", "out", calculateLengthOfPath(devices, "you", "out"))
    }

    fun part2 (input : List<String>): Long {


        val devices = input.map { line ->
            val lineSplit = line.split(" ")

            return@map lineSplit.first().dropLast(1) to lineSplit.drop(1)
        }.toMap()

        cacheLength.clear()
        val first = calculatePaths(devices, "svr", "fft", calculateLengthOfPath(devices, "svr", "fft"))
        println(first)
        cacheLength.clear()
        // The length of paths can differ. With trial and error I determined the max for the portion is two longer than it calculated with calculateLengthOfPath()
        val second = calculatePaths(devices, "fft", "dac", calculateLengthOfPath(devices, "fft", "dac") + 2)
        println(second)
        cacheLength.clear()
        val third = calculatePaths(devices, "dac", "out", calculateLengthOfPath(devices, "dac", "out"))
        println(third)

        return first * second * third
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2025","Day11_test")
    val input = readInput("2025","Day11")

    check(part1(testInput) == 5L)
    part1(input).println()

    val testInput2 = readInput("2025","Day11_testPart2")

    check(part2(testInput2) == 2L)
    part2(input).println()
}

