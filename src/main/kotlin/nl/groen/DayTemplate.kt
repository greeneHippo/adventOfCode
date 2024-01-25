package nl.groen

fun main() {

    fun part1 (input : List<String>): Long {

        return input.size.toLong()
    }

    fun part2 (input : List<String>): Long {

        return input.size.toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 1L)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()

}

