package nl.groen.year2023.day5

import nl.groen.readInput
import org.apache.commons.lang3.StringUtils
import java.lang.Math.floorMod

private fun partOne (lines : List<String>): Long {

    var seeds: ArrayList<Long> = StringUtils.splitByWholeSeparator(StringUtils.removeStart(lines[0], "seeds: "), " ").toList().map { return@map it.toLong()} as ArrayList<Long>
    var mapperPartOnes = arrayListOf<MapperPartOne>()
    var mapperPartOne: MapperPartOne? = null
    val sublines = lines.subList(2, lines.size)

    for (line in sublines) {

        if (StringUtils.isEmpty(line) && mapperPartOne != null) {
            mapperPartOnes.add(mapperPartOne)
            continue
        }

        if (line.contains("map")) {
            mapperPartOne = MapperPartOne(line)
            continue
        }

        var mapLine = StringUtils.splitByWholeSeparator(line, " ").toList().map { return@map it.toLong()}
        mapperPartOne?.mapLines?.add(mapLine)
    }

    for (mapper in mapperPartOnes) {

        seeds = mapper.applyMap(seeds)
    }

    return seeds.min()
}

private fun partTwo (lines : List<String>): Long {

    var seedsRanges = arrayListOf<LongRange>()
    val seedNumbers = StringUtils.splitByWholeSeparator(StringUtils.removeStart(lines[0], "seeds: "), " ").toList()

    for (i in seedNumbers.indices) {

        if (floorMod(i, 2) == 1) {
            seedsRanges.add(LongRange(seedNumbers[i-1].toLong(), (seedNumbers[i-1].toLong()+seedNumbers[i].toLong()-1)))
        }
    }

    var mappers = arrayListOf<MapperPartTwo>()
    var mapper: MapperPartTwo? = null
    val sublines = lines.subList(2, lines.size)

    for (line in sublines) {

        if (StringUtils.isEmpty(line) && mapper != null) {
            mappers.add(mapper)
            continue
        }

        if (line.contains("map")) {
            mapper = MapperPartTwo(line)
            continue
        }

        var mapLine = StringUtils.splitByWholeSeparator(line, " ").toList().map { return@map it.toLong()}
        mapper?.mapLines?.add(mapLine)

    }

    for (i in seedNumbers.indices) {

        if (floorMod(i, 2) == 1) {
            seedsRanges.add(LongRange(seedNumbers[i-1].toLong(), (seedNumbers[i-1].toLong()+seedNumbers[i].toLong()-1)))
        }
    }

    var result = 0L
    var numberInPreviousMapper = result
    do {

        for (mapper in mappers.reversed()) {

            mapper.longRangeLines = mapper.mapLines.map { return@map MapLine(it[0], LongRange(it[1], it[1]+it[2])) }.toCollection(ArrayList())
            numberInPreviousMapper = mapper.mapBack(numberInPreviousMapper)
        }

        //println("Het terugmappen van $result leidt tot $numberInPreviousMapper")
        for (range in seedsRanges) {
            if (range.contains(numberInPreviousMapper)) {
                println("$numberInPreviousMapper zit in $range!")
                return result
            }
        }

        println("$result is niet gevonden, ga door met volgende nummer")
        result +=1
        numberInPreviousMapper = result
    } while (true)
}

fun main(args : Array<String>) {

    val lines = readInput("day5")
    //println(partOne(lines))
    println(partTwo(lines))

}

