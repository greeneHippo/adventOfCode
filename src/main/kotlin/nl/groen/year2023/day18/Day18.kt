package nl.groen.year2023.day18

import nl.groen.*
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
        )
    }

    var grid: Array<Array<Status>> = printOrders(digOrders)

    var iteration = 0
    do {
        val newTiles :Int = findNewTiles(grid)
        iteration++
        //println("$iteration - $newTiles")
    } while (newTiles != 0)
    print2DArray(grid)

    return grid.map { return@map it.filter { status -> status.dug || status.partOfLoop }.count() }.reduce{acc: Int, i: Int -> acc + i}.toLong()
}

private fun printOrders(digOrderNeighboursList: MutableMap<DigOrder, Neighbours>, reduceFactor: Int){

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
    printOrders(digOrderList.map { DigOrder(it!!.direction, it.steps / digOrderList.minOf { d -> d!!.steps * reduceFactor }) })

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
    grid[yPositionDigger + 1][xPositionDigger - 1].dug = true
    grid = grid.filter { it.any { s -> s.partOfLoop } }.toTypedArray()
    grid = transpose(transpose(grid).filter { it.any { s -> s.partOfLoop } }.toTypedArray())
    print2DArray(grid)

    return grid
}

data class Vector(val start: Position, val end : Position)

private fun partTwo_alternative (lines : List<String>): Long {

    val digOrders = lines.map{return@map it.split(" ") }.map{return@map DigOrder(
        Direction.getByNumber(it[2].substring(7, 8)),
        it[2].substring(2,7).toLong(16)
    )
    }

    val vectors = digOrders.map{ Vector(Position(0,0), Position(0,0)) }.toMutableList()
    var positionDigger = Position(0,0)

    for (i in digOrders.indices) {

        val order = digOrders[i]
        val oldPosition = positionDigger.copy()

        positionDigger = when (order.direction) {
            Direction.NORTH -> Position(positionDigger.x, positionDigger.y + order.steps.toInt())
            Direction.EAST -> Position(positionDigger.x + order.steps.toInt(), positionDigger.y)
            Direction.SOUTH -> Position(positionDigger.x, positionDigger.y - order.steps.toInt())
            Direction.WEST -> Position(positionDigger.x - order.steps.toInt(), positionDigger.y)
            else -> throw IllegalArgumentException("Unknown direction")
        }

        vectors[i] = Vector(oldPosition, positionDigger)
    }

    var result = 0L
    val verticalVectors = vectors.filter{it.start.x == it.end.x}
    val maxY = verticalVectors.maxOf { it.start.y}
    for (y in verticalVectors.minOf { it.start.y}..verticalVectors.maxOf { it.start.y}) {
        val matches = verticalVectors.filter{(y >= it.start.y && y <= it.end.y) || (y <= it.start.y && y >= it.end.y)}
        val sections = matches.map { it.start.x }.sorted()
        //println("$y van $maxY")

        for (i in sections.indices) {
            if (i.rem(2) == 1) {
                result += sections[i]-sections[i-1]+1
            }
        }
        println(result)
    }

    println(digOrders.sumOf { it.steps })
    return result
}

private fun partTwo (lines : List<String>): Long {

    val digOrders = lines.map{return@map it.split(" ") }.map{return@map DigOrder(
        Direction.getByNumber(it[2].substring(7, 8)),
        it[2].substring(2,7).toLong(16)
    )
    }

    val digOrderNeighbours :MutableMap<DigOrder, Neighbours> = digOrders
        .mapIndexed { index, digOrder -> ImmutableTriple(digOrder, digOrders[((digOrders.size+index)-1)%digOrders.size], digOrders[(index+1)%digOrders.size])}
        .associate { it.left to Neighbours(it.middle, it.right) }.toMutableMap()

    val borderSteps = digOrders.sumOf { it.steps }
    //digOrders.forEach { println("${it.direction} = ${it.steps}") }
    //printOrders(digOrders.map { DigOrder(it.direction, it.steps / minimum) })
    //printOrders(digOrderNeighbours,1)
    //printOrders(digOrderNeighbours, 50)
    validate(digOrderNeighbours, Long.MAX_VALUE, Long.MAX_VALUE)

    var size = 0L
    var i = 0
    do  {

        val blobs = digOrderNeighbours
//            .map {
//
//                it.key.differenceWithNext =
//                return@map it
//            }
            .filter {
                (
                    (4 + it.key.direction.symbolNumber.toInt() - it.value.previous.direction.symbolNumber.toInt()) % 4 == 1 &&
                    (4 + it.value.next.direction.symbolNumber.toInt() - it.key.direction.symbolNumber.toInt()) % 4 == 1
                ) ||
                (
                    (4 + it.key.direction.symbolNumber.toInt() - it.value.previous.direction.symbolNumber.toInt()) % 4 == 3 &&
                    (4 + it.value.next.direction.symbolNumber.toInt() - it.key.direction.symbolNumber.toInt()) % 4 == 3
                )
            }.toSortedMap { o1, o2 -> o1.steps.toInt().compareTo(o2.steps.toInt()) }


        println("Totaal: ${digOrderNeighbours.size} - ${blobs.size}")
        if (i % 20 == 0) {
            println(i)
            //printOrders(digOrderNeighbours, 25)
        }
        val previousSumSouth = digOrderNeighbours.keys.filter { o -> o.direction == Direction.SOUTH }.sumOf { it.steps }
        val previousSumEast = digOrderNeighbours.keys.filter { o -> o.direction == Direction.EAST }.sumOf { it.steps }
        run breaking@ {
            blobs.toList().forEach {

                val previousNeighbours = digOrderNeighbours[it.second.previous]!!
                val nextNeighbours = digOrderNeighbours[it.second.next]!!

                val previous = it.second.previous
                val current = it.first
                val next = it.second.next

                val difference = previous.steps - next.steps
                val sizeBefore = digOrderNeighbours.size
                val reduce = ((4 + current.direction.symbolNumber.toInt() - previous.direction.symbolNumber.toInt()) % 4) == 1
                if (difference < 0) {
                    //println("Merge current and previous into previous-previous")

                    // Merge current and previous into previous-previous
                    val previous2 = previousNeighbours.previous
                    val previous3 = digOrderNeighbours[previous2]!!.previous

                    if (previous2.direction != current.direction) {
                        return@forEach
                    }

                    if (reduce) {
                        size += previous.steps*(current.steps-1)
                    } else {
                        size -= previous.steps*(current.steps+1)
                    }

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

                    if (sizeBefore - digOrderNeighbours.size != 2) {
                        throw Error("invalid")
                    }

                } else if (difference == 0L) {
                    //println("Merge previous & next into one order")

                    val previous2 = previousNeighbours.previous
                    val next2 = nextNeighbours.next
                    if (previous2.direction != current.direction || next2.direction != current.direction) {
                        return@forEach
                    }

                    // Merge previous & next into one order
                    if (reduce) {
                        size += previous.steps*(current.steps-1)
                    } else {
                        size -= previous.steps*(current.steps+1)
                    }
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

                    if (sizeBefore - digOrderNeighbours.size != 4) {
                        throw Error("invalid")
                    }
                } else if (difference > 0) {
                    //println("Merge current and next into next-next")

                    // Merge current and next into next-next
                    val next2 = nextNeighbours.next
                    val next3 = digOrderNeighbours[next2]!!.next

                    if (next2.direction != current.direction) {
                        return@forEach
                    }
                    if (reduce) {
                        size += next.steps*(current.steps-1)
                    } else {
                        size -= next.steps * (current.steps + 1)
                    }
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

                    if (sizeBefore - digOrderNeighbours.size != 2) {
                        throw Error("invalid")
                    }
                }

                println(reduce)
                return@breaking
            }
        }
        validate(digOrderNeighbours, previousSumEast, previousSumSouth)
        i++
    }  while (digOrderNeighbours.size > 4 && i < 500)

    printOrders(digOrderNeighbours, 1)

    val east = digOrderNeighbours.keys.filter { o -> o.direction == Direction.EAST }.sumOf { it.steps }
    val north = digOrderNeighbours.keys.filter { o -> o.direction == Direction.NORTH }.sumOf { it.steps }

    size += (east-1) * (north-1)
    size += borderSteps
    return size
}

fun validate(digOrderNeighbours: MutableMap<DigOrder, Neighbours>, previousSumEast: Long, previousSumSouth: Long) {

    val east = digOrderNeighbours.keys.filter { o -> o.direction == Direction.EAST }.sumOf { it.steps }
    val west = digOrderNeighbours.keys.filter { o -> o.direction == Direction.WEST }.sumOf { it.steps }
    val north = digOrderNeighbours.keys.filter { o -> o.direction == Direction.NORTH }.sumOf { it.steps }
    val south = digOrderNeighbours.keys.filter { o -> o.direction == Direction.SOUTH }.sumOf { it.steps }

    if (east != west || north != south) {
        throw Error("invalid orders east: ${east}, west: ${west}, north: ${north}, south: ${south} ")
    }
    if (east > previousSumEast) {
        throw Error("Growth in east: ${east}, previousSumEast: ${previousSumEast}  ")
    }
    if (south > previousSumSouth) {
        throw Error("Growth in south: ${south}, previousSumSouth: ${previousSumSouth}  ")
    }
}

fun main(args : Array<String>) {

    val lines = readInput("day18")
    //println(partOne(lines))
    println(partTwo(lines))

}

