package nl.groen.year2025

import nl.groen.println
import nl.groen.readInput
import java.util.*

fun main() {

    fun List<Int>.pushButton(indexIndicatorsToUpdate: Set<Int>): List<Int> {

        val updatedList = this.toMutableList()
        indexIndicatorsToUpdate.forEach { light ->
            updatedList[light] = (updatedList[light] + 1) % 2
        }
        return updatedList
    }

    data class State(val buttonsPressed: List<Int>, val statusIndicators: List<Int>)
    data class Machine(val indicatorLights : List<Int>, val buttons : List<Set<Int>>) {

        fun determineMinimumOfButtons(limitEachButtonToOne: Boolean) : Long {

            val cache = hashMapOf<List<Int>, List<Int>>()
            val queue = PriorityQueue { state1: State, state2: State -> state1.buttonsPressed.sum().compareTo(state2.buttonsPressed.sum()) }

            cache[List(buttons.size) { 0 }] = List(indicatorLights.size) { 0 }
            queue.add(State(List(buttons.size) { 0 }, List(indicatorLights.size) { 0 }))

            while(queue.isNotEmpty()) {
                val next = queue.poll()

                this.buttons.forEachIndexed { buttonIndex, button ->

                    if (limitEachButtonToOne && next.buttonsPressed[buttonIndex] == 1) {
                        return@forEachIndexed
                    }

                    val updateButtons = next.buttonsPressed.toMutableList()
                    updateButtons[buttonIndex]++

                    if (cache[updateButtons] != null) {
                        return@forEachIndexed
                    }

                    val indicatorAfterPush = next.statusIndicators.pushButton(button)

                    if (this.indicatorLights == indicatorAfterPush) {
                        return updateButtons.sum().toLong()
                    }

                    cache[updateButtons] = indicatorAfterPush
                    queue.add(State(updateButtons, indicatorAfterPush))
                }
            }

            throw IllegalStateException()
        }
    }

    fun part1 (input : List<String>): Long {

        val machines = input
            .map { line -> line.split(" ") }.toMutableList()
            .map { lineSplit ->
                Machine(
                    lineSplit[0]
                        .replace("[","")
                        .replace("]","")
                        .map { character -> if (character == '#') 1 else 0 },
                    lineSplit
                        .subList(1, lineSplit.size-1)
                        .map{string ->
                            string
                                .replace("(","").replace(")","")
                                .split(",")
                                .map{it.toInt()}
                                .toSet()
                })}

        val buttonsToPress = machines.map {
            it.determineMinimumOfButtons(true)
        }

        return buttonsToPress.sum()
    }

    fun part2 (input : List<String>): Long {
        val machines = input
            .map { line -> line.split(" ") }.toMutableList()
            .map { lineSplit ->
                Machine(
                    lineSplit.last()
                        .replace("{","")
                        .replace("}","")
                        .split(",")
                        .map { character -> character.toInt() },
                    lineSplit
                        .subList(1, lineSplit.size-1)
                        .map{string ->
                            string
                                .replace("(","").replace(")","")
                                .split(",")
                                .map{it.toInt()}
                                .toSet()
                        })}

        val buttonsToPress = machines.map {
            println(it)
            it.determineMinimumOfButtons(false)
        }

        return buttonsToPress.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2025","Day10_test")
    val input = readInput("2025","Day10")

    check(part1(testInput) == 7L)
    part1(input).println()

    check(part2(testInput) == 33L)
//    part2(input).println()

}

