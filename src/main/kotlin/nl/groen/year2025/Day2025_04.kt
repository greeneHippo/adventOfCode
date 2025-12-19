package nl.groen.year2025

import nl.groen.*

enum class Type(val symbol: String) {
    PAPER("@"),
    PAPER_ACCESSIBLE("x"),
    EMPTY(".");

    override fun toString() :String {
        return this.symbol
    }
}

fun main() {

    fun init(input: List<String>): Pair<MutableMap<Position, Type>, MutableMap<Position, MutableList<MoveAction>>> {
        val area: MutableMap<Position, Type> = mutableMapOf()
        val neighbourGrid: MutableMap<Position, MutableList<MoveAction>> = mutableMapOf()
        parseGrid(input, area, neighbourGrid, true) { s: String ->
            return@parseGrid Type.entries.firstOrNull { part -> s == part.symbol }!!
        }
        return Pair(area, neighbourGrid)
    }

    fun part1 (input : List<String>): Long {

        val (area: MutableMap<Position, Type>, neighbourGrid: MutableMap<Position, MutableList<MoveAction>>) = init(
            input
        )

//        printGridOfPoints(area)
//        println("-------------------------------------------------")
        area.filter { it.value == Type.PAPER }.forEach { forkliftPosition ->
            val adjacentPapers = neighbourGrid[forkliftPosition.key]!!.filter { area[it.position] == Type.PAPER || area[it.position] == Type.PAPER_ACCESSIBLE }
            if (adjacentPapers.count() < 4) {
                    area[forkliftPosition.key] = Type.PAPER_ACCESSIBLE
//                }

            }
        }

//        printGridOfPoints(area)
        return area.count { it.value == Type.PAPER_ACCESSIBLE }.toLong()
    }

    fun part2 (input : List<String>): Long {

        val (area: MutableMap<Position, Type>, neighbourGrid: MutableMap<Position, MutableList<MoveAction>>) = init(
            input
        )

        var removedRollPapers = -1
        var totalRemovedRollPapers = 0

        while (removedRollPapers != 0) {
            removedRollPapers=0
            area.filter { it.value == Type.PAPER }.forEach { forkliftPosition ->
                val adjacentPapers = neighbourGrid[forkliftPosition.key]!!.filter { area[it.position] == Type.PAPER || area[it.position] == Type.PAPER_ACCESSIBLE }
                if (adjacentPapers.count() < 4) {
                    area[forkliftPosition.key] = Type.PAPER_ACCESSIBLE
                    removedRollPapers++
                }

            }
            totalRemovedRollPapers+=area.count { it.value == Type.PAPER_ACCESSIBLE }
            area.filter {it.value == Type.PAPER_ACCESSIBLE}.forEach {
                area[it.key] = Type.EMPTY
            }
        }



        return totalRemovedRollPapers.toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2025","Day04_test")
    val input = readInput("2025","Day04")

    check(part1(testInput) == 13L)
    part1(input).println()

    check(part2(testInput) == 43L)
    part2(input).println()

}

