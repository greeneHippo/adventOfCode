package nl.groen.previousDays.day12

import nl.groen.readLines
import org.apache.commons.lang3.StringUtils
import org.apache.commons.math3.util.CombinatoricsUtils

data class Record(var row : String, val group : MutableList<Int>)
data class ModifiedRecord(val original: String, var sections : MutableList<String>, var group : MutableList<Int>, var combinations: Long)

private fun partOne (lines : List<String>): Long {

    val records = lines.map { it.split(" ")}.map { Record(
        StringUtils.strip(it[0], "."),
        it[1].split(",").map { s -> s.toInt() }.toMutableList())  }

    // Mark ?'s if a # follows that precisely fit the first of or last element of the group.
    records.forEach {
        if (it.row.substring(1, it.group.first()+1).all { s -> s.uppercase() == "#" }) {
            it.row = it.row.replaceRange(0..<1, ".")
            it.row = it.row.replaceRange(it.group.first()+1..< it.group.first()+2, ".")
            it.row = StringUtils.strip(it.row, ".")
        }

        if (it.row.reversed().substring(1, it.group.reversed().first()+1).all { s -> s.uppercase() == "#" }) {
            it.row = it.row.reversed().replaceRange(0..<1, ".").reversed()
            it.row = it.row.reversed().replaceRange(it.group.reversed().first()+1..< it.group.reversed().first()+2, ".").reversed()
            it.row = StringUtils.strip(it.row, ".")
        }
    }

    // Replace subsequent dots as one dot.
    records.forEach { it.row = it.row.replace("\\.+".toRegex(), "\\.")}

    // Strip sections that do not fit the smallest amount of records
    val modifiedRecords = records.map {
        ModifiedRecord(
            it.row,
            it.row.split(".").toMutableList(),
            it.group,
            0)
    }

    modifiedRecords.forEach {
        it.sections = it.sections.filter { s -> s.length >= it.group.min()}.toMutableList()
    }

    // Mark all records in which each group fits precisely because sum of groups + gaps equals length of the string
    modifiedRecords.filter { it.sections.size > 1 }.forEach {
        val lengthRow = it.sections.map { s -> s.length }.reduce { acc, i -> acc + i} + (it.sections.size - 1)
        val lengthGroups =  it.group.reduce { acc, i -> acc + i} + (it.group.size - 1)

        if (lengthRow == lengthGroups) {
            it.combinations = 1L
        }
    }

    // Remove sections / groups that precisely fit at the beginning or the end.
    modifiedRecords.forEach {
        while (it.sections.isNotEmpty() && it.sections.first().contains("#") && it.sections.first().length <= it.group.first()) {
           //println("first - ${it.original}")
           it.sections = it.sections.drop(1).toMutableList()
           it.group = it.group.drop(1).toMutableList()
        }

        while (it.sections.isNotEmpty() && it.sections.last().contains("#") && it.sections.last().length <= it.group.last()) {
            //println("last - ${it.original}")
            it.sections = it.sections.dropLast(1).toMutableList()
            it.group = it.group.dropLast(1).toMutableList()
        }
        if (it.sections.isEmpty() || it.group.isEmpty()) {
            it.combinations = 1L
        }
    }

    // Remove sections / groups that start at the beginning or the end
    modifiedRecords.forEach {
        while (it.sections.isNotEmpty() && it.group.isNotEmpty() && it.sections.first().first().uppercase() == "#") {
            //println("remove from start - ${it.original} - ${it.sections.first()}")
            it.sections[0] = it.sections.first().drop(it.group.first() + 1)
            if (it.sections.first().isEmpty()) {
                it.sections = it.sections.drop(1).toMutableList()
            }
            it.group = it.group.drop(1).toMutableList()
        }
        while (it.sections.isNotEmpty() && it.group.isNotEmpty() && it.sections.last().last().uppercase() == "#") {
            //println("remove from end - ${it.original} - ${it.sections.last()}")
            it.sections[it.sections.size-1] = it.sections.last().dropLast(it.group.last() + 1)
            if (it.sections.last().isEmpty()) {
                it.sections = it.sections.dropLast(1).toMutableList()
            }
            it.group = it.group.dropLast(1).toMutableList()
        }

        if (it.sections.isEmpty() || it.group.isEmpty()) {
            it.combinations = 1L
        }
    }

    // Remove sections / groups for which the highest group only fits one specific section
    modifiedRecords.forEach {
        while (it.sections.isNotEmpty() && it.group.isNotEmpty() &&
                    it.group.max() == it.sections.maxOfOrNull { s -> s.length } &&
                    it.sections.count { m -> m.length == it.sections.maxOfOrNull { s -> s.length } } == 1) {

            val size = it.group.max()
            it.sections = it.sections.filter { s -> s.length != size}.toMutableList()
            it.group = it.group.filter{s -> s != size}.toMutableList()
        }

        if (it.sections.isEmpty() || it.group.isEmpty()) {
            it.combinations = 1L
        }
    }


    modifiedRecords
        .filter { it.combinations == 0L }
        .filter { it.sections.size == 1}
        .filter { it.sections[0].none { s -> s.uppercase() == "#" } }
        .forEach {
            //println(it.original)
            val lengthRow = it.sections.map { s -> s.length }.reduce { acc, i -> acc + i} + (it.sections.size - 1)
            val lengthGroups =  it.group.reduce { acc, i -> acc + i} + (it.group.size - 1)
            val remainingElements = lengthRow - lengthGroups
            val numberSpaces = it.group.size + 1
            it.combinations = CombinatoricsUtils.binomialCoefficient(remainingElements + numberSpaces -1, numberSpaces-1)
    }

    modifiedRecords
        .filter { it.combinations == 0L }
        .filter { it.sections.size == 1}
        .filter { it.sections[0].none { s -> s.uppercase() == "#" } }
        .forEach {
            //println(it.original)
            val lengthRow = it.sections.map { s -> s.length }.reduce { acc, i -> acc + i} + (it.sections.size - 1)
            val lengthGroups =  it.group.reduce { acc, i -> acc + i} + (it.group.size - 1)
            val remainingElements = lengthRow - lengthGroups
            val numberSpaces = it.group.size + 1
            it.combinations = CombinatoricsUtils.binomialCoefficient(remainingElements + numberSpaces -1, numberSpaces-1)
        }

    modifiedRecords.filter { it.combinations == 0L }.forEach {
        println("${it.combinations} - ${it.original} - ${it.sections} - ${it.group}")
    }

    return modifiedRecords.count { it.combinations != 0L }.toLong()
}

private fun partOneCorrect (lines : List<String>): Long {

    val result = lines.sumOf { it.split(" ").let { count(it.first(), it[1].split(",").map(String::toInt)) }}
    return result;
}

private val cache = hashMapOf<Pair<String, List<Int>>, Long>()

private fun count(config: String, groups: List<Int>): Long {
    if (groups.isEmpty()) return if ("#" in config) 0 else 1
    if (config.isEmpty()) return 0

    return cache.getOrPut(config to groups) {
        var result = 0L
        if (config.first() in ".?")
            result += count(config.drop(1), groups)
        if (config.first() in "#?" && groups.first() <= config.length && "." !in config.take(groups.first()) && (groups.first() == config.length || config[groups.first()] != '#'))
            result += count(config.drop(groups.first() + 1), groups.drop(1))
        result
    }
}

private fun partOneDP (lines : List<String>): Long {

    val result = lines.sumOf { it.split(" ").let { countDP(it.first(), it[1].split(",").map(String::toInt), 0,0,0) }}
    return result;
}

// iconfig == current position within dots
// iblock == current position within blocks
// current == length of current block of '#'
// state space is len(dots) * len(blocks) * len(dots)
private val cacheDP = hashMapOf<Triple<Int, Int, Int>, Long>()

private fun countDP(config: String, groups: List<Int>, iconfig: Int, igroup : Int, current: Int) : Long {

    return cacheDP.getOrPut(Triple(iconfig, igroup, current)) {
        var result = 0L
        if (iconfig == config.length) {
            if (igroup == groups.size && current == 0) {
                result = 1L
            } else if (igroup == groups.size - 1 && groups[igroup] == current) {
                result = 1L
            } else {
                result = 0L
            }
        }

        if (iconfig != config.length && config[iconfig].uppercase() in ".?") {
            if (current == 0) {
                result += countDP(config, groups, iconfig+1, igroup, 0)
            } else if (current > 0 && igroup < groups.size && groups[igroup] == current) {
                result += countDP(config, groups, iconfig+1, igroup+1, 0)
            }
        }
        if (iconfig != config.length && config[iconfig].uppercase() in "#?") {
            result += countDP(config, groups, iconfig+1, igroup, current+1)
        }
        result
    }
}

private fun partTwo (lines : List<String>): Long {

    val result = lines.sumOf {
        it.split(" ")
        .let { split ->
             countDP(
                List(5) {split[0]}.joinToString(separator = "?", transform = null),
                List(5) {split[1]}.joinToString(separator = ",", transform = null).split(",").map(String::toInt),
                0,
                0,
                0
            )
        }
        .also { result -> println("$it - $result")}
        .also { cacheDP.clear() }


    }
    return result;
}

fun main(args : Array<String>) {

    val lines = readLines("day12")
    //println(partOne(lines))
//    println(partOneCorrect(lines))
//    println(partOneDP(lines))
    println(partTwo(lines))

}

