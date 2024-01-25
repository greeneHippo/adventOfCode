package nl.groen

fun main() {

    fun part1 (input : List<String>): Long {

        val caloriesPerElf = input
            .fold(mutableListOf(mutableListOf<String>())) { acc, line ->
                if (line.isEmpty()) {
                    acc.add(mutableListOf<String>())
                } else {
                    acc.last().add(line)
                }
                acc
            }
            .map { it.sumOf { string -> string.toLong() } }

        return caloriesPerElf.max()
    }

    fun part2 (input : List<String>): Long {

        val caloriesPerElf = input
            .fold(mutableListOf(mutableListOf<String>())) { acc, line ->
                if (line.isEmpty()) {
                    acc.add(mutableListOf<String>())
                } else {
                    acc.last().add(line)
                }
                acc
            }
            .map { it.sumOf { string -> string.toLong() } }

        return caloriesPerElf.sorted().reversed().subList(0,3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000L)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()

}

