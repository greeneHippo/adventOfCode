package nl.groen.year2025

import nl.groen.Position
import nl.groen.println
import nl.groen.readInput
import kotlin.math.abs

fun main() {

    data class Distance(val pair:Pair<Position, Position>, val distance:Long)

    fun init(input: List<String>): MutableList<Distance> {
        val junctionBoxes = input.map { it.split(",") }.map { Position(it[0].toInt(), it[1].toInt(), it[2].toInt()) }

        val distances = mutableListOf<Distance>()
        junctionBoxes.forEachIndexed { index, box1 ->
            for (indexBox2 in index + 1 until junctionBoxes.size) {

                val box2 = junctionBoxes[indexBox2]
                val distance =
                    (
                            abs(box1.x - box2.x).toLong() * abs(box1.x - box2.x).toLong() +
                                    abs(box1.y - box2.y).toLong() * abs(box1.y - box2.y).toLong() +
                                    abs(box1.z - box2.z).toLong() * abs(box1.z - box2.z).toLong()
                            )

                distances.add(Distance(Pair(box1, box2), distance))
            }
        }

        distances.sortWith(Comparator { any, any1 -> any.distance.compareTo(any1.distance) })
        return distances
    }

    fun evaluateNextPair(junctions: MutableList<MutableSet<Position>>, next: Distance) {
        val junctionWithFirstPosition = junctions.firstOrNull { junction -> junction.contains(next.pair.first) }
        val junctionWithSecondPosition = junctions.firstOrNull { junction -> junction.contains(next.pair.second) }

        if (junctionWithFirstPosition == null && junctionWithSecondPosition == null) {
            junctions.add(mutableSetOf(next.pair.first, next.pair.second))
            return
        }

        if (junctionWithFirstPosition != null && junctionWithSecondPosition != null) {
            if (junctionWithFirstPosition == junctionWithSecondPosition) {
                return
            }
            junctionWithFirstPosition.addAll(junctionWithSecondPosition)
            junctions.remove(junctionWithSecondPosition)
            return
        }

        if (junctionWithFirstPosition != null) {
            junctionWithFirstPosition.add(next.pair.second)
            return
        }
        if (junctionWithSecondPosition != null) {
            junctionWithSecondPosition.add(next.pair.first)
            return
        }

        throw IllegalStateException("Not possible")
    }

    fun part1 (input : List<String>, numberOfConnectionsToMake: Int): Long {

        val distances = init(input)

        val junctions = mutableListOf<MutableSet<Position>>()

        var numberOfConnectionsMade = 0
        while (numberOfConnectionsMade < numberOfConnectionsToMake) {
            val next = distances.removeFirst()

            evaluateNextPair(junctions, next)
            numberOfConnectionsMade++
        }

        val sortedJunctions = junctions.map{positions -> positions.size.toLong()}.sortedDescending().toTypedArray()

        val highestJunctions = sortedJunctions.take(3)
        return highestJunctions.reduce { acc, unit -> acc * unit }
    }

    fun part2 (input : List<String>): Long {
        val distances = init(input)

        val junctions = mutableListOf<MutableSet<Position>>()
        var lastPair: Distance? = null
        var largestJunction = 0
        while (largestJunction != input.size) {
            val next :Distance = distances.removeFirst()

            evaluateNextPair(junctions, next)
            lastPair = next
            largestJunction = junctions.maxOf { junction -> junction.size }
        }
        return lastPair!!.pair.first.x.toLong() * lastPair.pair.second.x.toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2025","Day08_test")
    val input = readInput("2025","Day08")

    check(part1(testInput, 10) == 40L)
    part1(input, 1000).println()

    check(part2(testInput) == 25272L)
    part2(input).println()

}

