package nl.groen.previousDays.day15

import nl.groen.readLines
import org.apache.commons.lang3.StringUtils

private fun partOne (lines : List<String>): Long {

    var words = StringUtils.splitByWholeSeparator(lines[0],",")
    var hashResult = words.map { return@map determineHash(it) }.toList()

    return hashResult.reduce{sum, element -> sum+element}.toLong()
}

fun determineHash(input: String): Int {

    var result = 0
    for (char in input) {
        result = determineHash(char, result)
    }
    return result
}

fun determineHash(input: Char, currentHash: Int): Int {

    var output = currentHash
    output += input.code
    output *= 17
    return output % 256
}

private fun partTwo (lines : List<String>): Long {

    var words = StringUtils.splitByWholeSeparator(lines[0],",")
    var lensesOperations = words.map {
        var label = StringUtils.splitByWholeSeparator(StringUtils.splitByWholeSeparator(it,"-")[0], "=")[0]

        return@map LensOperation(
            Lens(
                label,
                determineHash(label),
                determineFocalLength(it)
            ),
            determineOperation(it)
        )
    }.toList()

    var boxes :Map<Int, Box> = IntRange(0,255).associateBy({it} , { Box(mutableMapOf()) } )

    for (operation in lensesOperations) {

        val lens = operation.lens
        val lensesInBox = boxes[operation.lens.hash]?.lenses!!
        if (operation.operation == "=") {
            if (lensesInBox[lens.label] == null) {
                lensesInBox[lens.label] = lens.focalLength
            } else {
                lensesInBox[lens.label] = lens.focalLength
            }

        }

        if (operation.operation == "-") {
            if (lensesInBox[lens.label] != null) {
                lensesInBox.remove(lens.label)
            }
        }
    }

    var result = boxes.flatMap {

        val lensesInBox = it.value.lenses.map { lensInBox ->
            return@map lensInBox.value
        }.toList()

        return@flatMap lensesInBox.mapIndexed { index, focalLength -> (index+1) * (it.key+1) * (focalLength)}
    }
    return result.reduce { acc, i -> acc+i }.toLong()
}

private fun determineOperation(it: String) :String {
    return if (it.contains("=")) {
        "="
    } else {
        "-"
    }
}

fun determineFocalLength(it: String): Int {
    return if (determineOperation(it) == "-") {
        0
    } else {
        it.last().uppercase().toInt()
    }
}

fun main(args : Array<String>) {

    val lines = readLines("Day15")
    println(partOne(lines))
    println(partTwo(lines))

}

class Box(var lenses :MutableMap<String, Int>)
class Lens(var label :String, val hash: Int, var focalLength: Int)
class LensOperation(var lens : Lens, var operation: String)
