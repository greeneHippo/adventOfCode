package nl.groen.year2025

import nl.groen.*

enum class Operation(var symbol: String) {
    SUM("+"),
    PRODUCT("*");

    override fun toString() :String {
        return this.symbol
    }
}
fun main() {

    fun calculateProblems(
        operations: List<Operation?>,
        numbers: List<List<Long>>
    ): List<Long> {
        val results = operations.mapIndexed { index, operation ->
            return@mapIndexed if (Operation.SUM == operation) {
                numbers[index].reduce { acc, lng -> acc + lng }
            } else {
                numbers[index].reduce { acc, lng -> acc * lng }
            }
        }
        return results
    }

    fun part1 (input : List<String>): Long {

        val rowsWithNumbers = input.size - 1
        val numbers = input.subList(0, rowsWithNumbers)
            .map { string -> string
                .split(Regex("\\s+"))
                .filter { !it.isEmpty() }
                .map { it.toLong() }
            }
        val numbersTransposed = transpose(numbers)
        val operations = input[rowsWithNumbers].split(Regex("\\s+")).filter { !it.isEmpty() }.map { Operation.entries.firstOrNull { part -> it == part.symbol }}

        val results = calculateProblems(operations, numbersTransposed)

        return results.sum()
    }

    fun part2 (input : List<String>): Long {

        val transposedStrings = groupStringsOnEmptyLine(input.transposeListString())

        printList(transposedStrings)

        val operations = transposedStrings
            .map { string -> string.first().last()}
            .map { Operation.entries.firstOrNull { part -> it.toString() == part.symbol }}
        val numbers = transposedStrings.map {
            it.map {
                number -> number.replace("*", "").replace("+", "").trim().toLong()}
        }

        val results = calculateProblems(operations, numbers)

        return results.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2025","Day06_test")
    val input = readInput("2025","Day06")

    check(part1(testInput) == 4277556L)
    part1(input).println()

    check(part2(testInput) == 3263827L)
    part2(input).println()

}

