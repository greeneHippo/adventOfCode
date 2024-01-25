package nl.groen

enum class Result(val symbol :String, val score :Long) {
    LOSE("X", 0),
    DRAW( "Y", 3),
    WIN( "Z", 6);

    companion object {
        fun getBySymbol(input: String): Result {
            return Result.entries.firstOrNull { part -> input == part.symbol }!!
        }
    }
}
enum class Shape(val symbol1 :String, val symbol2 :String, val score :Long) {
    ROCK("A", "X", 1),
    PAPER("B", "Y", 2),
    SCISSORS("C", "Z", 3);

    companion object {
        fun getBySymbol1(input: String): Shape {
            return Shape.entries.firstOrNull { part -> input == part.symbol1 }!!
        }
        fun getBySymbol2(input: String): Shape {
            return Shape.entries.firstOrNull { part -> input == part.symbol2 }!!
        }

        fun determineScore(opponent: Shape, current: Shape): Long {
            if (opponent == current) {
                return 3L
            }

            if (current == SCISSORS && opponent == PAPER) {
                return 6L
            }
            if (current == PAPER && opponent == ROCK) {
                return 6L
            }
            if (current == ROCK && opponent == SCISSORS) {
                return 6L
            }

            return 0L
        }

    }
}
fun main() {

    fun part1 (input : List<String>): Long {

        val plays = input.map { it.split(" ")  }.map { arrayListOf(Shape.getBySymbol1(it[0]), Shape.getBySymbol2(it[1])) }

        val scoreEachRound = plays.map { it.last().score + Shape.determineScore(it.first(), it.last()) }

        return scoreEachRound.sum()
    }


    fun determineShape(handOpponent: Shape, result: Result): Shape {

        if (result == Result.DRAW) {
            return handOpponent
        }
        if (result == Result.WIN) {
            return when(handOpponent) {
                Shape.ROCK -> Shape.PAPER
                Shape.PAPER -> Shape.SCISSORS
                Shape.SCISSORS -> Shape.ROCK
            }
        }
        return when(handOpponent) {
            Shape.ROCK -> Shape.SCISSORS
            Shape.PAPER -> Shape.ROCK
            Shape.SCISSORS -> Shape.PAPER
        }
    }

    fun part2 (input : List<String>): Long {

        val plays = input.map { it.split(" ")  }.map { Pair(Shape.getBySymbol1(it[0]), Result.getBySymbol(it[1])) }

        val scoreEachRound = plays.map { it.second.score + determineShape(it.first, it.second).score }

        return scoreEachRound.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15L)

    val input = readInput("Day02")
    part1(input).println()

    check(part2(testInput) == 12L)
    part2(input).println()

}

