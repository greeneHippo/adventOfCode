package nl.groen.year2025

import nl.groen.Position
import nl.groen.println
import nl.groen.readInput

fun main() {

    data class Distance(val pair:Pair<Position, Position>, val distance:Long)
    fun part1 (input : List<String>, numberOfConnectionsToMake: Int): Long {

        val junctionBoxes = input.map { it.split(",") }.map { Position(it[0].toInt(), it[1].toInt(), it[2].toInt()) }

        val distances = mutableListOf<Distance>()
        junctionBoxes.forEachIndexed { index, box1 ->
            for (indexBox2 in index + 1 until junctionBoxes.size) {

                val box2 = junctionBoxes[indexBox2]
                val distance =
                    (
                        kotlin.math.abs(box1.x - box2.x).toLong() * kotlin.math.abs(box1.x - box2.x).toLong() +
                        kotlin.math.abs(box1.y - box2.y).toLong() * kotlin.math.abs(box1.y - box2.y).toLong() +
                        kotlin.math.abs(box1.z - box2.z).toLong() * kotlin.math.abs(box1.z - box2.z).toLong()
                    )

                distances.add(Distance(Pair(box1, box2), distance))
            }
        }

        distances.sortWith(Comparator { any, any1 -> any.distance.compareTo(any1.distance) })

        val junctions = mutableListOf<MutableSet<Position>>()

        var numberOfConnectionsMade = 0;
        while (numberOfConnectionsMade < numberOfConnectionsToMake) {
            val next = distances.removeFirst()

            val junctionWithFirstPosition = junctions.firstOrNull { junction -> junction.contains(next.pair.first) }
            val junctionWithSecondPosition = junctions.firstOrNull { junction -> junction.contains(next.pair.second) }

            if (junctionWithFirstPosition == null && junctionWithSecondPosition == null) {
                junctions.add(mutableSetOf(next.pair.first, next.pair.second))
                numberOfConnectionsMade++
                continue
            }

            if (junctionWithFirstPosition != null && junctionWithSecondPosition != null) {
                if (junctionWithFirstPosition == junctionWithSecondPosition) {
                    println("Positions already in the same junction")
                    numberOfConnectionsMade++
                    continue
                }
                junctionWithFirstPosition.addAll(junctionWithSecondPosition)
                junctions.remove(junctionWithSecondPosition)
                numberOfConnectionsMade++
                continue
            }

            if (junctionWithFirstPosition != null ) {
                junctionWithFirstPosition.add(next.pair.second)
                numberOfConnectionsMade++
                continue
            }
            if (junctionWithSecondPosition != null ) {
                junctionWithSecondPosition.add(next.pair.first)
                numberOfConnectionsMade++
                continue
            }

            throw IllegalStateException("Not possible")
//            println("Next pair: ${next}")
//            println("Join junction ${presence[0]} and ${presence[1]}")

        }

        val sortedJunctions = junctions.map{positions -> positions.size.toLong()}.sortedDescending().toTypedArray()

        val highestJunctions = sortedJunctions.take(3)
        return highestJunctions.reduce { acc, unit -> acc * unit }
    }

    fun part2 (input : List<String>): Long {

        return input.size.toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2025","Day08_test")
    val input = readInput("2025","Day08")

    check(part1(testInput, 10) == 40L)
    part1(input, 1000).println()

    check(part2(testInput) == 1L)
    part2(input).println()

}

