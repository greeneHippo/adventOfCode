package nl.groen.year2024

import nl.groen.println
import nl.groen.readInput

fun main() {

    fun part1 (input : List<String>): Long {

        val pairs = input.map { it.split("-") }.flatMap { listOf(Pair(it[0], it[1]), Pair(it[1], it[0])) }

        val pairMaps = pairs.groupBy { it.first }
        val result :MutableList<Triple<String, String, String>> = mutableListOf()
        pairMaps.forEach {

            val listPairs = it.value.toList()
            listPairs.forEach { pair ->
                listPairs.forEach { pair2 ->
                    if (pair == pair2) {
                        return@forEach
                    }
                    if (pairs.any { secondPair -> secondPair.first == pair.second && secondPair.second == pair2.second }) {
                        val connected = listOf(it.key, pair.second, pair2.second).sorted()
                        val triple = Triple(connected[0], connected[1], connected[2])
                        if (connected.any {key -> key.startsWith("t")} && !result.contains(triple)) {
//                            println("New pair detected ${it.key} ${pair.second} ${pair2.second }")
                            result.add(triple)
                        }
                    }
                }
            }
        }

        return result.size.toLong()
    }

    fun findNextTriple(
        map: MutableMap<Triple<String, String, String>, Int?>,
        index: Int
    ): Boolean {
        map.filter { it.value == index }.forEach {
            val nextTriples = map.filter{entry -> entry.value == null}.filter{ entry ->
                val list = listOf(entry.key.first, entry.key.second, entry.key.third)
                return@filter ((list.contains(it.key.first) && list.contains(it.key.second) && !list.contains(it.key.third)) ||
                    (list.contains(it.key.first) && !list.contains(it.key.second) && list.contains(it.key.third)) ||
                    (!list.contains(it.key.first) && list.contains(it.key.second) && list.contains(it.key.third)))
            }

            if (nextTriples.isNotEmpty()) {
                map[nextTriples.entries.first().key] = index
                return true
            }
        }

        return false
    }

    fun part2 (input : List<String>): String {

        val pairs = input.map { it.split("-") }.flatMap { listOf(Pair(it[0], it[1]), Pair(it[1], it[0])) }

        val pairMaps = pairs.groupBy { it.first }
        val result :MutableList<Triple<String, String, String>> = mutableListOf()
        pairMaps.forEach {

            val listPairs = it.value.toList()
            listPairs.forEach { pair ->
                listPairs.forEach { pair2 ->
                    if (pair == pair2) {
                        return@forEach
                    }
                    if (pairs.any { secondPair -> secondPair.first == pair.second && secondPair.second == pair2.second }) {
                        val connected = listOf(it.key, pair.second, pair2.second).sorted()
                        val triple = Triple(connected[0], connected[1], connected[2])
                        if (!result.contains(triple)) {
//                            println("New pair detected ${it.key} ${pair.second} ${pair2.second }")
                            result.add(triple)
                        }
                    }
                }
            }
        }

        val map :MutableMap<Triple<String, String, String>, Int?> = result.associateWith { null }.toMutableMap()
        var index = 0
        while (map.values.any { it == null }) {
            val triple = map.filter { it.value == null }.entries.first()
            map[triple.key] = index
            var searchCompleted = false
            while (!searchCompleted) {
                searchCompleted = !findNextTriple(map, index)
            }

            index++
            continue

        }

        val indexLargestGroup = map.values.groupBy { it }.entries.maxByOrNull { it.value.size }!!.key!!


        val mostConnectedPairs = map.filter { it.value == indexLargestGroup  }
            .flatMap { listOf(it.key.first, it.key.second, it.key.third) }.toSet()
            .sorted()


        return mostConnectedPairs.joinToString(separator = ",") { it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day23_test")
    val input = readInput("2024","Day23")

    check(part1(testInput) == 7L)
    part1(input).println()

    check(part2(testInput) == "co,de,ka,ta")
    part2(input).println()

}

