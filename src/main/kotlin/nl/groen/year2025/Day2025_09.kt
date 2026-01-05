package nl.groen.year2025

import nl.groen.Position
import nl.groen.PositionDouble
import nl.groen.doIntersect
import nl.groen.mapPositionToDouble
import nl.groen.println
import nl.groen.readInput
import kotlin.math.abs

fun mapVectorToDouble(pair: Pair<Position, Position>): Pair<PositionDouble, PositionDouble> {
    return Pair(mapPositionToDouble(pair.first), mapPositionToDouble(pair.second))
}

fun main() {

    fun part1 (input : List<String>): Long {

        val redTiles = input.map { it.split(",") }.map { Position(it[0].toInt(), it[1].toInt()) }

        var largestArea = 0L
        redTiles.forEachIndexed { index, box1 ->
            for (indexBox2 in index + 1..<redTiles.size) {
                val box2 = redTiles[indexBox2]
                val currentArea = (1 + abs(box1.x - box2.x).toLong()) * (1 + abs(box1.y - box2.y).toLong())
                if (currentArea > largestArea) {
                    largestArea = currentArea
                }
            }
        }

        return largestArea
    }

    fun part2 (input : List<String>): Long {

        val redTiles = input.map { it.split(",") }.map { Position(it[0].toInt(), it[1].toInt()) }.toMutableList()
        val vectors = redTiles.mapIndexed { index, position ->
            val secondPosition = redTiles[(index+1) % redTiles.size]
            return@mapIndexed Pair(position, secondPosition)
        }

        var largestArea = 0L
        redTiles.forEachIndexed { index, box1 ->
            for (indexBox2 in index + 1..<redTiles.size) {
                val box2 = redTiles[indexBox2]

                // Check first on area since calculating intersection is more costly
                val currentArea = (1 + abs(box1.x - box2.x).toLong()) * (1 + abs(box1.y - box2.y).toLong())
                if (currentArea < largestArea) {
                    continue
                }

                val diagonalOne = Pair(box1, box2)
                val diagonalTwo = Pair(Position(box1.x, box2.y), Position(box2.x, box1.y))

                // Filter out the lines originating from the two points. Then check if the other lines intersect with the diagonals of the Rectangle.
                val vectorThatIntersect = vectors
                    .filter { pair ->
                        pair.first != diagonalOne.first &&
                        pair.second != diagonalOne.first &&
                        pair.first != diagonalOne.second &&
                        pair.second != diagonalOne.second
                    }
                    .filter { vector ->
                        doIntersect(
                        mapVectorToDouble(vector),
                        mapVectorToDouble(diagonalOne)
                            ) ||
                        doIntersect(
                            mapVectorToDouble(vector),
                            mapVectorToDouble(diagonalTwo)
                        )
                    }

                if (vectorThatIntersect.isEmpty()) {
                    largestArea = currentArea
                }
            }
        }

        return largestArea
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2025","Day09_test")
    val input = readInput("2025","Day09")

    check(part1(testInput) == 50L)
    part1(input).println()

    check(part2(testInput) == 24L)
    part2(input).println()

}

