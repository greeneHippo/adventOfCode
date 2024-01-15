package nl.groen.day22

import nl.groen.Position
import nl.groen.readLines

data class Vector(var start: Position, var end : Position) {

}

private fun partOne (lines : List<String>): Long {

    val blocks = lines.map { it.split("~") }.map {
        val splitFirst = it[0].split(",").map { it.toInt() }
        val splitSecond = it[1].split(",").map { it.toInt() }
        return@map Vector(Position(splitFirst[0],splitFirst[1],splitFirst[2]), Position(splitSecond[0],splitSecond[1],splitSecond[2]))}.toMutableList()

    val maxZ = blocks.maxOf { it.end.z }
    var iteration = 0
    do {
        var noFallingBlocks = 0
        blocks.forEachIndexed { index, vector ->
            val start = vector.start
            val end = vector.end
            if (start.z == 1) {
                return@forEachIndexed
            }

            val blocksToCheck = mutableListOf<Position>()
            if (start.x != end.x) {
                blocksToCheck.addAll(IntRange(start.x, end.x).map { Position(it, start.y, start.z-1) })
            } else if (start.y != end.y) {
                blocksToCheck.addAll(IntRange(start.y, end.y).map { Position(start.x, it, start.z-1) })
            } else {
                blocksToCheck.add(Position(start.x, start.y, start.z-1))
            }

            // Check that none of the blocks lies below any of the lowest level blocks
            if (blocks.all {
                    blocksToCheck.none { blockToCheck ->
                        IntRange(it.start.x, it.end.x).contains(blockToCheck.x) && IntRange(it.start.y, it.end.y).contains(blockToCheck.y) && IntRange(it.start.z, it.end.z).contains(blockToCheck.z)
                    }
                }) {
                noFallingBlocks++
//                println("$vector can fall down to ${blocksToCheck.minOf { it.z }}")
                blocks[index] = Vector(Position(vector.start.x, vector.start.y, vector.start.z-1), Position(vector.end.x, vector.end.y, vector.end.z-1))
            }

        }

        println("$iteration - $noFallingBlocks")
        iteration++
    } while (iteration <= maxZ)

    blocks.sortBy { it.start.z }

    val supportsBlocks : List<List<Int>> = blocks.map {vector ->

        val start = vector.start
        val end = vector.end
        if (start.z == maxZ) {
            return@map mutableListOf()
        }

        val blocksToCheck = mutableListOf<Position>()
        if (start.x != end.x) {
            blocksToCheck.addAll(IntRange(start.x, end.x).map { Position(it, start.y, start.z+1) })
        }
        if (start.y != end.y) {
            blocksToCheck.addAll(IntRange(start.y, end.y).map { Position(start.x, it, start.z+1) })
        }
        if (start.z != end.z) {
            blocksToCheck.add(Position(end.x, end.y, end.z+1))
        }

        // Check which vectors lies above any of the vector blocks on the highest level
        val supportsBlocks : List<Int> = blocks.mapIndexedNotNull{ index, block ->

            if (blocksToCheck.none {
                    IntRange(block.start.x, block.end.x).contains(it.x) && IntRange(block.start.y, block.end.y).contains(it.y) && IntRange(block.start.z, block.end.z).contains(it.z)
            }) {
                return@mapIndexedNotNull null
            } else {
                return@mapIndexedNotNull index
            }
            }.toMutableList()

        return@map supportsBlocks
    }

    val result = supportsBlocks.mapIndexedNotNull { indexSupport, ints ->

        if (
            ints.isEmpty() ||
            ints.all { supportsBlocks.count{b -> b.contains(it)} > 1 }
        ) {
            return@mapIndexedNotNull indexSupport
        } else {
            return@mapIndexedNotNull null
        }
    }

//    for (x in 0..blocks.maxOf { it.end.x } ) {
//        for (y in 0..blocks.maxOf { it.end.y } ) {
//            for (z in 0..blocks.maxOf { it.end.z } ) {
//                if (blocks.count { IntRange(it.start.x, it.end.x).contains(x) &&
//                            IntRange(it.start.y, it.end.y).contains(y) &&
//                            IntRange(it.start.z, it.end.z).contains(z) } > 1) {
//                    throw Error("not valid")
//                }
//            }
//        }
//    }

    return result.count().toLong()
}

private fun partTwo (lines : List<String>): Long {

    return 0L
}

fun main(args : Array<String>) {

    val lines = readLines("day22")
    println(partOne(lines))
    println(partTwo(lines))

}

