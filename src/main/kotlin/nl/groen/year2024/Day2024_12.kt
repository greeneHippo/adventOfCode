package nl.groen.year2024

import nl.groen.*
import java.util.*

fun main() {

    data class Plant(val symbol:String, var plotId: Int? = null)
    data class State(val position: Position, val directionsFences :List<Direction> = listOf())
    data class Fence(val coordinate1 :Position, val coordinate2 :Position, val gardenCoordinate :Position, val direction: Direction, var visited: Boolean = false)

    fun fillFenceMap(
        direction: Direction,
        fenceMap: MutableMap<Int, MutableList<Fence>>,
        id: Int?,
        position: Position
    ) {
        when (direction) {
            Direction.NORTH -> fenceMap[id]!!.add(
                Fence(
                    Position(position.x, position.y),
                    Position(position.x + 1, position.y),
                    position,
                    direction
                )
            )

            Direction.EAST -> fenceMap[id]!!.add(
                Fence(
                    Position(position.x + 1, position.y + 1),
                    Position(position.x + 1, position.y),
                    position,
                    direction
                )
            )

            Direction.SOUTH -> fenceMap[id]!!.add(
                Fence(
                    Position(position.x + 1, position.y + 1),
                    Position(position.x, position.y + 1),
                    position,
                    direction
                )
            )

            Direction.WEST -> fenceMap[id]!!.add(
                Fence(
                    Position(position.x, position.y),
                    Position(position.x, position.y + 1),
                    position,
                    direction
                )
            )
            else -> throw IllegalArgumentException("Unknown direction")
        }
    }

    fun MutableList<Fence>.getNextFence(firstCoordinate: Position, previousCoordinate: Position, currentFence: Fence): Fence? {
        val nextFences = this.filter { it.coordinate1 == firstCoordinate || it.coordinate2 == firstCoordinate }
            .filter { it.coordinate1 != previousCoordinate && it.coordinate2 != previousCoordinate }
            .filter { !it.visited }

        if (nextFences.isEmpty()) return null

        val nextFence: Fence = if (nextFences.size > 1) {
            val filtered = nextFences.filter { next -> next.gardenCoordinate == currentFence.gardenCoordinate }
            filtered.first()
        } else {
            nextFences.first()
        }
        return nextFence
    }

    fun MutableList<Fence>.calculateSides() :Long {

        var currentFence = this.first()
        var firstCoordinate = currentFence.coordinate1
        var previousCoordinate = currentFence.coordinate2
        var numberSides = 0L

        while (this.any { !it.visited }) {

            var nextFence = getNextFence(firstCoordinate, previousCoordinate, currentFence)

            // Continue with next separate loop of fences
            if (nextFence == null) {
                currentFence = this.filter { !it.visited }.first()
                firstCoordinate = currentFence.coordinate1
                previousCoordinate = currentFence.coordinate2
                nextFence = getNextFence(firstCoordinate, previousCoordinate, currentFence)
                if (nextFence == null) error("Not possible")
            }

            nextFence.visited = true

            if (nextFence.direction != currentFence.direction && !Direction.isOppositeDirection(nextFence.direction, currentFence.direction)) {
                numberSides +=1
            }

            currentFence = nextFence
            previousCoordinate = firstCoordinate
            firstCoordinate = mutableListOf(currentFence.coordinate1, currentFence.coordinate2).first { it != firstCoordinate }
        }

        return numberSides
    }

    fun Map.Entry<Position, Plant>.calculatePlotCost(area: MutableMap<Position, Plant>, neighbourGrid: MutableMap<Position, MutableList<MoveAction>>,
        fenceMap: MutableMap<Int, MutableList<Fence>>, withDiscount: Boolean): Long {

        val startPosition = this.key
        val symbol = this.value.symbol
        val id = this.value.plotId

        val queue = PriorityQueue { state1: State, state2: State -> state1.position.x.compareTo(state2.position.x) }
        queue.add(State(startPosition))

        var areaCount = 1L
        var fenceCount = 0L
        var corners = 0L
        fenceMap[id!!] = mutableListOf()

        while (queue.isNotEmpty()) {
            val state = queue.poll()
            val directionsOfFences :MutableList<Direction> = mutableListOf()
            val neighbours = neighbourGrid[state.position]!!

            // Check for the edges of the garden
            for (direction in Direction.entries) {
                if (neighbours.none { it.direction == direction }) {
                    fillFenceMap(direction, fenceMap, id, state.position)
                    directionsOfFences.add(direction)
                    fenceCount += 1
                }
            }

            // Check for neighbours in the garden
            val listNeighboursToSearch :MutableList<Position> = mutableListOf()
            for (neighbour in neighbours) {
                if (area[neighbour.position]!!.symbol == symbol && area[neighbour.position]!!.plotId == null) {
                    area[neighbour.position]!!.plotId = id
                    areaCount +=1
                    listNeighboursToSearch.add(neighbour.position)
                } else if (area[neighbour.position]!!.symbol != symbol) {
                    fillFenceMap(neighbour.direction, fenceMap, id, state.position)
                    directionsOfFences.add(neighbour.direction)
                    fenceCount += 1
                }
            }

            for (newNeighbours in listNeighboursToSearch) {
                queue.add(State(newNeighbours, directionsOfFences))
            }
            corners += directionsOfFences.sumOf { dir1 -> directionsOfFences.count { dir2 -> Direction.isNextDirection(dir1, dir2) }.toLong()} / 2L
        }

        return if (withDiscount) {
            areaCount * fenceMap[id]!!.calculateSides()
        } else {
            areaCount * fenceCount
        }

    }

    fun calculateTotalCost (input : List<String>, withDiscount: Boolean): Long {

        val area: MutableMap<Position, Plant> = mutableMapOf()
        val neighbourGrid: MutableMap<Position, MutableList<MoveAction>> = mutableMapOf()
        parseGrid(input, area, neighbourGrid) { s: String ->
            return@parseGrid Plant(s)
        }

        val gardenPlots: MutableMap<Int, Long> = mutableMapOf()
        val fenceMap: MutableMap<Int, MutableList<Fence>> = mutableMapOf()

        var id = 0
        while (area.values.any { it.plotId == null }) {

            val entry = area.filter { it.value.plotId == null }.firstNotNullOf { it }
            area[entry.key]!!.plotId = id++
            gardenPlots[id] = entry.calculatePlotCost(area, neighbourGrid, fenceMap, withDiscount)
        }


        return gardenPlots.values.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day12_test")
    val input = readInput("2024","Day12")

    check(calculateTotalCost(testInput, false) == 1930L)
    calculateTotalCost(input, false).println()

    check(calculateTotalCost(testInput, true) == 1206L)
    calculateTotalCost(input, true).println()

}

