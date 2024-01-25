package nl.groen

fun main() {

    fun part1 (input : List<String>): Long {

        val rucksacks = input.map { Pair(it.substring(0, it.length /2), it.substring(it.length /2, it.length)) }

        val characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val doubleCharacters = rucksacks.map {
            val character = characters.first { c -> it.first.contains(c) && it.second.contains(c) }
            characters.indexOf(character) + 1L
        }
        return doubleCharacters.sum()
    }

    fun part2 (input : List<String>): Long {

        val rucksacks : MutableList<MutableList<String>> = mutableListOf()
        input.foldIndexed(rucksacks) {index, acc, s ->

            if (index % 3 == 0) {
                acc.add(mutableListOf())
                acc.last().add(s)
            } else {
                acc.last().add(s)
            }
            acc
        }

        val characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val badges = rucksacks.map {
            val character = characters.first { c -> it[0].contains(c) && it[1].contains(c) && it[2].contains(c) }
            characters.indexOf(character) + 1L
        }
        return badges.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    val input = readInput("Day03")

    check(part1(testInput) == 157L)
    part1(input).println()

    check(part2(testInput) == 70L)
    part2(input).println()

}

