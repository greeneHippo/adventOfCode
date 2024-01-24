package nl.groen.year2023.day22

import nl.groen.Position
import nl.groen.readInput

data class Vector(val id : Int, var start: Position, var end : Position)

private fun solve (lines : List<String>, part2 : Boolean): Long {

    val blocks = lines.map { it.split("~") }.mapIndexed { index, it ->
        val splitFirst = it[0].split(",").map { s -> s.toInt() }
        val splitSecond = it[1].split(",").map { s -> s.toInt() }
        return@mapIndexed Vector(index, Position(splitFirst[0],splitFirst[1],splitFirst[2]), Position(splitSecond[0],splitSecond[1],splitSecond[2]))
    }.toMutableList()

    val maxZ = blocks.maxOf { it.end.z }
    var iteration = 0
    blocks.sortBy { it.start.z }
    do {
        var numberOfFallingBlocks = 0
        blocks.forEachIndexed { index, vector ->
            val start = vector.start
            val end = vector.end
            if (start.z == 1) {
                return@forEachIndexed
            }

            val blocksToCheck = mutableListOf<Position>()
            if (start.x != end.x) {
                assert(start.y == end.y)
                assert(start.z == end.z)
                blocksToCheck.addAll(IntRange(start.x, end.x).map { Position(it, start.y, start.z-1) })
            } else if (start.y != end.y) {
                assert(start.z == end.z)
                blocksToCheck.addAll(IntRange(start.y, end.y).map { Position(start.x, it, start.z-1) })
            } else {
                blocksToCheck.add(Position(start.x, start.y, start.z-1))
            }

            // Check that none of the blocks lies below any of the lowest level blocks
            if (blocksToCheck.none { blockToCheck ->
                    blocks.any {
                        IntRange(it.start.x, it.end.x).contains(blockToCheck.x) && IntRange(it.start.y, it.end.y).contains(blockToCheck.y) && IntRange(it.start.z, it.end.z).contains(blockToCheck.z)
                    }
                }) {
                numberOfFallingBlocks++
//                println("$vector can fall down to ${blocksToCheck.minOf { it.z }}")
                blocks[index] = Vector(
                    vector.id,
                    Position(vector.start.x, vector.start.y, vector.start.z-1),
                    Position(vector.end.x, vector.end.y, vector.end.z-1))
            }

        }

        println("$iteration - $numberOfFallingBlocks")
        iteration++
    } while (iteration <= maxZ)

    blocks.sortBy { it.start.z }

    val supportsBlocks : Map<Int, List<Int>> = blocks.associate {vector ->

        val start = vector.start
        val end = vector.end

        val blocksToCheck = mutableListOf<Position>()
        if (start.x != end.x) {
            assert(start.z == end.z)
            blocksToCheck.addAll(IntRange(start.x, end.x).map { Position(it, start.y, start.z+1) })
        } else if (start.y != end.y) {
            assert(start.z == end.z)
            blocksToCheck.addAll(IntRange(start.y, end.y).map { Position(start.x, it, start.z+1) })
        } else {
            blocksToCheck.add(Position(end.x, end.y, end.z+1))
        }


        // Check which vectors lies above any of the vector blocks on the highest level
        val supportsBlocks : List<Int> = blocks.mapIndexedNotNull{ index, block ->

            if (blocksToCheck.any {
                    IntRange(block.start.x, block.end.x).contains(it.x) && IntRange(block.start.y, block.end.y).contains(it.y) && IntRange(block.start.z, block.end.z).contains(it.z)
            }) {
                return@mapIndexedNotNull block.id
            } else {
                return@mapIndexedNotNull null
            }
            }.toMutableList()

        Pair(vector.id, supportsBlocks)
    }

    if (!part2) {

        val result = supportsBlocks.filter{

            it.value.isEmpty() ||
            it.value.all { supportsBlocks.count{b -> b.value.contains(it)} > 1 }
        }.toMutableMap()

        result.forEach{println(it.key)}

        return result.count().toLong()
    } else {

        val dependsOnBlock = supportsBlocks.keys.associateWith {
            val dependsOn = supportsBlocks.filterValues { values -> values.contains(it) }
            dependsOn.keys
        }

        return dependsOnBlock.keys.sumOf { findDependentBlocks(dependsOnBlock, it) }.toLong()
    }
}

fun findDependentBlocks(dependsOnBlock: Map<Int, Set<Int>>, index: Int): Int {

    val listFallenBricks :MutableSet<Int> = mutableSetOf(index)

    do {
        val newBlocks = dependsOnBlock
            .filter { it.value.isNotEmpty() && it.value.all { element -> listFallenBricks.contains(element) } }
            .map { it.key }
            .filter { !listFallenBricks.contains(it) }
        listFallenBricks.addAll(newBlocks)
    } while(newBlocks.isNotEmpty())

    println("$index - ${listFallenBricks.size-1}")
    return listFallenBricks.size-1

}

private fun partTwo (lines : List<String>): Long {

    return 0L
}

fun main(args : Array<String>) {

    val lines = readInput("Day22")
    println(solve(lines, true))

}

