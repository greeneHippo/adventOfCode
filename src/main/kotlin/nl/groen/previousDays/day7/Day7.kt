package nl.groen.previousDays.day7

import nl.groen.readLines
import org.apache.commons.lang3.StringUtils
import java.util.Comparator

private fun partOne (lines : List<String>): Long {

    var hands = lines.map { it ->
        var splittedString = StringUtils.splitByWholeSeparator(it, " ")
        return@map HandBasic(splittedString[1].toInt(), splittedString[0].toList().map { it.uppercase() })
    }

    hands.forEach{it.type = it.determineType5Cards()}

    var count = 1
    for (type in Type.entries) {
        count = fillRankForType(hands, type, count)
    }

    return hands.map { it.rank * it.bid.toLong() }.reduce{sum, element -> sum + element}
}

private fun partTwo (lines : List<String>): Long {

    var hands = lines.map { it ->
        var splittedString = StringUtils.splitByWholeSeparator(it, " ")
        return@map HandJoker(splittedString[1].toInt(), splittedString[0].toList().map { it.uppercase() })
    }

    hands.forEach{it.type = it.determineType5Cards()}

    var count = 1
    for (type in Type.entries) {
        count = fillRankForType(hands, type, count)
    }

    return hands.map { it.rank * it.bid.toLong() }.reduce{sum, element -> sum + element}
}

private fun fillRankForType(hands: List<HandBasic>, type: Type, startRank: Int) :Int {
    val filteredHands = hands.filter { it.type == type}.sortedWith(Comparator.comparing(HandBasic::order))
    filteredHands.forEachIndexed { i, hand -> hand.rank = i + startRank}
    println("$type - ${filteredHands.size} van $startRank tot ${startRank + filteredHands.size}")
    return startRank + filteredHands.size
}

fun main(args : Array<String>) {

    val lines = readLines("day7")
    //println(partOne(lines))
    println(partTwo(lines))

}

