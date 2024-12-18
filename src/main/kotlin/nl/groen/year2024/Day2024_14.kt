package nl.groen.year2024

import nl.groen.PositionLong
import nl.groen.println
import nl.groen.readInput
import java.util.stream.IntStream

data class Robot(val position: PositionLong, val velocity: PositionLong)

fun List<Robot>.simulate(seconds: Long, lengthX: Int, lengthY: Int) :List<Robot> {

    return this.map {
        Robot(
            PositionLong(
            ((it.velocity.x * seconds) + it.position.x) % lengthX,
            ((it.velocity.y * seconds) + it.position.y) % lengthY
            ),
            it.velocity
        )
    }.map{
        Robot(
            PositionLong((it.position.x + lengthX) % lengthX, (it.position.y + lengthY) % lengthY),
            it.velocity
        )
    }

}

fun List<Robot>.splitIntoQuadrants(lengthX: Int, lengthY: Int) :List<Long> {
    val result : MutableList<Long> = mutableListOf()
    for (x in 0 until 2) {
        val xPointsPerQuadrant = lengthX / 2
        for (y in 0 until 2) {
            val yPointsPerQuadrant = lengthY / 2
            val count = this.count {
                    it.position.x in (x * (xPointsPerQuadrant+1))..<xPointsPerQuadrant + ( 2 * x * xPointsPerQuadrant) &&
                    it.position.y in (y * (yPointsPerQuadrant+1))..<yPointsPerQuadrant + ( 2 * y * yPointsPerQuadrant)
                }
            result.add(count.toLong())
        }
    }

    return result

}

fun main() {


    fun part1 (input : List<String>, lengthX: Int, lengthY: Int): Long {

        val robots = input
            .map { it.split("p=", ","," v=") }
            .map { s -> Robot(PositionLong(s[1].toLong(), s[2].toLong()), PositionLong(s[3].toLong(), s[4].toLong())) }

        val robotsAfterSimulation = robots.simulate(100L, lengthX, lengthY)
        val quadrants = robotsAfterSimulation.splitIntoQuadrants(lengthX, lengthY)

        return quadrants.reduce { acc, l -> acc * l  }
    }

    fun part2 (input : List<String>): Long {

        var robots = input
            .map { it.split("p=", ","," v=") }
            .map { s -> Robot(PositionLong(s[1].toLong(), s[2].toLong()), PositionLong(s[3].toLong(), s[4].toLong())) }

        var seconds = 0L
        var resultCount = 0
        while(true) {
            seconds += 1

            robots = robots.simulate(1, 101, 103)
            val allHaveNeighbours = robots.count { robot ->
                robots.any { next -> next.position.x == robot.position.x + 0 && next.position.y == robot.position.y + 1} ||
                robots.any { next -> next.position.x == robot.position.x + 0 && next.position.y == robot.position.y - 1} ||
                robots.any { next -> next.position.x == robot.position.x + 1 && next.position.y == robot.position.y + 0} ||
                robots.any { next -> next.position.x == robot.position.x - 1 && next.position.y == robot.position.y + 0} ||
                robots.any { next -> next.position.x == robot.position.x + 1 && next.position.y == robot.position.y + 1} ||
                robots.any { next -> next.position.x == robot.position.x + 1 && next.position.y == robot.position.y - 1} ||
                robots.any { next -> next.position.x == robot.position.x - 1 && next.position.y == robot.position.y + 1} ||
                robots.any { next -> next.position.x == robot.position.x - 1 && next.position.y == robot.position.y - 1}}

            if (allHaveNeighbours > resultCount ) {
                resultCount = allHaveNeighbours
                println("$seconds - $allHaveNeighbours")
                IntStream.range(0, 101).forEach { x ->
                    IntStream.range(0, 101).forEach { y ->
                        val symbol = if (robots.count { it.position.x == x.toLong() && it.position.y == y.toLong() } == 0) "." else "1"
                        print(symbol)
                    }
                    println("")
                }
            }

        }

        return input.size.toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day14_test")
    val input = readInput("2024","Day14")

    check(part1(testInput, 11, 7) == 12L)
    part1(input, 101, 103).println()

//    check(part2(testInput) == 1L)
    part2(input).println()

}

