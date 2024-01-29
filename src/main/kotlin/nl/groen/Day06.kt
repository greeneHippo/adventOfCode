package nl.groen

fun main() {

    fun part1 (input : List<String>): Long {

        var i = 0
        do {
            if (i < 3) {
                i++
                continue
            }

            val characters = input[0].substring(i-3, i+1).map { it.toString() }.toSet()

            if (characters.size == 4) {
                return i + 1L
            }
            i++
        } while (true)

    }

    fun part2 (input : List<String>): Long {

        var i = 0
        do {
            if (i < 13) {
                i++
                continue
            }

            val characters = input[0].substring(i-13, i+1).map { it.toString() }.toSet()

            if (characters.size == 14) {
                return i + 1L
            }
            i++
        } while (true)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    val input = readInput("Day06")

    check(part1(testInput) == 7L)
    part1(input).println()

    check(part2(testInput) == 19L)
    part2(input).println()

}

