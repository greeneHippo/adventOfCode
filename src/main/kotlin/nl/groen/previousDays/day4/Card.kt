package nl.groen.previousDays.day4

class Card(var gameNumber: Int) {

    var scratchedNumbers = mutableSetOf<Int>()
    var winningNumbers = mutableSetOf<Int>()
    var number = 1

}
