package nl.groen

data class Order (val from: Int, val to: Int, val number: Int)
fun main() {

    fun determineInitialString(input :List<String>, i: Int, indexWithStackIdentifiers: Int): String {

        val chars = input.subList(0, indexWithStackIdentifiers).map { it[4*i+1] }.reversed()
        return String(chars.toCharArray()).trim()
    }

    fun part1 (input : List<String>): String {

        val indexWithStackIdentifiers = input.indexOfFirst { it[1].toString() == "1" }
        val numberOfStacks = input.first { it[1].toString() == "1" }.trim().split( "   ").map { it.toInt() }.max()

        val stacks :MutableList<String> = buildList(numberOfStacks) {
            repeat(numberOfStacks) {add("")}
        }.toMutableList()

        stacks.forEachIndexed { i, s ->
            stacks[i] = determineInitialString(input, i, indexWithStackIdentifiers)
        }

        val orders = input.subList(indexWithStackIdentifiers+2, input.size ).map {
            val (_, number, from, to) = it.split("move ", "from ", "to ").map { it.trim() }
            Order(from.toInt()-1, to.toInt()-1, number.toInt())
        }

        orders.forEach {

            val move = stacks[it.from].takeLast(it.number)
            stacks[it.from] = stacks[it.from].dropLast(it.number)
            stacks[it.to] = stacks[it.to] + move.reversed()
        }

        val result = stacks.map { it.takeLast(1) }.joinToString(separator = "")
        return result
    }

    fun part2 (input : List<String>): String {

        val indexWithStackIdentifiers = input.indexOfFirst { it[1].toString() == "1" }
        val numberOfStacks = input.first { it[1].toString() == "1" }.trim().split( "   ").map { it.toInt() }.max()

        val stacks :MutableList<String> = buildList(numberOfStacks) {
            repeat(numberOfStacks) {add("")}
        }.toMutableList()

        stacks.forEachIndexed { i, s ->
            stacks[i] = determineInitialString(input, i, indexWithStackIdentifiers)
        }

        val orders = input.subList(indexWithStackIdentifiers+2, input.size ).map {
            val (_, number, from, to) = it.split("move ", "from ", "to ").map { it.trim() }
            Order(from.toInt()-1, to.toInt()-1, number.toInt())
        }

        orders.forEach {

            val move = stacks[it.from].takeLast(it.number)
            stacks[it.from] = stacks[it.from].dropLast(it.number)
            stacks[it.to] = stacks[it.to] + move
        }

        val result = stacks.map { it.takeLast(1) }.joinToString(separator = "")
        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    val input = readInput("Day05")

    check(part1(testInput) == "CMZ")
    part1(input).println()

    check(part2(testInput) == "MCD")
    part2(input).println()

}

