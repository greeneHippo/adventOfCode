package nl.groen.year2023.day16

import nl.groen.readInput

private fun partOne (lines : List<String>): Long {

    var expandedLines = mutableListOf<String>()
    var emptyLine = Mirror.NONE.symbol.repeat(lines[0].length)
    expandedLines.add(emptyLine)
    expandedLines.addAll(lines)
    expandedLines.add(emptyLine)
    expandedLines = expandedLines.map { return@map Mirror.NONE.symbol + it + Mirror.NONE.symbol }.toMutableList()

    var points = expandedLines.map { return@map it.map { s -> return@map Point(Mirror.get(s), hashMapOf()) } }

    // Mark the tile in the most North-West as an open tile.
    points[0][0].energized[Direction.EAST] = true
    //print2DList(points)

    return determineEnergizedPoints(points)
}

private fun partTwo (lines : List<String>): Long {

    var expandedLines = mutableListOf<String>()
    var emptyLine = Mirror.NONE.symbol.repeat(lines[0].length)
    expandedLines.add(emptyLine)
    expandedLines.addAll(lines)
    expandedLines.add(emptyLine)
    expandedLines = expandedLines.map { return@map Mirror.NONE.symbol + it + Mirror.NONE.symbol }.toMutableList()

    val energizedTilesEachConfiguration :ArrayList<Long> = arrayListOf()
    var result = 0L
    for (i in 0..<expandedLines.size) {
        var points = expandedLines.map { return@map it.map { s -> return@map Point(Mirror.get(s), hashMapOf()) } }

        points[i][0].energized[Direction.EAST] = true
        result = determineEnergizedPoints(points)
        energizedTilesEachConfiguration.add(result)
        println("$i -> $result")

        points = expandedLines.map { return@map it.map { s -> return@map Point(Mirror.get(s), hashMapOf()) } }
        points[0][i].energized[Direction.SOUTH] = true
        result = determineEnergizedPoints(points)
        energizedTilesEachConfiguration.add(result)
        println("$i -> $result")

        points = expandedLines.map { return@map it.map { s -> return@map Point(Mirror.get(s), hashMapOf()) } }
        points[i][points.size-1].energized[Direction.WEST] = true
        result = determineEnergizedPoints(points)
        energizedTilesEachConfiguration.add(result)
        println("$i -> $result")

        points = expandedLines.map { return@map it.map { s -> return@map Point(Mirror.get(s), hashMapOf()) } }
        points[points.size-1][i].energized[Direction.NORTH] = true
        result = determineEnergizedPoints(points)
        energizedTilesEachConfiguration.add(result)
        println("$i -> $result")
    }



    return energizedTilesEachConfiguration.max()
}

private fun determineEnergizedPoints(points: List<List<Point>>): Long {
    var iteration = 0
    do {
        val newEnergizedPoint: Int = findNewEnergizedPoints(points)
        iteration++

        //println("$iteration - $newEnergizedPoint - ${points.map { it.count { point -> point.energized.isNotEmpty() } }.reduce { acc, i -> acc + i }.toLong()}")
    } while (newEnergizedPoint != 0)

    //print2DList(points)
    //print2DListEnergized(points)
    var cutEdge = points.subList(1, points.size-1).map {return@map it.subList(1, it.size-1)}
    return cutEdge.map { it.count { point -> point.energized.isNotEmpty() } }.reduce { acc, i -> acc + i }.toLong()
}

fun findNewEnergizedPoints(array: List<List<Point>>): Int {

    var newEnergizedPoints = 0
    for ((y, row) in array.withIndex()) {
        for((x, sourcePoint) in row.withIndex()) {
            if (sourcePoint.energized.isEmpty()) {
                // Not energized, so no need to check yet
                continue
            }

            // Check all directions
            for (direction in Direction.entries) {

                if (sourcePoint.energized[direction] == null) {
                    // Not energized in this direction.
                    continue
                }

                val nextPoint: Point? = when(sourcePoint.mirror) {
                    Mirror.NONE, Mirror.SLASH, Mirror.BACKWARD_SLASH -> when(direction) {
                        Direction.EAST ->  { if (x!=row.size-1)   { array[y][x+1] } else {null} }
                        Direction.WEST ->  { if (x!=0)            { array[y][x-1] } else {null} }
                        Direction.NORTH -> { if (y!=0)            { array[y-1][x] } else {null} }
                        Direction.SOUTH -> { if (y!=array.size-1) { array[y+1][x] } else {null} }
                    }
                    Mirror.HORIZONTAL -> when(direction) {
                        Direction.EAST ->  { if (x!=row.size-1)   { array[y][x+1] } else {null} }
                        Direction.WEST ->  { if (x!=0)            { array[y][x-1] } else {null} }
                        Direction.NORTH -> { null }
                        Direction.SOUTH -> { null }
                    }
                    Mirror.VERTICAL -> when(direction) {
                        Direction.EAST ->  { null }
                        Direction.WEST ->  { null }
                        Direction.NORTH -> { if (y!=0)            { array[y-1][x] } else {null} }
                        Direction.SOUTH -> { if (y!=array.size-1) { array[y+1][x] } else {null} }
                    }
                }


                if (nextPoint == null) {
                    // There is no next point to check, or it is already energized in this direction.
                    continue
                }

                val currentDirectionsNextPoint = nextPoint.energized.toList().map { it.first }

//                newEnergizedPoints++
                if (nextPoint.mirror == Mirror.NONE ||
                    nextPoint.mirror == Mirror.VERTICAL && listOf(Direction.NORTH, Direction.SOUTH).contains(direction) ||
                    nextPoint.mirror == Mirror.HORIZONTAL && listOf(Direction.EAST, Direction.WEST).contains(direction)
                ) {
                    // We always mark this point as energized now in this direction
                    nextPoint.energized[direction] = true
                }
                if (nextPoint.mirror == Mirror.HORIZONTAL && listOf(Direction.NORTH, Direction.SOUTH).contains(direction)) {
                    // If a horizontal mirror comes from north or south we marked it as also going east and west
                    nextPoint.energized[Direction.WEST] = true
                    nextPoint.energized[Direction.EAST] = true
                }
                if (nextPoint.mirror == Mirror.VERTICAL && listOf(Direction.EAST, Direction.WEST).contains(direction)) {
                    // If a vertical mirror comes from west or east we marked it as also going north and south
                    nextPoint.energized[Direction.NORTH] = true
                    nextPoint.energized[Direction.SOUTH] = true
                }
                if (nextPoint.mirror == Mirror.SLASH) {

                    val directionToMark = when(direction) {
                        Direction.NORTH -> Direction.EAST
                        Direction.WEST -> Direction.SOUTH
                        Direction.SOUTH -> Direction.WEST
                        Direction.EAST -> Direction.NORTH
                    }
                    nextPoint.energized[directionToMark] = true
                }
                if (nextPoint.mirror == Mirror.BACKWARD_SLASH ) {
                    val directionToMark = when(direction) {
                        Direction.SOUTH -> Direction.EAST
                        Direction.EAST -> Direction.SOUTH
                        Direction.NORTH -> Direction.WEST
                        Direction.WEST -> Direction.NORTH
                    }
                    nextPoint.energized[directionToMark] = true
                }

                if (!currentDirectionsNextPoint.containsAll(nextPoint.energized.toList().map { it.first })) {
//                    println("$y, $x, $direction, ${nextPoint.mirror}")
                    newEnergizedPoints++
                }
            }
        }
    }

    return newEnergizedPoints
}

private fun print2DList(expandedArrayOfPoints: List<List<Point>>) {
    println("-------------------------------------------")
    expandedArrayOfPoints.forEach {
        it.forEach { point ->
            print(
                if (point.mirror == Mirror.NONE) {
                    if (point.energized.isEmpty()) {
                        "."
                    } else if (point.energized.count() > 1) {
                        "${point.energized.count()}"
                    } else {
                        when (point.energized.keys.stream().findFirst().get()) {
                            Direction.NORTH -> "^"
                            Direction.EAST -> ">"
                            Direction.SOUTH -> "v"
                            Direction.WEST -> "<"
                        }
                    }
                } else point.mirror.symbol
            )
        }
        println()
    }
}

private fun print2DListEnergized(expandedArrayOfPoints: List<List<Point>>) {
    println("-------------------------------------------")
    expandedArrayOfPoints.forEach {
        it.forEach { point ->
            print(
                if (point.energized.isNotEmpty()) {
                    "#"
                } else {
                    " "
                }
            )
        }
        println()
    }
}

fun main(args : Array<String>) {

    val lines = readInput("day16")
    println(partOne(lines))
    println(partTwo(lines))

}

class Point(val mirror: Mirror, var energized: HashMap<Direction, Boolean>)

enum class Direction { NORTH, EAST, SOUTH, WEST}

enum class Mirror (var symbol: String) {NONE("."), HORIZONTAL("-"), VERTICAL("|"), SLASH("/"), BACKWARD_SLASH("\\");
    companion object {
        fun get(input: Char): Mirror {
            return Mirror.entries.firstOrNull { part -> input.uppercase() == part.symbol }!!
        }
    }
}
