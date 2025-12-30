package nl.groen.year2023.day14

import nl.groen.print2DArray
import nl.groen.readInput
import nl.groen.revertRows
import nl.groen.transpose

private fun partOne (lines : List<String>): Long {

    var field = lines.map { return@map it.toCharArray().map { symbol -> return@map symbol.uppercase() }.toTypedArray() }.toTypedArray()
    print2DArray(field)

    do {
        val result = tilt(field, Direction.NORTH)
        field = result.field
        print2DArray(field)
        println(result.numberOfSwaps)
    } while (result.numberOfSwaps != 0)

    var result = field.mapIndexed { i, string -> return@mapIndexed string.filter { it == "O" }.count() * (field.size-i)}

    return result.reduce{sum, element -> sum+element}.toLong()
}

fun tilt(field: Array<Array<String>>, direction: Direction) : TiltResult {

    var result = field.copyOf()
    var numberOfSwaps = 0

    when (direction) {
        Direction.NORTH -> result = transpose(result)
        Direction.WEST -> result = result
        Direction.SOUTH -> result = revertRows(transpose(result))
        Direction.EAST -> result = revertRows(result)
    }

    val pair = tiltTransposedField(result, numberOfSwaps)
    numberOfSwaps = pair.first
    result = pair.second

    // Transpose back if necessary
    when (direction) {
        Direction.NORTH -> result = transpose(result)
        Direction.WEST -> result = result
        Direction.SOUTH -> result = transpose(revertRows(result))
        Direction.EAST -> result = revertRows(result)
    }

    return TiltResult(result, numberOfSwaps)
}

private fun tiltOneDirectionTillDone(field: Array<Array<String>>, direction: Direction) : Array<Array<String>> {
    var result = TiltResult(field, 0)
    do {
        result = tilt(result.field, direction)
    } while (result.numberOfSwaps != 0)
    return result.field
}

private fun tiltTransposedField(result: Array<Array<String>>, numberOfSwaps: Int): Pair<Int, Array<Array<String>>> {
    var result1 = result
    var numberOfSwaps1 = numberOfSwaps
    result1 = result1.map {

        it.forEachIndexed { i, s ->
            if (s != ".") {
                return@forEachIndexed
            }
            if (i == it.size - 1) {
                return@forEachIndexed
            }
            if (it[i + 1] == "O") {
                it[i] = "O"
                it[i + 1] = "."
                numberOfSwaps1++
            }
        }
        return@map it
    }.toTypedArray()
    return Pair(numberOfSwaps1, result1)
}

private fun partTwo (lines : List<String>): Long {

    var field = lines.map { return@map it.toCharArray().map { symbol -> return@map symbol.uppercase() }.toTypedArray() }.toTypedArray()
    print2DArray(field)
    var iteration = 0
    do {
        var result = tiltOneDirectionTillDone(field, Direction.NORTH)
        //print2DArray(result)
        result = tiltOneDirectionTillDone(result, Direction.WEST)
        //print2DArray(result)
        result = tiltOneDirectionTillDone(result, Direction.SOUTH)
        //print2DArray(result)
        result = tiltOneDirectionTillDone(result, Direction.EAST)
        field = result
        //print2DArray(field)
        iteration++
        println("$iteration - ${field.mapIndexed { i, string -> return@mapIndexed string.filter { it == "O" }.count() * (field.size-i)}.reduce{sum, element -> sum+element}.toLong()}")
    } while (iteration < 1000000000)


    var result = field.mapIndexed { i, string -> return@mapIndexed string.filter { it == "O" }.count() * (field.size-i)}
    return result.reduce{sum, element -> sum+element}.toLong()
}


fun main() {

    val lines = readInput("day14")
    //println(partOne(lines))
    println(partTwo(lines))

}

enum class Direction() {
    NORTH, EAST, SOUTH, WEST
}

data class TiltResult(var field: Array<Array<String>>, var numberOfSwaps: Int)

