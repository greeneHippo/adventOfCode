package nl.groen.day13

import nl.groen.readLines
import nl.groen.transposeListString

private fun partOne (lines : List<String>): Long {

    val emptyLines = lines.mapIndexedNotNull() { index, s ->
            if (s.isEmpty()) {
                return@mapIndexedNotNull index
            } else {
                return@mapIndexedNotNull null
            }
        }

    var result = 0

    for (i in 0..emptyLines.size) {
        val fromIndex = if (i == 0) { 0 } else {emptyLines[i-1] + 1}
        val toIndex = if (i == emptyLines.size) { lines.size } else {emptyLines[i]}
        val pattern = lines.subList(fromIndex, toIndex)
        val reflection = determineReflectionOfPattern(pattern)

        result += if (reflection.first != null) {
            reflection.first!! * 100
        } else {
            reflection.second!!
        }
    }


    return result.toLong()
}

private fun determineReflectionOfPattern(pattern: List<String>): Pair<Int?, Int?> {
    return determineReflectionOfPattern(pattern, Pair(null, null))
}

private fun determineReflectionOfPattern(pattern: List<String>, ignoreReflection:Pair<Int?, Int?>): Pair<Int?, Int?> {
    val indexHorizonalReflection = determineReflectionIndex(pattern, ignoreReflection.first)

    if (indexHorizonalReflection != null) {
        return Pair(indexHorizonalReflection, null)
    }

    val patternTransposed = transposeListString(pattern)
    val indexVerticalReflection = determineReflectionIndex(patternTransposed, ignoreReflection.second)
    if (indexVerticalReflection != null) {
        return Pair(null, indexVerticalReflection)
    }

    return Pair(null, null)
}

private fun determineReflectionOfModifiedPattern(pattern: List<String>, initialReflection: Pair<Int?, Int?>): Pair<Int?, Int?> {
    for (j in pattern.indices) {
        for (k in 0..< pattern[0].length) {
            val newValue = if (pattern[j].substring(k, k + 1) == ".") {
                "#"
            } else {
                "."
            }
            val modifiedPattern = pattern.toMutableList()
            modifiedPattern[j] = pattern[j].replaceRange(range = k..k, newValue)

            val result = determineReflectionOfPattern(modifiedPattern, initialReflection)
            if (result.first == null && result.second == null) {
                continue
            }
            if (result == initialReflection) {
                continue
            }

            return result
        }
    }

    return Pair(null, null)
}

private fun determineReflectionIndex(pattern: List<String>, ignoreIndex: Int?) =

    pattern.mapIndexedNotNull { index, s ->
        if (index != 0 && s == pattern[index - 1]) {
            return@mapIndexedNotNull index
        } else {
            return@mapIndexedNotNull null
        }
    }
    .filter { ignoreIndex == null || ignoreIndex != it }
    .firstOrNull() {
        if (it == 1 || it == pattern.size - 1) {
            true
        } else {
            isReflectionInOtherLines(pattern, Pair(it - 2, it + 1))
        }
    }

fun isReflectionInOtherLines(pattern: List<String>, inputPair: Pair<Int, Int>): Boolean {

    if (inputPair.first == -1 || inputPair.second == pattern.size) {
        return true;
    }
    if (pattern[inputPair.first] == pattern[inputPair.second]) {
        return isReflectionInOtherLines(pattern, Pair(inputPair.first-1, inputPair.second+1))
    }

    return false
}

private fun partTwo (lines : List<String>): Long {

    val emptyLines = lines.mapIndexedNotNull() { index, s ->
        if (s.isEmpty()) {
            return@mapIndexedNotNull index
        } else {
            return@mapIndexedNotNull null
        }
    }

    var result = 0

    for (i in 0..emptyLines.size) {
        val fromIndex = if (i == 0) { 0 } else {emptyLines[i-1] + 1}
        val toIndex = if (i == emptyLines.size) { lines.size } else {emptyLines[i]}
        val pattern = lines.subList(fromIndex, toIndex)
        val reflection = determineReflectionOfPattern(pattern)

        val newReflection = determineReflectionOfModifiedPattern(pattern, reflection)

        result += if (newReflection.first != null) {
            newReflection.first!! * 100
        } else {
            newReflection.second!!
        }
        println(i)

    }

    return result.toLong()
}

fun main(args : Array<String>) {

    val lines = readLines("day13")
    //println(partOne(lines))
    println(partTwo(lines))

}

