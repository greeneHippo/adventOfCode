package nl.groen.day18

import nl.groen.Direction
import nl.groen.print2DArray
import nl.groen.readLines
import nl.groen.transpose
import org.apache.commons.lang3.tuple.ImmutableTriple
import kotlin.math.abs

data class DigOrder(var direction: Direction, var steps: Long)
data class Neighbours(var previous: DigOrder, var next: DigOrder)

class Status(var partOfLoop: Boolean, var dug: Boolean, var endOfOrder: Boolean ) : Any() {
    fun markAsPartOfLoop() {
        partOfLoop = true
        dug = false
    }

    override fun toString() : String {
        return if (endOfOrder) {
            "O"
        } else if (partOfLoop) {
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
            it[1].toLong()
        )}

    var grid: Array<Array<Status>> = printOrders(digOrders)

    var iteration = 0
    do {
        val newTiles :Int = findNewTiles(grid)
        iteration++
        //println("$iteration - $newTiles")
    } while (newTiles != 0)
    //print2DArray(grid)

    return grid.map { return@map it.filter { status -> status.dug || status.partOfLoop }.count() }.reduce{acc: Int, i: Int -> acc + i}.toLong()
}

private fun printOrders(digOrderNeighboursList: MutableMap<DigOrder, Neighbours>) {

    val digOrders: Array<DigOrder?> = Array(digOrderNeighboursList.values.size){null}

    var order: DigOrder? = null

    for (i in digOrderNeighboursList.values.indices) {
        order = if (i == 0) {
            digOrderNeighboursList.keys.first()
        } else {
            digOrderNeighboursList[order]!!.next
        }
        digOrders[i] = order
    }

    val digOrderList = digOrders.toList()
    printOrders(digOrderList.map { DigOrder(it!!.direction, it.steps / digOrderList.minOf { d -> d!!.steps * 5 }) })

}

private fun printOrders(digOrders: List<DigOrder>): Array<Array<Status>> {
    val maxSizeEast = digOrders.filter { o -> o.direction == Direction.EAST }.sumOf { it.steps }.toInt()
    val maxSizeSouth = digOrders.filter { o -> o.direction == Direction.SOUTH }.sumOf { it.steps }.toInt()
    var grid: Array<Array<Status>> = Array(2 * maxSizeSouth + 2) { Array(2 * maxSizeEast + 2) { Status(partOfLoop = false, dug = false, endOfOrder = false) } }
    var yPositionDigger = maxSizeSouth
    var xPositionDigger = maxSizeEast
    grid[yPositionDigger][xPositionDigger].markAsPartOfLoop()
    for (order in digOrders) {
        //println("$yPositionDigger - $xPositionDigger")
        //println("${order.direction} - ${order.steps}")
        val maxSteps = order.steps.toInt()
        if (order.direction == Direction.NORTH) {
            for (step in 1..<maxSteps+1) {
                grid[yPositionDigger - step][xPositionDigger].markAsPartOfLoop()
                if (step >= order.steps-1) {
                    grid[yPositionDigger - step][xPositionDigger].endOfOrder = true
                }
            }
            yPositionDigger -= maxSteps
        }
        if (order.direction == Direction.EAST) {
            for (step in 1..<maxSteps+1) {
                grid[yPositionDigger][xPositionDigger + step].markAsPartOfLoop()
                if (step >= maxSteps-1) {
                    grid[yPositionDigger][xPositionDigger + step].endOfOrder = true
                }
            }
            xPositionDigger += maxSteps
        }
        if (order.direction == Direction.SOUTH) {
            for (step in 1..<maxSteps+1) {
                grid[yPositionDigger + step][xPositionDigger].markAsPartOfLoop()
                if (step >= maxSteps-1) {
                    grid[yPositionDigger + step][xPositionDigger].endOfOrder = true
                }
            }
            yPositionDigger += maxSteps
        }
        if (order.direction == Direction.WEST) {
            for (step in 1..<maxSteps+1) {
                grid[yPositionDigger][xPositionDigger - step].markAsPartOfLoop()
                if (step >= maxSteps-1) {
                    grid[yPositionDigger][xPositionDigger - step].endOfOrder = true
                }
            }
            xPositionDigger -= maxSteps
        }
    }
    // Mark the tile to the right of the starting position as dug.
    grid[yPositionDigger + 1][xPositionDigger + 1].dug = true
    grid = grid.filter { it.any { s -> s.partOfLoop } }.toTypedArray()
    grid = transpose(transpose(grid).filter { it.any { s -> s.partOfLoop } }.toTypedArray())
    print2DArray(grid)

    return grid
}

private fun partTwo (lines : List<String>): Long {

    val digOrders = lines.map{return@map it.split(" ") }.map{return@map DigOrder(
        Direction.getByNumber(it[2].substring(7, 8)),
        it[2].substring(2,7).toLong(16)
    )}

    val digOrderNeighbours :MutableMap<DigOrder, Neighbours> = digOrders
        .mapIndexed { index, digOrder -> ImmutableTriple(digOrder, digOrders[((digOrders.size+index)-1)%digOrders.size], digOrders[(index+1)%digOrders.size])}
        .associate { it.left to Neighbours(it.middle, it.right)}.toMutableMap()

    val borderSteps = digOrders.sumOf { it.steps }
    //digOrders.forEach { println("${it.direction} = ${it.steps}") }
    //printOrders(digOrders.map { DigOrder(it.direction, it.steps / minimum) })
    //printOrders(digOrderNeighbours)
    printOrders(digOrderNeighbours)
    validate(digOrderNeighbours)

    var size = 0L
    var i = 0
    do  {

        val blobs = digOrderNeighbours.filter {
            (4 + it.key.direction.symbolNumber.toInt() - it.value.previous.direction.symbolNumber.toInt()) % 4 == 1  &&
            (4 + it.value.next.direction.symbolNumber.toInt() - it.key.direction.symbolNumber.toInt()) % 4 == 1
        }
        println("Totaal: ${digOrderNeighbours.size} - ${blobs.size}")

        run breaking@ {
            blobs.toList().forEach {

                val previousNeighbours = digOrderNeighbours[it.second.previous]!!
                val nextNeighbours = digOrderNeighbours[it.second.next]!!

                val previous = it.second.previous
                val current = it.first
                val next = it.second.next

                val difference = previous.steps - next.steps

                if (difference < 0) {
                    println("Merge current and previous into previous-previous")

                    // Merge current and previous into previous-previous
                    val previous2 = previousNeighbours.previous
                    val previous3 = digOrderNeighbours[previous2]!!.previous

                    if (previous2.direction != current.direction) {
                        return@forEach
                    }
                    size += previous.steps*(current.steps-1)

                    val newCurrentOrder = DigOrder(previous2.direction, previous2.steps + current.steps)
                    // Reduce steps of next
                    val newNextOrder = DigOrder(next.direction, abs(difference))

                    digOrderNeighbours[previous3] = Neighbours(digOrderNeighbours[previous3]!!.previous, newCurrentOrder)
                    digOrderNeighbours[newCurrentOrder] = Neighbours(previous3, newNextOrder)
                    digOrderNeighbours[newNextOrder] = Neighbours(newCurrentOrder, nextNeighbours.next)
                    digOrderNeighbours[nextNeighbours.next] = Neighbours(newNextOrder, digOrderNeighbours[nextNeighbours.next]!!.next)

                    digOrderNeighbours.remove(previous2)
                    digOrderNeighbours.remove(previous)
                    digOrderNeighbours.remove(current)
                    digOrderNeighbours.remove(next)

                } else if (difference == 0L) {
                    println("Merge previous & next into one order")

                    val previous2 = previousNeighbours.previous
                    val next2 = nextNeighbours.next
                    if (previous2.direction != current.direction || next2.direction != current.direction) {
                        return@forEach
                    }

                    // Merge previous & next into one order
                    size += previous.steps*(current.steps-1)

                    val previous3 = digOrderNeighbours[previous2]!!.previous
                    val next3 = digOrderNeighbours[next2]!!.next

                    val newOrder = DigOrder(previous2.direction, previous2.steps + current.steps + next2.steps)

                    digOrderNeighbours[previous3] = Neighbours(digOrderNeighbours[previous3]!!.previous, newOrder)
                    digOrderNeighbours[newOrder] = Neighbours(previous3, next3)
                    digOrderNeighbours[next3] = Neighbours(newOrder, digOrderNeighbours[next3]!!.next)

                    digOrderNeighbours.remove(previous2)
                    digOrderNeighbours.remove(previous)
                    digOrderNeighbours.remove(current)
                    digOrderNeighbours.remove(next)
                    digOrderNeighbours.remove(next2)
                } else if (difference > 0) {
                    println("Merge current and next into next-next")

                    // Merge current and next into next-next
                    val next2 = nextNeighbours.next
                    val next3 = digOrderNeighbours[next2]!!.next

                    if (next2.direction != current.direction) {
                        return@forEach
                    }
                    size += next.steps*(current.steps-1)

                    val newCurrentOrder = DigOrder(next2.direction, next2.steps + current.steps)
                    // Reduce steps of previous
                    val newPreviousOrder = DigOrder(previous.direction, abs(difference))

                    digOrderNeighbours[previousNeighbours.previous] = Neighbours(digOrderNeighbours[previousNeighbours.previous]!!.previous, newPreviousOrder)
                    digOrderNeighbours[newPreviousOrder] = Neighbours(previousNeighbours.previous, newCurrentOrder)
                    digOrderNeighbours[newCurrentOrder] = Neighbours(newPreviousOrder, next3)
                    digOrderNeighbours[next3] = Neighbours(newCurrentOrder, digOrderNeighbours[next3]!!.next)

                    digOrderNeighbours.remove(previous)
                    digOrderNeighbours.remove(current)
                    digOrderNeighbours.remove(next)
                    digOrderNeighbours.remove(next2)

                }
                return@breaking
                }
        }
        println(size)
        validate(digOrderNeighbours)
        i++
        println(i)

    }  while (digOrderNeighbours.size > 4 && i < 500)

    printOrders(digOrderNeighbours)

    val east = digOrderNeighbours.keys.filter { o -> o.direction == Direction.EAST }.sumOf { it.steps }
    val north = digOrderNeighbours.keys.filter { o -> o.direction == Direction.NORTH }.sumOf { it.steps }

    size += (east-1) * (north-1)
    size += borderSteps
    return size
}

fun validate(digOrderNeighbours: MutableMap<DigOrder, Neighbours>) {

    val east = digOrderNeighbours.keys.filter { o -> o.direction == Direction.EAST }.sumOf { it.steps }
    val west = digOrderNeighbours.keys.filter { o -> o.direction == Direction.WEST }.sumOf { it.steps }
    val north = digOrderNeighbours.keys.filter { o -> o.direction == Direction.NORTH }.sumOf { it.steps }
    val south = digOrderNeighbours.keys.filter { o -> o.direction == Direction.SOUTH }.sumOf { it.steps }

    if (east != west || north != south) {
        throw Error("invalid orders east: ${east}, west: ${west}, north: ${north}, south: ${south} ")
    }
}

fun main(args : Array<String>) {

    val lines = readLines("test_CW")
//    println(partOne(lines))
    println(partTwo(lines))

}

