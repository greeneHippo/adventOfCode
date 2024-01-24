package nl.groen.year2023.day4

import nl.groen.readInput
import org.apache.commons.lang3.StringUtils
import java.util.stream.Collectors
import kotlin.math.pow

private fun partOne (lines : List<String>): Int {

    var cards = lines
        .stream()
        .map{
            return@map convertStringToCard(it)
        }
        .map{
            return@map determinePointsPartOne(it)
        }
        .collect(Collectors.toList())

    return cards.reduce{sum, element -> sum + element}
}

fun convertStringToCard(it: String): Card {

    var splitString = StringUtils.splitByWholeSeparator(it, ":")
    var cardNumber = StringUtils.trimToEmpty(StringUtils.removeStart(splitString[0], "Card")).toInt()
    var card = Card(cardNumber)
    var numbers = StringUtils.splitByWholeSeparator(splitString[1], "|")

    // Parse scratch numbers
    for (number in StringUtils.splitByWholeSeparator(numbers[0], " ")) {

        if (StringUtils.isNotEmpty(number)) {
            card.scratchedNumbers.add(StringUtils.trimToEmpty(number).toInt())
        }
    }

    for (number in StringUtils.splitByWholeSeparator(numbers[1], " ")) {

        if (StringUtils.isNotEmpty(number)) {
            card.winningNumbers.add(StringUtils.trimToEmpty(number).toInt())
        }
    }

    return card
}

fun determinePointsPartOne(card: Card): Int {
    card.scratchedNumbers.retainAll(card.winningNumbers)

    return if (card.scratchedNumbers.size < 1) {
        0
    } else {
        2.toDouble().pow(card.scratchedNumbers.size - 1).toInt()
    }
}

private fun partTwo (lines : List<String>): Int {

    var cards = lines
        .stream()
        .map{
            return@map convertStringToCard(it)
        }
        .collect(Collectors.toList())

    for (i in cards.indices) {
        cards[i].scratchedNumbers.retainAll(cards[i].winningNumbers)
        for (j in cards[i].scratchedNumbers.indices) {
            cards[i+j+1].number +=1
        }

        if (cards[i].number > 1) {
            for (j in cards[i].scratchedNumbers.indices) {
                cards[i+j+1].number = cards[i+j+1].number + cards[i].number - 1
            }
        }
    }

    var numberOfCards = cards
        .stream()
        .map{
            return@map it.number
        }
        .reduce{sum, element -> sum + element}
    return numberOfCards.get()
}

fun main(args : Array<String>) {

    val lines = readInput("day4")
    println(partOne(lines))
    println(partTwo(lines))

}

