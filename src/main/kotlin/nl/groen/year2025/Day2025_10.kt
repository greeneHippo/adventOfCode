package nl.groen.year2025

import nl.groen.power
import nl.groen.println
import nl.groen.readInput
import java.util.PriorityQueue

fun main() {

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

                    // Check that the buttons are not pushed more than once.
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

    val cacheCombinationOfButtonsToReachGoal = hashMapOf<String, List<List<Int>>>()

    fun determineAllCombinationsOfButtonsForParity(indicatorGoalToReach: List<Int>, buttons: List<Set<Int>>): List<List<Int>> {

        return cacheCombinationOfButtonsToReachGoal.getOrPut(indicatorGoalToReach.joinToString (",")) {

            val localCache = hashSetOf<List<Int>>()
            val queue : ArrayDeque<State> = ArrayDeque()
            val combinations = mutableSetOf<List<Int>>()

            localCache.add(List(buttons.size) { 0 })
            queue.add(State(List(buttons.size) { 0 }, List(indicatorGoalToReach.size) { 0 }))

            while (queue.isNotEmpty()) {
                val next = queue.removeFirst()

                buttons.forEachIndexed { buttonIndex, button ->

                    // Check that the buttons are not pushed more than once.
                    if (next.buttonsPressed[buttonIndex] == 1) {
                        return@forEachIndexed
                    }

                    val indicatorAfterPush = next.statusIndicators.pushButton(button)
                    val updateButtons = next.buttonsPressed.toMutableList()
                    updateButtons[buttonIndex]++

                    if (indicatorGoalToReach == indicatorAfterPush) {
                        combinations.add(updateButtons)
                    }

                    if (localCache.contains(updateButtons)) {
                        return@forEachIndexed
                    }

                    localCache.add(updateButtons)
                    queue.add(State(updateButtons, indicatorAfterPush))
                }
            }

            return@getOrPut combinations.toList()
        }
    }

    val cacheMinimumButtonsToReachGoal = hashMapOf<String, Long>()

    fun determineMinimumOfButtonsPart2(indicatorGoalToReach: List<Int>, buttons: List<Set<Int>>): Long {

        return cacheMinimumButtonsToReachGoal.getOrPut(indicatorGoalToReach.joinToString (",")) {
            val indicatorsUneven = indicatorGoalToReach.map { it % 2 }

            val combinations = determineAllCombinationsOfButtonsForParity(indicatorsUneven, buttons)

            val steps = combinations.map { buttonIds ->

                val indicatorLeftOver = indicatorGoalToReach.pushButtons(buttonIds, buttons)

                if (indicatorLeftOver.any { it < 0 }) {
                    return@map listOf(10000000L)
                }
                if (indicatorLeftOver.all { it == 0 }) {
                    return@map listOf(buttonIds.sum().toLong())
                }

                val results = mutableListOf<Long>()
                var isEven = true
                var divided = indicatorLeftOver
                var tries = 0L
                while (isEven) {
                    divided = divideByTwo(divided)
                    tries++
                    results.add((power(2L, tries) * determineMinimumOfButtonsPart2(divided, buttons)) + buttonIds.sum().toLong())
                    isEven = divided.all { it % 2 == 0 }
                }

                return@map results
            }.flatten()

            return@getOrPut if (steps.isEmpty()) 10000000L else steps.min()
        }
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
                        }
                )}

        val buttonsToPress = machines.map {
            println(it)
            cacheCombinationOfButtonsToReachGoal.clear()
            cacheMinimumButtonsToReachGoal.clear()
            val (divided, tries) = determineDivisionByTwoToReachUneven(it.indicatorGoalToReach)
            val value = power(2L, tries) * determineMinimumOfButtonsPart2(divided, it.buttons)
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

private fun List<Int>.pushButtons(buttonSequence: List<Int>, buttons: List<Set<Int>>) : List<Int>
    = toMutableList().apply {
    buttonSequence.forEachIndexed { index, toPush ->
        if (toPush == 0) {
            return@forEachIndexed
        }
        buttons[index].forEach { indexIndicator ->
            this[indexIndicator]--
        }
    }
}

private fun List<Int>.pushButton(indexIndicatorsToUpdate: Set<Int>)
        : List<Int> = toMutableList().apply {
    indexIndicatorsToUpdate.forEach { index ->
        this[index] = (this[index] + 1) % 2
    }
}

private fun determineDivisionByTwoToReachUneven(indicatorLeftOver: List<Int>): Pair<List<Int>, Long> {
    var divided = indicatorLeftOver
    var tries = 0L
    while (divided.all { it % 2 == 0 }) {
        divided = divided.map { it / 2 }
        tries++
    }
    return Pair(divided, tries)
}

private fun divideByTwo(indicatorLeftOver: List<Int>): List<Int> {
    return indicatorLeftOver.map { it / 2 }
}
