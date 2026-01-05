package nl.groen.year2023.day10

import nl.groen.readInput
import nl.groen.transpose

private fun partTwo (lines : List<String>): Long {

    val resultLoop = determineLoop(lines)
    var arrayPoints = resultLoop.arrayOfPoints

    // Expand each tile to a 3x3 matrix:
    val expandedArrayOfPoints: Array<Array<Point>> = expandArray(arrayPoints)

    print2DList(arrayPoints)
    //printArray(expandedArrayOfPoints)

    // Mark the tile in the most North-West as an open tile.
    expandedArrayOfPoints[0][0].outsideLoop = true
    var iteration = 0
    do {
        val newTiles :Int = findNewTiles(expandedArrayOfPoints)
        iteration++
        //println("$iteration - $newTiles")
    } while (newTiles != 0)


    shrinkAray(arrayPoints, expandedArrayOfPoints)

    //printArray(expandedArrayOfPoints)
    print2DList(arrayPoints)

    var numberNotEnclosedTiles = 0L
    for ((y, row) in arrayPoints.withIndex()) {
        for ((x, sourcePoint) in row.withIndex()) {

            val yExpanded = y * 3 + 1
            val xExpanded = x * 3 + 1
            if (!expandedArrayOfPoints[yExpanded][xExpanded].partOfLoop &&
                !expandedArrayOfPoints[yExpanded-1][xExpanded-1].outsideLoop &&
                !expandedArrayOfPoints[yExpanded-1][xExpanded].outsideLoop &&
                !expandedArrayOfPoints[yExpanded-1][xExpanded+1].outsideLoop &&
                !expandedArrayOfPoints[yExpanded][xExpanded-1].outsideLoop &&
                !expandedArrayOfPoints[yExpanded][xExpanded].outsideLoop &&
                !expandedArrayOfPoints[yExpanded][xExpanded+1].outsideLoop &&
                !expandedArrayOfPoints[yExpanded+-1][xExpanded-1].outsideLoop &&
                !expandedArrayOfPoints[yExpanded+-1][xExpanded].outsideLoop &&
                !expandedArrayOfPoints[yExpanded+-1][xExpanded+1].outsideLoop)
                numberNotEnclosedTiles++
        }
    }

    return numberNotEnclosedTiles
}

fun shrinkAray(arrayPoints: List<List<Point>>, expandedArrayOfPoints: Array<Array<Point>>) {
    for ((y, row) in arrayPoints.withIndex()) {
        for ((x, sourcePoint) in row.withIndex()) {

            val yExpanded = y * 3 + 1
            val xExpanded = x * 3 + 1
            sourcePoint.outsideLoop = expandedArrayOfPoints[yExpanded][xExpanded].outsideLoop
        }
    }
}

private fun print2DArray(expandedArrayOfPoints: Array<Array<Point>>) {
    expandedArrayOfPoints.forEach {
        it.forEach { point ->
            print(
                if (point.outsideLoop) {
                    "O"
                } else point.part.outputSymbol
            )
        }
        println()
    }
}

private fun print2DList(expandedArrayOfPoints: List<List<Point>>) {
    expandedArrayOfPoints.forEach {
        it.forEach { point ->
            print(
                if (point.outsideLoop) {
                    "O"
                } else point.part.outputSymbol
            )
        }
        println()
    }
}

private fun expandArray(arrayPoints: List<List<Point>>): Array<Array<Point>> {
    val expandedArrayOfPoints: Array<Array<Point>> = Array(arrayPoints.size * 3) { Array(arrayPoints[0].size * 3) { Point(PipePart.GROUND) } }

    for ((y, row) in arrayPoints.withIndex()) {
        for ((x, sourcePoint) in row.withIndex()) {

            val yExpanded = y * 3 + 1
            val xExpanded = x * 3 + 1

            if (!sourcePoint.partOfLoop) {
                // Point is not part of the loop, the default is OK (ground / not part of loop)
                continue
            }
            if (sourcePoint.part == PipePart.VERTICAL) {
                expandedArrayOfPoints[yExpanded - 1][xExpanded] = sourcePoint
                expandedArrayOfPoints[yExpanded][xExpanded] = sourcePoint
                expandedArrayOfPoints[yExpanded + 1][xExpanded] = sourcePoint
            }
            if (sourcePoint.part == PipePart.HORIZONTAL) {
                expandedArrayOfPoints[yExpanded][xExpanded - 1] = sourcePoint
                expandedArrayOfPoints[yExpanded][xExpanded] = sourcePoint
                expandedArrayOfPoints[yExpanded][xExpanded + 1] = sourcePoint
            }
            if (sourcePoint.part == PipePart.START) {
                expandedArrayOfPoints[yExpanded][xExpanded - 1] = sourcePoint
                expandedArrayOfPoints[yExpanded][xExpanded + 1] = sourcePoint
                expandedArrayOfPoints[yExpanded][xExpanded] = sourcePoint
                expandedArrayOfPoints[yExpanded - 1][xExpanded] = sourcePoint
                expandedArrayOfPoints[yExpanded + 1][xExpanded] = sourcePoint
            }
            if (sourcePoint.part == PipePart.BEND_NE) {
                expandedArrayOfPoints[yExpanded - 1][xExpanded] = Point(PipePart.VERTICAL)
                expandedArrayOfPoints[yExpanded - 1][xExpanded].partOfLoop = true
                expandedArrayOfPoints[yExpanded][xExpanded] = sourcePoint
                expandedArrayOfPoints[yExpanded][xExpanded + 1] = Point(PipePart.HORIZONTAL)
                expandedArrayOfPoints[yExpanded][xExpanded + 1].partOfLoop = true
            }
            if (sourcePoint.part == PipePart.BEND_NW) {
                expandedArrayOfPoints[yExpanded - 1][xExpanded] = Point(PipePart.VERTICAL)
                expandedArrayOfPoints[yExpanded - 1][xExpanded].partOfLoop = true
                expandedArrayOfPoints[yExpanded][xExpanded] = sourcePoint
                expandedArrayOfPoints[yExpanded][xExpanded - 1] = Point(PipePart.HORIZONTAL)
                expandedArrayOfPoints[yExpanded][xExpanded - 1].partOfLoop = true
            }
            if (sourcePoint.part == PipePart.BEND_SE) {
                expandedArrayOfPoints[yExpanded + 1][xExpanded] = Point(PipePart.VERTICAL)
                expandedArrayOfPoints[yExpanded + 1][xExpanded].partOfLoop = true
                expandedArrayOfPoints[yExpanded][xExpanded] = sourcePoint
                expandedArrayOfPoints[yExpanded][xExpanded + 1] = Point(PipePart.HORIZONTAL)
                expandedArrayOfPoints[yExpanded][xExpanded + 1].partOfLoop = true
            }
            if (sourcePoint.part == PipePart.BEND_SW) {
                expandedArrayOfPoints[yExpanded + 1][xExpanded] = Point(PipePart.VERTICAL)
                expandedArrayOfPoints[yExpanded + 1][xExpanded].partOfLoop = true
                expandedArrayOfPoints[yExpanded][xExpanded] = sourcePoint
                expandedArrayOfPoints[yExpanded][xExpanded - 1] = Point(PipePart.HORIZONTAL)
                expandedArrayOfPoints[yExpanded][xExpanded - 1].partOfLoop = true
            }
        }
    }
    return expandedArrayOfPoints
}

fun findNewTiles(arrayPoints: Array<Array<Point>>): Int {

    var tilesMarkedAsOpen = 0
    for ((y, row) in arrayPoints.withIndex()) {
        for((x, sourcePoint) in row.withIndex()) {
            if (!sourcePoint.outsideLoop) {
                continue
            }
            val nextAdjacents = determineAdjacents(y, x, arrayPoints, row)

            for (adjacent in nextAdjacents) {
                if (adjacent.point.outsideLoop) {
                    continue
                }
                if (!adjacent.point.partOfLoop) {
                    //                   println("Mark a tile not part of the loop as outside: ${adjacent.y},${adjacent.x} part: ${adjacent.point.part} via $y,$x")
                    arrayPoints[adjacent.y][adjacent.x].outsideLoop = true
                    tilesMarkedAsOpen++
                    continue
                }
            }
        }
    }

    return tilesMarkedAsOpen
}

private fun determineAdjacents(y: Int, x: Int, arrayPoints: Array<Array<Point>>, row: Array<Point>): MutableList<AdjacentPointClosedTileDetector> {
    var nextAdjacents = mutableListOf<AdjacentPointClosedTileDetector>()

    if (y != 0) {
        nextAdjacents.add(AdjacentPointClosedTileDetector(y-1, x, arrayPoints[y - 1][x], PipePart.Direction.NORTH))
    }
    if (y != arrayPoints.size-1) {
        nextAdjacents.add(AdjacentPointClosedTileDetector(y+1, x, arrayPoints[y + 1][x], PipePart.Direction.SOUTH))
    }
    if (x != 0) {
        nextAdjacents.add(AdjacentPointClosedTileDetector(y, x-1, arrayPoints[y][x - 1], PipePart.Direction.WEST))
    }
    if (x != row.size-1) {
        nextAdjacents.add(AdjacentPointClosedTileDetector(y, x+1, arrayPoints[y][x + 1], PipePart.Direction.EAST))
    }
    return nextAdjacents
}

private fun partOne (lines : List<String>): Long {

    var resultLoop = determineLoop(lines)

    return resultLoop.iterations
}

private fun determineLoop(lines: List<String>): ResultLoop {
    var expandedLines = mutableListOf<String>()
    var emptyLine = PipePart.GROUND.symbol.repeat(lines[0].length)
    expandedLines.add(emptyLine)
    expandedLines.addAll(lines)
    expandedLines.add(emptyLine)
    expandedLines = expandedLines.map { return@map PipePart.GROUND.symbol + it + PipePart.GROUND.symbol }.toMutableList()
    val arrayPipeParts = expandedLines.map { return@map it.toCharArray().map { symbol -> return@map Point(PipePart.get(symbol)) } }
    var start: Pair<Int, Int> = determineStart(arrayPipeParts)
    var startTravelPoint = TravelPoint(start.first, start.second, PipePart.START, Pair(-1, -1), PipePart.Direction.EAST)
    arrayPipeParts[start.first][start.second].partOfLoop = true
    var parts = determineNextPoints(startTravelPoint, arrayPipeParts)
    var newFirst = parts[0]
    var newSecond = parts[1]
    var i = 1
    do {
        //println("$i - ${newFirst.part} (${newFirst.y},${newFirst.x}) - ${newSecond.part} (${newSecond.y},${newSecond.x})")
        newFirst = determineNextPoints(newFirst, arrayPipeParts).firstOrNull()!!
        newSecond = determineNextPoints(newSecond, arrayPipeParts).firstOrNull()!!
        i++
    } while (newFirst.y != newSecond.y || newFirst.x != newSecond.x)
    return ResultLoop(i.toLong(), arrayPipeParts)
}

fun determineStart(arrayPipeParts: List<List<Point>>): Pair<Int, Int> {
    var y = arrayPipeParts.mapIndexed { i, points -> return@mapIndexed if (points.any{it.part == PipePart.START }) i else null}.firstNotNullOf { it }
    var x = transpose(arrayPipeParts).mapIndexed { i, points -> return@mapIndexed if (points.any{it.part == PipePart.START }) i else null}.firstNotNullOf { it }
    return Pair(y,x)
}

fun determineNextPoints(travelPoint: TravelPoint, arrayPipeParts: List<List<Point>>): List<TravelPoint> {
    var coordinatesOldPoint = Pair(travelPoint.y,travelPoint.x)
    var nextAdjacents = listOf(
        TravelPoint(travelPoint.y+1, travelPoint.x, arrayPipeParts[travelPoint.y+1][travelPoint.x].part, coordinatesOldPoint, PipePart.Direction.SOUTH),
        TravelPoint(travelPoint.y-1, travelPoint.x, arrayPipeParts[travelPoint.y-1][travelPoint.x].part, coordinatesOldPoint, PipePart.Direction.NORTH),
        TravelPoint(travelPoint.y, travelPoint.x+1, arrayPipeParts[travelPoint.y][travelPoint.x+1].part, coordinatesOldPoint, PipePart.Direction.EAST),
        TravelPoint(travelPoint.y, travelPoint.x-1, arrayPipeParts[travelPoint.y][travelPoint.x-1].part, coordinatesOldPoint, PipePart.Direction.WEST)
    )
    // Remove the previous point
    nextAdjacents = nextAdjacents.filter { it.y != travelPoint.previous.first || it.x != travelPoint.previous.second}.toMutableList()
    // Determine the valid next part
    nextAdjacents = nextAdjacents.filter { travelPoint.part.directionsTo.contains(it.travelDirection) && it.part.directionsFrom.contains(it.travelDirection)}

    for (adjacent in nextAdjacents) {
        // Mark the point in the array as part of the loop.
        arrayPipeParts[adjacent.y][adjacent.x].partOfLoop = true
    }
    return nextAdjacents
}

fun main(args : Array<String>) {

    val lines = readInput("day10")
    println("Solution part 1: number of iterations: ${partOne(lines)}")
    println(partTwo(lines))

}

class TravelPoint(val y: Int, val x: Int, val part: PipePart, val previous: Pair<Int, Int>, val travelDirection: PipePart.Direction)

class AdjacentPointClosedTileDetector(val y: Int, val x: Int, val point: Point, val travelDirection: PipePart.Direction)

class Point(var part: PipePart) {
    var partOfLoop = false
    var outsideLoop = false
    var directionsToOutsideLoop: List<PipePart.Direction> = mutableListOf()
}

data class ResultLoop(val iterations: Long, val arrayOfPoints: List<List<Point>>)
