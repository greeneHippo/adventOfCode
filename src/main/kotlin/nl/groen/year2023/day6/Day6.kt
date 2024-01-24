package nl.groen.year2023.day6

import nl.groen.readInput
import org.apache.commons.lang3.StringUtils

private fun partOne (lines : List<String>): Long {

    var regexDigit = "\\d+".toRegex()
    var allTimes = regexDigit.findAll(lines[0]).toList()
    var allDistances = regexDigit.findAll(lines[1]).toList()
    var races = arrayListOf<Race>()
    for (i in allTimes.indices) {
        races.add(Race(allTimes[i].value.toLong(), allDistances[i].value.toLong()))
    }

    var result = arrayListOf<Long>()
    for (race in races) {
        var resultRace = 0L
        //println("$race")
        for (i in 0 .. race.time) {
            var distance = i * (race.time-i)
            //println("$distance - ${race.record}")
            if (distance > race.record) {
                resultRace +=1
            }
        }
        result.add(resultRace)
    }
    return result.reduce {i, j -> i * j}
}

private fun partTwo (lines : List<String>): Long {

    var regexDigit = "\\d+".toRegex()
    var strippedLines = lines.map{StringUtils.replace(it, " ", "")}
    var allTimes = regexDigit.findAll(strippedLines[0]).toList()
    var allDistances = regexDigit.findAll(strippedLines[1]).toList()
    var races = arrayListOf<Race>()
    for (i in allTimes.indices) {
        races.add(Race(allTimes[i].value.toLong(), allDistances[i].value.toLong()))
    }

    var result = arrayListOf<Long>()
    for (race in races) {
        var resultRace = 0L
        //println("$race")
        for (i in 0 .. race.time) {
            var distance = i * (race.time-i)
            //println("$distance - ${race.record}")
            if (distance > race.record) {
                resultRace +=1
            }
        }
        result.add(resultRace)
    }
    return result.reduce {i, j -> i * j}
}

fun main(args : Array<String>) {

    val lines = readInput("day6")
    println(partOne(lines))
    println(partTwo(lines))

}

