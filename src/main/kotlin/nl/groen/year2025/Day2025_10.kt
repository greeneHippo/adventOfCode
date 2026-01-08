package nl.groen.year2025

import nl.groen.println
import nl.groen.readInput
import java.util.*

fun main() {

    class IntArrayKey(private val data: IntArray) {
        private val hash = data.contentHashCode()

        override fun equals(other: Any?) =
            other is IntArrayKey && data.contentEquals(other.data)

        override fun hashCode() = hash
    }

    val cache = hashMapOf<IntArrayKey, Long>()

    fun List<Int>.pushButton(indexIndicatorsToUpdate: Set<Int>)
    : List<Int> = toMutableList().apply {
            indexIndicatorsToUpdate.forEach { index ->
                this[index] = (this[index] + 1) % 2
            }
        }

    fun IntArray.minusButton(indexIndicatorsToUpdate: Set<Int>)
    : IntArray {
        val intArrayToReturn = this.copyOf()
        indexIndicatorsToUpdate.forEach { index ->
            intArrayToReturn[index]--
        }
        return intArrayToReturn
    }

    data class State(val buttonsPressed: List<Int>, val statusIndicators: List<Int>)
    data class Machine(val indicatorGoalToReach : List<Int>, val buttons : List<Set<Int>>) {

        fun determineMinimumOfButtons() : Long {

            val cache = hashMapOf<List<Int>, List<Int>>()
            val queue = PriorityQueue { state1: State, state2: State -> state1.buttonsPressed.sum().compareTo(state2.buttonsPressed.sum()) }

            cache[List(indicatorGoalToReach.size) { 0 }] = List(buttons.size) { 0 }
            queue.add(State(List(buttons.size) { 0 }, List(indicatorGoalToReach.size) { 0 }))

            while(queue.isNotEmpty()) {
                val next = queue.poll()

                this.buttons.forEachIndexed { buttonIndex, button ->

                    // Check for part 1 that the buttons are not pushed more than once.
                    if (next.buttonsPressed[buttonIndex] == 1) {
                        return@forEachIndexed
                    }

                    val indicatorAfterPush = next.statusIndicators.pushButton(button)
                    if (this.indicatorGoalToReach == indicatorAfterPush) {
                        return next.buttonsPressed.sum().toLong() + 1L
                    }

                    if (cache[indicatorAfterPush] != null) {
                        return@forEachIndexed
                    }

                    val updateButtons = next.buttonsPressed.toMutableList()
                    updateButtons[buttonIndex]++
                    cache[indicatorAfterPush] = updateButtons

                    queue.add(State(updateButtons, indicatorAfterPush))
                }
            }

            throw IllegalStateException()
        }

        fun determineMinimumOfButtonsRecursive(indicatorGoalToReach: IntArray): Long {

            return cache.getOrPut(IntArrayKey(indicatorGoalToReach)) {

                if (indicatorGoalToReach.any { it < 0 }) {
                    return@getOrPut 1000000L
                }

                val minimumEachButton = this.buttons.map { button ->
                    determineMinimumOfButtonsRecursive(indicatorGoalToReach.minusButton(button))
                }

                return@getOrPut minimumEachButton.min() + 1
            }
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
            it.determineMinimumOfButtons()
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
            cache.clear()
            cache[IntArrayKey(IntArray(it.indicatorGoalToReach.size) { 0 })] = 0
            val value = it.determineMinimumOfButtonsRecursive(it.indicatorGoalToReach.toIntArray())
            return@map value
        }

        return buttonsToPress.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2025","Day10_test")
    val input = readInput("2025","Day10")

    check(part1(testInput) == 7L)
    part1(input).println()

    check(part2(testInput) == 33L)
    part2(input).println()

}

