package nl.groen.year2023.day1

import org.apache.commons.lang3.StringUtils
import java.io.File
import java.io.FileReader

fun main(args : Array<String>) {

    var file = File("C:\\Workspace\\AdventOfCode2023\\src\\main\\resources\\day1_input.txt")

    var fileReader = FileReader(file)
    var lines = fileReader.readLines()

    var digits = lines.stream().map{
        var characters = it.toCharArray()
        var twoDigitString = CharArray(2)
        twoDigitString[0] = extractFistDigit(characters)
        twoDigitString[1] = extractLastDigit(characters)

        return@map twoDigitString.concatToString().toInt()
    }

//    for (digit in digits) {
//        println(digit)
//    }
    var sum = digits.reduce{sum, element -> sum + element}
    println(sum.get())

}

private fun extractFistDigit(characters: CharArray): Char {
    for (i in characters.indices) {

        if (characters[i].isDigit()) {
            return characters[i]
        }

        var digitInWord = digitInWord(i, characters.concatToString())
        if (digitInWord != null) {
            return digitInWord
        }

    }

    throw RuntimeException("No digit")
}

private fun extractLastDigit(characters: CharArray): Char {
    for (i in characters.indices) {
        if (characters[characters.size-i-1].isDigit()) {
            return characters[characters.size-i-1]
        }

        var digitInWord = digitInWordRecursive(characters.size - i, characters.concatToString())
        if (digitInWord != null) {
            return digitInWord
        }
    }

    throw RuntimeException("No digit")
}

private fun digitInWord(i: Int, string: String): Char? {
    if (StringUtils.equals("one", StringUtils.substring(string, i, i+3))) {
        return "1".toCharArray()[0]
    }
    if (StringUtils.equals("two", StringUtils.substring(string, i, i+3))) {
        return "2".toCharArray()[0]
    }
    if (StringUtils.equals("three", StringUtils.substring(string, i, i+5))) {
        return "3".toCharArray()[0]
    }
    if (StringUtils.equals("four", StringUtils.substring(string, i, i+4))) {
        return "4".toCharArray()[0]
    }
    if (StringUtils.equals("five", StringUtils.substring(string, i, i+4))) {
        return "5".toCharArray()[0]
    }
    if (StringUtils.equals("six", StringUtils.substring(string, i, i+3))) {
        return "6".toCharArray()[0]
    }
    if (StringUtils.equals("seven", StringUtils.substring(string, i, i+5))) {
        return "7".toCharArray()[0]
    }
    if (StringUtils.equals("eight", StringUtils.substring(string, i, i+5))) {
        return "8".toCharArray()[0]
    }
    if (StringUtils.equals("nine", StringUtils.substring(string, i, i+4))) {
        return "9".toCharArray()[0]
    }

    return null
}

fun digitInWordRecursive(i: Int, string: String): Char? {
    if (StringUtils.equals("one", StringUtils.substring(string, i-3, i))) {
        return "1".toCharArray()[0]
    }
    if (StringUtils.equals("two", StringUtils.substring(string, i-3, i))) {
        return "2".toCharArray()[0]
    }
    if (StringUtils.equals("three", StringUtils.substring(string, i-5, i))) {
        return "3".toCharArray()[0]
    }
    if (StringUtils.equals("four", StringUtils.substring(string, i-4, i))) {
        return "4".toCharArray()[0]
    }
    if (StringUtils.equals("five", StringUtils.substring(string, i-4, i))) {
        return "5".toCharArray()[0]
    }
    if (StringUtils.equals("six", StringUtils.substring(string, i-3, i))) {
        return "6".toCharArray()[0]
    }
    if (StringUtils.equals("seven", StringUtils.substring(string, i-5, i))) {
        return "7".toCharArray()[0]
    }
    if (StringUtils.equals("eight", StringUtils.substring(string, i-5, i))) {
        return "8".toCharArray()[0]
    }
    if (StringUtils.equals("nine", StringUtils.substring(string, i-4, i))) {
        return "9".toCharArray()[0]
    }

    return null
}

