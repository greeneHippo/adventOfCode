package nl.groen.year2024

import nl.groen.*

fun main() {

    data class LocationProperties (val symbol: String, var hasAntinode: Boolean = false);

    fun part1 (input : List<String>): Long {

        val area: MutableMap<Position, LocationProperties> = mutableMapOf()
        val neighbourGrid: MutableMap<Position, MutableList<MoveAction>> = mutableMapOf()
        parseGrid(input, area, neighbourGrid) { s: String ->
            return@parseGrid LocationProperties(s)
        }

        val antennas = area.filterValues { it.symbol != "." }
        val frequencies = antennas.values.groupBy { it.symbol }

        for (frequency in frequencies) {
            val positions = antennas.filter { it.value.symbol == frequency.key }.keys.toList()
            for (i in positions.indices) {
                for (j in positions.indices) {

                    if (i == j) continue

                    val first = positions[i]
                    val second = positions[j]

                    val antinodePosition = Position(first.x + (2 * (second.x - first.x)), first.y + (2 * (second.y - first.y)))
                    if (area.containsKey(antinodePosition) ) {
                        area[antinodePosition]!!.hasAntinode = true
                    }
                }
            }

        }

        return area.filterValues { it.hasAntinode}.keys.size.toLong()
    }

    fun markAntinodes(area: MutableMap<Position, LocationProperties>, startPosition :Position, delta :Position) {

        var newPosition = Position(startPosition.x + delta.x, startPosition.y + delta.y)
        while (area.containsKey(newPosition)) {

            area[newPosition]!!.hasAntinode = true
            newPosition = Position(newPosition.x + delta.x, newPosition.y + delta.y)
        }
    }

    fun part2 (input : List<String>): Long {

        val area: MutableMap<Position, LocationProperties> = mutableMapOf()
        val neighbourGrid: MutableMap<Position, MutableList<MoveAction>> = mutableMapOf()
        parseGrid(input, area, neighbourGrid) { s: String ->
            return@parseGrid LocationProperties(s)
        }

        val antennas = area.filterValues { it.symbol != "." }
        val frequencies = antennas.values.groupBy { it.symbol }.filter { it.value.size > 1 }

        for (frequency in frequencies) {
            val positions = antennas.filter { it.value.symbol == frequency.key }.keys.toList()
            for (i in positions.indices) {
                for (j in positions.indices) {

                    if (i == j) {
                        area[positions[i]]!!.hasAntinode = true
                        continue
                    }

                    val first = positions[i]
                    val second = positions[j]
                    val deltaPosition = Position(second.x - first.x, second.y - first.y)
                    markAntinodes(area, second, deltaPosition)
                }
            }

        }

        return area.filterValues { it.hasAntinode }.keys.size.toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day08_test")
    val input = readInput("2024","Day08")

    check(part1(testInput) == 14L)
    part1(input).println()

    check(part2(testInput) == 34L)
    part2(input).println()

}

