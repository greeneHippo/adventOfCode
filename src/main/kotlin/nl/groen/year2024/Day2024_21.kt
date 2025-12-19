package nl.groen.year2024

import nl.groen.println
import nl.groen.readInput
import java.util.*

enum class Button (val symbol: String) {
    ZERO("0"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    A("A"),
    UP("^"),
    RIGHT(">"),
    DOWN("v"),
    LEFT("<"),
    A_("a");
    companion object {
        fun getBySymbol(input: String): Button {
            return Button.entries.firstOrNull { part -> input == part.symbol }!!
        }
    }

}

val transitions = mutableMapOf(
    Pair(Button.ZERO, listOf(
        Pair(Button.A, Button.RIGHT),
        Pair(Button.TWO, Button.UP),
    )),
    Pair(Button.A, listOf(
        Pair(Button.ZERO, Button.LEFT),
        Pair(Button.THREE, Button.UP),
    )),
    Pair(Button.ONE, listOf(
        Pair(Button.FOUR, Button.UP),
        Pair(Button.TWO, Button.RIGHT),
    )),
    Pair(Button.TWO, listOf(
        Pair(Button.ONE, Button.LEFT),
        Pair(Button.FIVE, Button.UP),
        Pair(Button.THREE, Button.RIGHT),
        Pair(Button.ZERO, Button.DOWN),
    )),
    Pair(Button.THREE, listOf(
        Pair(Button.A, Button.DOWN),
        Pair(Button.TWO, Button.LEFT),
        Pair(Button.SIX, Button.UP),
    )),
    Pair(Button.FOUR, listOf(
        Pair(Button.SEVEN, Button.UP),
        Pair(Button.FIVE, Button.RIGHT),
        Pair(Button.ONE, Button.DOWN),
    )),
    Pair(Button.FIVE, listOf(
        Pair(Button.FOUR, Button.LEFT),
        Pair(Button.EIGHT, Button.UP),
        Pair(Button.SIX, Button.RIGHT),
        Pair(Button.TWO, Button.DOWN),
    )),
    Pair(Button.SIX, listOf(
        Pair(Button.THREE, Button.DOWN),
        Pair(Button.FIVE, Button.LEFT),
        Pair(Button.NINE, Button.UP),
    )),
    Pair(Button.SEVEN, listOf(
        Pair(Button.EIGHT, Button.RIGHT),
        Pair(Button.FOUR, Button.DOWN),
    )),
    Pair(Button.EIGHT, listOf(
        Pair(Button.SEVEN, Button.LEFT),
        Pair(Button.FIVE, Button.DOWN),
        Pair(Button.NINE, Button.RIGHT),
    )),
    Pair(Button.NINE, listOf(
        Pair(Button.SIX, Button.DOWN),
        Pair(Button.EIGHT, Button.LEFT),
    )),
    Pair(Button.LEFT, listOf(
        Pair(Button.DOWN, Button.RIGHT),
    )),
    Pair(Button.DOWN, listOf(
        Pair(Button.LEFT, Button.LEFT),
        Pair(Button.UP, Button.UP),
        Pair(Button.RIGHT, Button.RIGHT),
    )),
    Pair(Button.UP, listOf(
        Pair(Button.A_, Button.RIGHT),
        Pair(Button.DOWN, Button.DOWN),
    )),
    Pair(Button.RIGHT, listOf(
        Pair(Button.A_, Button.UP),
        Pair(Button.DOWN, Button.LEFT),
    )),
    Pair(Button.A_, listOf(
        Pair(Button.UP, Button.LEFT),
        Pair(Button.RIGHT, Button.DOWN),
    ))

)

fun main() {
    data class Transition(val startString: String, val finishString: String)
//    data class State(val currentKeyPad: Button, val buttons :List<Button>, val transitions :List<Transition>) {
//        fun compareTo(s2: State) : Int {
//            val result = this.transitions.size.compareTo(s2.transitions.size)
//            if (result != 0) return result
//
//            val s1Fold = this.transitions.map { it.finishString }.reduce(operation = { a, b ->
//                if (a.takeLast(1) == b) {
//                    a
//                } else {
//                    a.plus(b)
//                }
//            })
//            val s2Fold = s2.transitions.map { it.finishString }.reduce(operation = { a, b ->
//                if (a.takeLast(1) == b) {
//                    a
//                } else {
//                    a.plus(b)
//                }
//            })
//            return s1Fold.length.compareTo(s2Fold.length)
//        }
//    }

        data class State(val currentKeyPad: Button, val buttons :List<Button>, val transitions :List<String>) {
        fun compareTo(s2: State) : Int {
            val result = this.transitions.size.compareTo(s2.transitions.size)
            if (result != 0) return result

            val s1Fold = this.transitions.reduce(operation = { a, b ->
                if (a.takeLast(1) == b) {
                    a
                } else {
                    a.plus(b)
                }
            })
            val s2Fold = s2.transitions.reduce(operation = { a, b ->
                if (a.takeLast(1) == b) {
                    a
                } else {
                    a.plus(b)
                }
            })
            return s1Fold.length.compareTo(s2Fold.length)
        }
    }

//    fun Transition.translateInstructionNumericKeyPad() : List<Transition> {
//
//        val queue = PriorityQueue { s1 : State, s2 : State -> s1.compareTo(s2)}
//        queue.add(State(Button.getBySymbol(this.startString), listOf(Button.getBySymbol(this.startString)), listOf()))
//        val finalDestination = Button.getBySymbol(this.finishString)
//
//        while (!queue.isEmpty()) {
//
//            val next = queue.poll()
//            if (next.currentKeyPad == finalDestination) {
//
//                val initialString =
//                    if (next.transitions.isEmpty()) {
//                        finalDestination.symbol
//                    } else {
//                        next.transitions.last().finishString
//                    }
//                return next.transitions.plus(Transition(initialString, "a"))
//            }
//
//            val buttons = transitions[next.currentKeyPad]!!
//            buttons.forEach { button ->
//                if (next.buttons.any { it == button.first}) {
//                    return@forEach
//                }
//
//                if (next.transitions.isEmpty()) {
//                    queue.add(State(button.first, next.buttons.plus(button.first), next.transitions.plus(Transition("a", button.second.symbol))))
//                } else {
//                    queue.add(State(button.first, next.buttons.plus(button.first), next.transitions.plus(Transition(next.transitions.last().finishString, button.second.symbol))))
//                }
//
//            }
//        }
//
//        error("route not found")
//    }

    fun Transition.translateInstructionNumericKeyPad() : List<String> {

        val queue = PriorityQueue { s1 : State, s2 : State -> s1.compareTo(s2)}
        queue.add(State(Button.getBySymbol(this.startString), listOf(Button.getBySymbol(this.startString)), listOf()))
        val finalDestination = Button.getBySymbol(this.finishString)

        while (!queue.isEmpty()) {

            val next = queue.poll()
            if (next.currentKeyPad == finalDestination) {
                return next.transitions.sorted().plus( "a")
            }

            val buttons = transitions[next.currentKeyPad]!!
            buttons.forEach { button ->
                if (next.buttons.any { it == button.first}) {
                    return@forEach
                }

                queue.add(State(button.first, next.buttons.plus(button.first), next.transitions.plus(button.second.symbol)))

            }
        }

        error("route not found")
    }

    fun part1 (input : List<String>): Long {

        val instructionsRobotOne = input.map { "A$it" }.map { it.toList().windowed(2,1, false) { slice -> Transition(slice[0].toString(), slice[1].toString())}}
        val instructionsRobotTwo = instructionsRobotOne.map { list -> list.flatMap { transition -> transition.translateInstructionNumericKeyPad() }}
        val transitionsRobotTwo = instructionsRobotTwo.map { listOf("a").plus(it) }.map {it.windowed(2,1, false) { slice -> Transition(slice[0], slice[1])}}
        val instructionsRobotThree = transitionsRobotTwo.map { list -> list.flatMap { transition -> transition.translateInstructionNumericKeyPad() }}
        val transitionsRobotThree = instructionsRobotThree.map { listOf("a").plus(it) }.map {it.windowed(2,1, false) { slice -> Transition(slice[0], slice[1])}}
        val instructionsUser = transitionsRobotThree.map { list -> list.flatMap { transition -> transition.translateInstructionNumericKeyPad() }.joinToString (separator = "") { it }}

        return instructionsUser.mapIndexed { index, string -> string.length * input[index].take(3).toLong() }.sum()
    }

    fun part2 (input : List<String>): Long {

        return input.size.toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day21_test")
    val input = readInput("2024","Day21")

    check(part1(testInput) == 126384L)
    part1(input).println()

    check(part2(testInput) == 1L)
    part2(input).println()

}

