package nl.groen.year2024

import nl.groen.println
import nl.groen.readInput

fun main() {

    fun List<Int>.check(shouldBeBeforePages: Map<Int, List<Int>>, shouldBeAfterPages: Map<Int, List<Int>>) : Boolean {

        this.forEachIndexed { index, page ->

            val listPagesNotBefore = shouldBeBeforePages[page]
            val numbersBeforePage = this.take(index)
            if (listPagesNotBefore != null && listPagesNotBefore.any {  numbersBeforePage.contains(it) }) {
                return false
            }
        }

        return true
    }

    fun List<Int>.swap(shouldBeBeforePages: Map<Int, List<Int>>, shouldBeAfterPages: Map<Int, List<Int>>) : List<Int> {

        this.forEachIndexed { index, page ->

            val listPagesNotBefore = shouldBeBeforePages[page]
            val numbersBeforePage = this.take(index)
            if (listPagesNotBefore != null && listPagesNotBefore.any {  numbersBeforePage.contains(it) }) {
                val otherPage = listPagesNotBefore.first{numbersBeforePage.contains(it)}
                val newList = this.toMutableList()
                newList[this.indexOf(otherPage)] = page
                newList[index] = otherPage
                return newList
            }
        }

        return this
    }

    fun List<Int>.reorder(shouldBeBeforePages: Map<Int, List<Int>>, shouldBeAfterPages: Map<Int, List<Int>>) : List<Int> {

        var tempList = this
        while (!tempList.check(shouldBeBeforePages, shouldBeAfterPages)) {
            tempList = tempList.swap(shouldBeBeforePages, shouldBeAfterPages)
        }

        return tempList
    }

    fun init(input: List<String>): Triple<List<List<Int>>, Map<Int, List<Int>>, Map<Int, List<Int>>> {
        val parsedInput: MutableList<MutableList<String>> = mutableListOf(mutableListOf())
        input.fold(parsedInput) { acc, s ->
            if (s.isEmpty()) {
                acc.add(mutableListOf())
            } else {
                acc.last().add(s)
            }
            acc
        }
        val rules = parsedInput[0].map { it.split("|") }.map { Pair(it[0].toInt(), it[1].toInt()) }
        val updates = parsedInput[1].map { it.split(",").map { it.toInt() } }

        val shouldBeBeforePages: Map<Int, List<Int>> = rules.map { it.first }
            .associateWith { int -> rules.filter { rule -> rule.first == int }.map { it.second } }
        val shouldBeAfterPages: Map<Int, List<Int>> = rules.map { it.second }
            .associateWith { int -> rules.filter { rule -> rule.second == int }.map { it.first } }
        return Triple(updates, shouldBeBeforePages, shouldBeAfterPages)
    }

    fun part1 (input : List<String>): Long {

        val (updates, shouldBeBeforePages: Map<Int, List<Int>>, shouldBeAfterPages: Map<Int, List<Int>>) = init(input)
        val correctUpdates = updates.filter { it.check(shouldBeBeforePages, shouldBeAfterPages) }

        return correctUpdates.map{it[(it.size-1)/2]}.sum().toLong()
    }

    fun part2 (input : List<String>): Long {

        val (updates, shouldBeBeforePages: Map<Int, List<Int>>, shouldBeAfterPages: Map<Int, List<Int>>) = init(input)
        val incorrectUpdates = updates.filter { !it.check(shouldBeBeforePages, shouldBeAfterPages) }
        val sortedIncorrectUpdates = incorrectUpdates.map { it.reorder(shouldBeBeforePages, shouldBeAfterPages) }

        return sortedIncorrectUpdates.map{it[(it.size-1)/2]}.sum().toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day05_test")
    val input = readInput("2024","Day05")

    check(part1(testInput) == 143L)
    part1(input).println()

    check(part2(testInput) == 123L)
    part2(input).println()

}

