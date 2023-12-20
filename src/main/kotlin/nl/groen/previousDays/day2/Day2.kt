package nl.groen.day1

import nl.groen.previousDays.day2.Cube
import nl.groen.previousDays.day2.CubeGame
import org.apache.commons.lang3.StringUtils
import java.io.File
import java.io.FileReader
import java.util.stream.Collectors

fun main(args : Array<String>) {

    var file = File("C:\\Workspace\\AdventOfCode2023\\src\\main\\resources\\day2_input.txt")

    var fileReader = FileReader(file)
    var lines = fileReader.readLines()

    var games = lines
        .stream()
        .map{
            return@map convertStringToCubeArray(it)
        }.collect(Collectors.toList())


    var sumGameNumbers = games
        .filter { it.cubes[Cube.RED]!! <= 12 && it.cubes[Cube.GREEN]!! <= 13  && it.cubes[Cube.BLUE]!! <= 14 }
        .map(CubeGame::gameNumber).reduce{ sum, element -> sum + element}

    var productMinimum = games
        .map { it.cubes[Cube.RED]!! * it.cubes[Cube.GREEN]!! * it.cubes[Cube.BLUE]!!}
        .reduce{sum, element -> sum + element}

   println(sumGameNumbers)
   println(productMinimum)
}

private fun convertStringToCubeArray(it: String?): CubeGame {

    var splitGame = StringUtils.splitByWholeSeparator(it, ":")
    var tmp = splitGame[0]
    var gameNumber = StringUtils.removeStart(splitGame[0],"Game ").toInt()
    var result = CubeGame(gameNumber)
    var sets = StringUtils.splitByWholeSeparator(splitGame[1],";")

    for (set in sets) {
        var cubesInSet = StringUtils.splitByWholeSeparator(set,",")
        for (cube in cubesInSet) {
            var splittedCube = StringUtils.splitByWholeSeparator(cube," ")
            var numberOfCubes = StringUtils.trimToEmpty(splittedCube[0]).toInt()
            var cubeType = Cube.valueOf(StringUtils.trimToEmpty(splittedCube[1].uppercase()))

            var currentMax = result.cubes[cubeType]
            if (currentMax != null && currentMax < numberOfCubes) {
                result.cubes[cubeType] = numberOfCubes
                println("$gameNumber : $cubeType -> $numberOfCubes")
            }
        }

    }

    return result
}

