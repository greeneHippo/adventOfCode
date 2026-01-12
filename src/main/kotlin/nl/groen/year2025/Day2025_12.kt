package nl.groen.year2025

import nl.groen.groupStringsOnEmptyLine
import nl.groen.println
import nl.groen.readInput
import kotlin.collections.flatten

fun main() {

    data class Region(val length : Int, val width : Int, val requiredPresents : List<Int>)

    fun part1 (input : List<String>): Long {

        val strings = groupStringsOnEmptyLine(input)
        val presents = strings.dropLast(1).map {
            it.drop(1)
        }
        val regions = strings.last().map {
            val lineSplit = it.split("x", ": ", " ")
            Region(
                lineSplit[0].toInt(),
                lineSplit[1].toInt(),
                lineSplit.subList(2, lineSplit.size).filter{it.isNotEmpty()}.map { it.toInt() }
            )
        }

        val regionsWitEnoughSize = regions.filter { region ->
            val sizeRegion = region.length * region.width
            val sizePresents = region.requiredPresents.mapIndexed { index, count -> count * presents[index].joinToString ("").count { it.toString() == "#" }}.sum()
            return@filter sizeRegion >= sizePresents
        }
        return regionsWitEnoughSize.count().toLong()
    }

    // test if implementation meets criteria from the description, like:
    val input = readInput("2025","Day12")

    part1(input).println()

}

