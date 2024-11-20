package nl.groen

fun main() {

    data class Monkey(val name : Int, var items : MutableList<Long>, val toMonkeyWhenTrue : Int, val toMonkeyWhenFalse : Int, val transform: (Long) -> Long, val test: (Long) -> Boolean)
    data class Item(val monkey : Int, val worryLevel : Long)

    fun determineTransformFunction(operation: String): (Long) -> Long {

        val value = operation.split(" ")[2]
        if (operation.contains("+")) {
            return if (Regex("\\d*").matches(value) ) {
                {it + value.toLong()}
            } else {
                {it + it}
            }
        } else if (operation.contains("*")) {
            if (Regex("\\d*").matches(value) ) {
                val product: (Long) -> Long = { x: Long -> Math.multiplyExact(x, value.toLong())}
                return product
            } else {
                val product: (Long) -> Long = {Math.multiplyExact(it, it)}
                return product
            }

        }
        return {it}
    }

    fun part1 (input: List<String>, iterations: Int): Long {

        val groupStrings : MutableList<MutableList<String>> = mutableListOf(mutableListOf())
        input.fold(groupStrings) { acc, s ->
            if (s.isEmpty()) {
                acc.add(mutableListOf())
            } else {
                acc.last().add(s)
            }
            acc
        }
        val monkeys : MutableList<Monkey> = groupStrings.map {
            val key = it[0].split(" ", ":")[1].toInt()
            val items = it[1].split("Starting items: ", ", ").drop(1).map { it.toLong() }.toMutableList()
            val operation = it[2].split("Operation: new = ", ", ").drop(1).first()
            var transformerFunction: ((Long) -> Long) = determineTransformFunction(operation)
            val division = it[3].split("Test: divisible by ").drop(1)[0].toLong()
            val toMonkeyWhenTrue = Regex("\\d").find(it[4])!!.value.toInt()
            val toMonkeyWhenFalse = Regex("\\d").find(it[5])!!.value.toInt()
            val testFunction: (Long) -> Boolean = { it % division == 0L}
            val monkey = Monkey(key, items, toMonkeyWhenTrue, toMonkeyWhenFalse, transform = transformerFunction, test = testFunction)
             monkey
        }.toMutableList()

        val inspections = monkeys.map { 0 }.toMutableList()
        val items = monkeys.flatMap { it.items.map { item -> Item(it.name, item) } }

        val result = items.map {
            var thrownItem = it
            //println("Monkey: ${thrownItem.monkey} = worryLevel: ${thrownItem.worryLevel}")
            IntRange(1,iterations).forEach {round ->
                IntRange(0,monkeys.size).forEach {monkeyIndex ->
                    if (thrownItem.monkey == monkeyIndex) {
                        inspections[monkeyIndex]++
                        val monkey = monkeys[monkeyIndex]
                        val worryLevel = thrownItem.worryLevel
                        val newWorryLevel = monkey.transform.invoke(worryLevel) / 3
                        val newMonkeyIndex = if (monkey.test.invoke(newWorryLevel)) monkey.toMonkeyWhenTrue else {monkey.toMonkeyWhenFalse}
                        thrownItem = Item(newMonkeyIndex, newWorryLevel)
                        //println("Round ${round} - Monkey: ${thrownItem.monkey} = worryLevel: ${thrownItem.worryLevel}")
                    }
                }
            }

            return@map thrownItem
        }

        IntRange(0,monkeys.size).forEach { monkeyIndex ->
            println("Monkey $monkeyIndex: " + result.filter { it.monkey == monkeyIndex }.map{it.worryLevel}.joinToString())
        }

        inspections.sortDescending()

        return inspections.filterIndexed { index, _ -> index < 2 }.reduce { acc, i -> acc * i }.toLong()
    }

    fun part2 (input : List<String>): Long {

        return input.size.toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    val input = readInput("Day11")

    check(part1(testInput, 20) == 10605L)
    part1(input, 20).println()

    check(part1(testInput, 10000) == 2713310158L)
    part2(input).println()

}
