package nl.groen

data class Assignment(val low :Int, val high :Int)
fun main() {

    fun part1 (input : List<String>): Long {

        val cleaningPairs = input.map {
            val (first, second, third, fourth) = it.split(",", "-").map { s -> s.toInt() }
            Pair(Assignment(first, second), Assignment(third, fourth))
        }

        val overlapInAssignments = cleaningPairs.filter {
            (it.first.low >= it.second.low && it.first.high <= it.second.high) ||
            (it.second.low >= it.first.low && it.second.high <= it.first.high)
        }

        return overlapInAssignments.size.toLong()
    }

    fun part2 (input : List<String>): Long {

        val cleaningPairs = input.map {
            val (first, second, third, fourth) = it.split(",", "-").map { s -> s.toInt() }
            Pair(Assignment(first, second), Assignment(third, fourth))
        }

        val overlapInAssignments = cleaningPairs.filter {
            (it.first.low <= it.second.low && it.first.high >= it.second.low) ||
            (it.second.low <= it.first.low && it.second.high >= it.first.low)
        }

        return overlapInAssignments.size.toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    val input = readInput("Day04")

    check(part1(testInput) == 2L)
    part1(input).println()

    check(part2(testInput) == 4L)
    part2(input).println()

}

