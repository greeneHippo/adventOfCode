package nl.groen.previousDays.day11

import nl.groen.previousDays.GalaxyCoordinates
import nl.groen.readLines
import java.util.stream.Collectors
import java.util.stream.IntStream


private fun partOne (lines : List<String>): Long {

    var universe = lines.map { it.toCharArray().toMutableList()}.toMutableList()
    // First expand vertically
    var expandedUniverse = expandUniverse(universe)
    // Transpose to expand horizontally
    expandedUniverse = transpose(expandedUniverse)
    expandedUniverse = expandUniverse(expandedUniverse)
    // Transpose back
    expandedUniverse = transpose(expandedUniverse)


    var galaxyCoordinates = determineGalaxPositions(expandedUniverse)

    var result = galaxyCoordinates.map { it -> galaxyCoordinates.map{other -> it.shortestPathToGalaxy(other)}.reduce{sum, element -> sum+element}}

    return result.reduce{sum, element -> sum+element}.toLong()/2
}

private fun determineGalaxPositions(expandedUniverse: List<List<Char>>): MutableSet<GalaxyCoordinates> {

    var result = mutableSetOf<GalaxyCoordinates>()
    for (i in expandedUniverse.indices) {
        for (j in expandedUniverse[i].indices) {
            if (expandedUniverse[i][j] == '#') {
                result.add(GalaxyCoordinates(i, j))
            }
        }

    }

    return result
}

private fun expandUniverse(universe: List<List<Char>>) : List<List<Char>> {

    var emptyLines = arrayListOf<Int>()
    var expandedUniverse = universe.toMutableList()
    for (i in universe.indices) {

        if (!universe[i].stream().anyMatch { it == '#' }) {
            emptyLines.add(i)
        }
    }

    var i = 0
    for (j in emptyLines) {
        var tempList = expandedUniverse.subList(0, i+j+1)
        tempList.add(expandedUniverse[i+j])
        tempList.addAll(expandedUniverse.subList(i+j+2, expandedUniverse.size))
        expandedUniverse = tempList
        i++
    }

    return expandedUniverse
}

fun <T> transpose(list: List<List<T>>): List<List<T>> {
    val N = list.stream().mapToInt { l: List<T> -> l.size }.max().orElse(-1)
    val iterList = list.stream().map { it: List<T> -> it.iterator() }.collect(Collectors.toList())
    return IntStream.range(0, N)
        .mapToObj { n: Int ->
            iterList.stream()
                .filter { it: Iterator<T> -> it.hasNext() }
                .map { m: Iterator<T> -> m.next() }
                .collect(Collectors.toList())
        }
        .collect(Collectors.toList())
}

private fun partTwo (lines : List<String>): Long {

    var universe = lines.map { it.toCharArray().toMutableList()}.toMutableList()
    // First expand vertically
    var expandedUniverse = expandUniverse(universe)
    // Transpose to expand horizontally
    expandedUniverse = transpose(expandedUniverse)
    expandedUniverse = expandUniverse(expandedUniverse)
    // Transpose back
    expandedUniverse = transpose(expandedUniverse)

    val resultNotExpanded = determineGalaxiesAndReturnTravelLength(universe)
    val resultExpanded = determineGalaxiesAndReturnTravelLength(expandedUniverse)
    val difference = resultExpanded - resultNotExpanded
    println(difference)
    return resultNotExpanded + ((1000000L-1L) * difference)
}

private fun determineGalaxiesAndReturnTravelLength(universe: List<List<Char>>): Long {
    var galaxyCoordinates = determineGalaxPositions(universe)
    var result = galaxyCoordinates.map { it -> galaxyCoordinates.map { other -> it.shortestPathToGalaxy(other) }.reduce { sum, element -> sum + element } }
    return result.reduce { sum, element -> sum + element }.toLong() / 2
}

fun main(args : Array<String>) {

    val lines = readLines("day11")
    println(partOne(lines))
    println(partTwo(lines))

}

