package nl.groen.year2025

import nl.groen.println
import nl.groen.readInput
import kotlin.math.abs

fun main() {

    fun part1 (input : List<String>): Long {

        val rotations = input.map { Pair(it[0], it.substring(1).toInt())}.toList()

        var result = 0L
        var dial = 50
        rotations.forEach {
            if (it.first == 'L') {
                dial-= it.second
            } else {
                dial+= it.second
            }
            if (dial % 100 == 0) {
                result += 1
            }
        }
        println(result)
        return result
    }

    fun part2 (input : List<String>): Long {

        val rotations = input.map { Pair(it[0], it.substring(1).toInt())}.toList()

        var result = 0L
        var dial = 50
        rotations.forEach {
            val olddial = dial
            println("olddial: $olddial")
            if (it.first == 'L') {
                dial-= it.second
                println("after dial: $dial")
                if (dial == 0) {
                    result +=1
                }
                if (dial < 0) {
                    result += (abs(dial / 100)) + 1
                    if (olddial == 0) {
                        result -=1
                    }
                    if (dial % 100 != 0) {
                        dial = (dial % 100) + 100
                    } else {
                        dial = (dial % 100)
                    }
                }
            } else {
                dial+= it.second
                println("after dial: $dial")
                if (dial == 100) {
                    result +=1
                    dial = 0
                }
                if (dial >= 100) {
                    result += abs(dial / 100)
//                    if (olddial == 0) {
//                        result +=1
//                    }
                    dial = (dial % 100)
                }

            }
            println("dial: $dial, result: $result" )
        }
        println(result)
        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2025","Day01_test")
    val input = readInput("2025","Day01")

    check(part1(testInput) == 3L)
    part1(input).println()

    check(part2(testInput) == 13L)
    part2(input).println()

}

