package nl.groen

import kotlin.math.abs

fun main() {

    data class Instruction (val finishesOnCycle: Int, val newValue: Int )
    fun part1 (input : List<String>): Long {

        val relevantCycles = IntRange(0, 5).map { 20 + it * 40 }

        val instructions : MutableList<Instruction> = mutableListOf(Instruction(0, 1))
        val instructionSimulation = input.fold(instructions) {acc, s ->
            val previousInstruction = acc.last()
            if (s == "noop") {
                acc.add(Instruction(previousInstruction.finishesOnCycle+1, previousInstruction.newValue))
            } else {
                val (_, value) = s.split(" ")
                acc.add(Instruction(previousInstruction.finishesOnCycle+2, previousInstruction.newValue+value.toInt()))
            }

            acc
        }

        return relevantCycles.map {cycle ->
            val value = instructionSimulation.filter { it.finishesOnCycle < cycle }.last().newValue
            println("$cycle -> $value")
            cycle * value
        }.sum().toLong()

    }

    fun part2 (input : List<String>): Long {

        val relevantCycles = IntRange(0, 5).map { it * 40 }

        val instructions : MutableList<Instruction> = mutableListOf(Instruction(0, 1))
        val instructionSimulation = input.fold(instructions) {acc, s ->
            val previousInstruction = acc.last()
            if (s == "noop") {
                acc.add(Instruction(previousInstruction.finishesOnCycle+1, previousInstruction.newValue))
            } else {
                val (_, value) = s.split(" ")
                acc.add(Instruction(previousInstruction.finishesOnCycle+2, previousInstruction.newValue+value.toInt()))
            }

            acc
        }

        val symbols = IntRange(1, 240).map {cycle ->
            val value = instructionSimulation.filter { it.finishesOnCycle < cycle }.last().newValue
            val symbol = if (abs(cycle%40-value-1) <= 1 ) {
                "#"
            } else {
                "."
            }

            symbol
        }

        relevantCycles.map { symbols.subList(it, it+40) }.forEach { it.joinToString("").println() }

        return 1L
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    val input = readInput("Day10")

    check(part1(testInput) == 13140L)
    part1(input).println()

//    part2(testInput)
    part2(input).println()

}

