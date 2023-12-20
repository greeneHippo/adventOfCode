package nl.groen.previousDays.day3

import nl.groen.readLines
import kotlin.io.path.readLines
import kotlin.math.max
import kotlin.math.min

private fun partOne (lines : List<String>): Int {

    var numberAdjacentToSymbol = arrayListOf<Int>()

    for (i in lines.indices) {

        var regex = "\\d+".toRegex()
        var regex2 = "\\d||\\.".toRegex()
        var result = regex.find(lines[i])


        do {

            if (result == null) {
                continue
            }

            val number = result.value.toInt()
            val subSet = lines.subList(max(0, i - 1), min(lines.size, i + 2))
            if (subSet.all {
                    it.substring(max(0, result!!.range.first - 1), min(lines[i].length, result!!.range.last + 2))
                        .chars().allMatch { char -> Character.isDigit(char) || ".".single().code == char }
                }) {
                result = result.next()
                continue
            }

            // println(number)
            numberAdjacentToSymbol.add(number)
            result = result.next()

        } while (result != null)


    }

    return numberAdjacentToSymbol.reduce { sum, element -> sum + element }
}

private fun partTwo (lines : List<String>): Int {

    var numberAdjacentToSymbol = arrayListOf<Int>()

    for (i in lines.indices) {

        var regexAsterisk = "\\*".toRegex()
        var regexDigit = "\\d+".toRegex()
        var result = regexAsterisk.find(lines[i])

        do {

            if (result == null) {
                continue
            }

            var numberAdjacentToGear = arrayListOf<Int>()
            val subSet = lines.subList(max(0, i - 1), min(lines.size, i + 2))
            for (line in subSet) {
                var resultDigit = regexDigit.find(line)

                do {

                    if (resultDigit == null) {
                        continue
                    }

                    val number = resultDigit.value.toInt()

                    if (resultDigit.range.contains(result.range.first-1) || resultDigit.range.contains(result.range.first) || resultDigit.range.contains(result.range.first+1)) {
                       // println("$i: $number")
                        numberAdjacentToGear.add(number)
                    }
                    resultDigit = resultDigit.next()

                } while (resultDigit != null)
            }


            if (numberAdjacentToGear.size > 1) {
                if (numberAdjacentToGear[0] == numberAdjacentToGear[1]) {
                    println("Coordinaten: ${result.range.first},$i with numbers ${numberAdjacentToGear[0]} en \"${numberAdjacentToGear[1]} - ${Math.multiplyExact(numberAdjacentToGear[0], numberAdjacentToGear[1])}")
                }
                numberAdjacentToSymbol.add(numberAdjacentToGear.reduce { sum, element -> Math.multiplyExact(sum, element) })
            }
            result = result.next()

        } while (result != null)


    }

    return numberAdjacentToSymbol.reduce { sum, element -> sum + element }
}


fun main(args : Array<String>) {

    val lines = readLines("day3")
    println(partOne(lines))
    println(partTwo(lines))

}

