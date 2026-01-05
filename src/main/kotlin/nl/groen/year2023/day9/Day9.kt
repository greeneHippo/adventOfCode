package nl.groen.year2023.day9

import nl.groen.readInput

private fun partOne (lines : List<String>): Long {

    return lines.map { return@map determineNextValue(it, false) }.reduce{sum, element -> sum + element}
}

fun determineNextValue(it: String, reverse: Boolean): Long {

    var sequence = it.split(" ").map { it.toInt() }.toMutableList()
    if (reverse) {
        sequence = sequence.reversed().toMutableList()
    }
    val differenceLastElement = mutableListOf<Int>()
    val lastNumber = sequence.last()
    var i = 0
    do {
        //println("$i - $sequence")
        var sequenceNextLayer: MutableList<Int> = determineSequenceNextLayer(sequence, reverse)
        differenceLastElement.add(i, sequenceNextLayer.last())
        i++
        var lastElement = if (!reverse) sequence.last() else sequence.first()
        sequence = sequenceNextLayer
    } while (lastElement != 0)

    var sumLastElements: Long
    if (!reverse) {
        sumLastElements = differenceLastElement.reversed().reduce { sum, element -> sum + element }.toLong()
    } else {
        sumLastElements = differenceLastElement.reversed().reduce { sum, element -> element - sum}.toLong()
    }
    //println("$sumLastElements $differenceLastElement")
    var firstElement = if (!reverse) lastNumber + sumLastElements else lastNumber - sumLastElements
    println("$firstElement $it")
    return firstElement
}

fun determineSequenceNextLayer(input: MutableList<Int>, reverse: Boolean): MutableList<Int> {
    return input.mapIndexedNotNull { i, value -> return@mapIndexedNotNull calculateDifference(input, i, value, reverse) }.toMutableList()
}

private fun calculateDifference(input: MutableList<Int>, i: Int, value: Int, reverse: Boolean): Int? {
    return if (input.size == i+1) {
        null
    } else {
        if (!reverse) input[i+1] - value
        else value - input[i+1]
    }
}

private fun partTwo (lines : List<String>): Long {

    return lines.map { return@map determineNextValue(it, true) }.reduce{sum, element -> sum + element}
}

fun main(args : Array<String>) {

    val lines = readInput("day9")
    //println(partOne(lines))
    println(partTwo(lines))

}

