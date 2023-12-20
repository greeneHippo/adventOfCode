package nl.groen.day18

import nl.groen.Direction
import nl.groen.print2DArray
import nl.groen.readLines
import nl.groen.transpose

private class DigOrder(var direction: Direction, var steps: Int)
class Status(var partOfLoop: Boolean, var dug: Boolean ) : Any() {
    fun markAsPartOfLoop() {
        partOfLoop = true
        dug = false
    }

    override fun toString() : String {
        return if (partOfLoop) {
            "#"
        } else if (dug) {
            "X"
        } else {
            "."
        }
    }
}
class AdjacentPointClosedTileDetector(val y: Int, val x: Int, val status: Status)

fun findNewTiles(arrayPoints: Array<Array<Status>>): Int {

    var tilesMarkedAsOpen = 0
    for ((y, row) in arrayPoints.withIndex()) {
        for((x, sourcePoint) in row.withIndex()) {
            if (!sourcePoint.dug) {
                continue
            }
            val nextAdjacents = determineAdjacents(y, x, arrayPoints, row)

            for (adjacent in nextAdjacents) {
                if (adjacent.status.dug || adjacent.status.partOfLoop) {
                    continue
                }
                if (!adjacent.status.partOfLoop) {
                    //println("Mark a tile not part of the loop as outside: ${adjacent.y},${adjacent.x} via $y,$x")
                    arrayPoints[adjacent.y][adjacent.x].dug = true
                    tilesMarkedAsOpen++
                    continue
                }
            }
        }
    }

    return tilesMarkedAsOpen
}

private fun determineAdjacents(y: Int, x: Int, grid: Array<Array<Status>>, row: Array<Status>): MutableList<AdjacentPointClosedTileDetector> {
    val nextAdjacents = mutableListOf<AdjacentPointClosedTileDetector>()

    if (y != 0) {
        nextAdjacents.add(AdjacentPointClosedTileDetector(y-1, x, grid[y - 1][x]))
    }
    if (y != grid.size-1) {
        nextAdjacents.add(AdjacentPointClosedTileDetector(y+1, x, grid[y + 1][x]))
    }
    if (x != 0) {
        nextAdjacents.add(AdjacentPointClosedTileDetector(y, x-1, grid[y][x - 1]))
    }
    if (x != row.size-1) {
        nextAdjacents.add(AdjacentPointClosedTileDetector(y, x+1, grid[y][x + 1]))
    }
    return nextAdjacents
}

private fun partOne (lines : List<String>): Long {

    val digOrders = lines.map{return@map it.split(" ") }.map{return@map DigOrder(
            Direction.getByULDR(it[0]),
            it[1].toInt()
        )}

    val maxSizeEast = digOrders.filter { o -> o.direction == Direction.EAST }.map { return@map it.steps }.reduce{acc: Int, i: Int -> acc+i}
    val maxSizeSouth = digOrders.filter { o -> o.direction == Direction.SOUTH }.map { return@map it.steps }.reduce{acc: Int, i: Int -> acc+i}
    var grid: Array<Array<Status>> = Array(2*maxSizeEast) { Array(2*maxSizeSouth) { Status(partOfLoop = false, dug = false) }}
    var yPositionDigger = maxSizeSouth
    var xPositionDigger = maxSizeEast
    grid[yPositionDigger][xPositionDigger].markAsPartOfLoop()
    for (order in digOrders) {
        println("$yPositionDigger - $xPositionDigger")
        println("${order.direction} - ${order.steps}")
        if (order.direction == Direction.NORTH) {
            for (step in 0..< order.steps) {
                grid[yPositionDigger-step][xPositionDigger].markAsPartOfLoop()
            }
            yPositionDigger-=order.steps
        }
        if (order.direction == Direction.EAST) {
            for (step in 0..< order.steps) {
                grid[yPositionDigger][xPositionDigger+step].markAsPartOfLoop()
            }
            xPositionDigger+=order.steps
        }
        if (order.direction == Direction.SOUTH) {
            for (step in 0..< order.steps) {
                grid[yPositionDigger+step][xPositionDigger].markAsPartOfLoop()
            }
            yPositionDigger+=order.steps
        }
        if (order.direction == Direction.WEST) {
            for (step in 0..< order.steps) {
                grid[yPositionDigger][xPositionDigger-step].markAsPartOfLoop()
            }
            xPositionDigger-=order.steps
        }
    }
    // Mark the tile to the right of the starting position as dug.
    grid[yPositionDigger+1][xPositionDigger+1].dug = true
    //print2DArray(grid)

    grid = grid.filter { it.any { s -> s.partOfLoop } }.toTypedArray()
    grid = transpose(transpose(grid).filter { it.any { s -> s.partOfLoop } }.toTypedArray())

    print2DArray(grid)

    var iteration = 0
    do {
        val newTiles :Int = findNewTiles(grid)
        iteration++
        println("$iteration - $newTiles")
    } while (newTiles != 0)
    print2DArray(grid)

    return grid.map { return@map it.filter { status -> status.dug || status.partOfLoop }.count() }.reduce{acc: Int, i: Int -> acc + i}.toLong()
}

private fun partTwo (lines : List<String>): Long {

    val digOrders = lines.map{return@map it.split(" ") }.map{return@map DigOrder(
        Direction.getByNumber(it[2].substring(7, 8)),
        it[2].substring(2,7).toInt(16)
    )}

    val maxSizeEast = digOrders.filter { o -> o.direction == Direction.EAST }.map { return@map it.steps }.reduce{acc: Int, i: Int -> acc+i}
    val maxSizeSouth = digOrders.filter { o -> o.direction == Direction.SOUTH }.map { return@map it.steps }.reduce{acc: Int, i: Int -> acc+i}

    return 0L
}

fun main(args : Array<String>) {

    val lines = readLines("test")
    //println(partOne(lines))
    println(partTwo(lines))

}

