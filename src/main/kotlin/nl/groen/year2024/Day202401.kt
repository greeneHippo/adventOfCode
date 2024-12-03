package nl.groen.year2024

import nl.groen.println
import nl.groen.readInput
import kotlin.math.abs

fun main() {

    fun part1 (input : List<String>): Long {

        val left : MutableList<Int> = mutableListOf()
        val right : MutableList<Int> = mutableListOf()
        val ints = input.map { it.split("   ").map { it.toInt() }}.forEach {
            left.add(it[0])
            right.add(it[1])
        }
        left.sortDescending()
        right.sortDescending()

        return left.mapIndexed { index, int -> abs(int - right[index])  }.sum().toLong()
    }

    fun part2 (input : List<String>): Long {

        val left : MutableList<Int> = mutableListOf()
        val right : MutableList<Int> = mutableListOf()
        val ints = input.map { it.split("   ").map { it.toInt() }}.forEach {
            left.add(it[0])
            right.add(it[1])
        }

        return left.mapIndexed { index, int -> (int * right.count { it == int })  }.sum().toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day01_test")
    val input = readInput("2024","Day01")

    check(part1(testInput) == 11L)
    part1(input).println()

    check(part2(testInput) == 31L)
    part2(input).println()

}

