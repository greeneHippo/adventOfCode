package nl.groen.year2023.day4

class Card(var gameNumber: Int) {

    var scratchedNumbers = mutableSetOf<Int>()
    var winningNumbers = mutableSetOf<Int>()
    var number = 1

}
