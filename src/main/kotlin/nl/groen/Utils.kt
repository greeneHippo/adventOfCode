package nl.groen

import java.time.Instant
import java.time.ZoneId
import java.util.stream.Collectors
import java.util.stream.IntStream
import kotlin.io.path.*
import kotlin.math.abs

data class MoveAction(val position: Position, val direction: Direction)
data class Position(val x: Int, val y: Int, val z: Int = -1)
data class PositionLong(val x: Long, val y: Long, val z: Long = -1)

fun readInput (input : String): List<String> {
    return readInput("2022", input)
}

fun readInput (year : String = "2022", input : String): List<String> {

    try {
        val fullPath = Path("src\\main\\kotlin\\input\\year$year\\$input.txt").absolutePathString()
        return Path(fullPath).readLines()
    } catch (e: Exception) {
    }

    val fullPath = Path("src\\main\\kotlin\\input\\old\\$input.txt").absolutePathString()
    return Path(fullPath).readLines()
}

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)


fun groupStringsOnEmptyLine(input: List<String>): List<List<String>> {
    val groupStrings: MutableList<MutableList<String>> = mutableListOf(mutableListOf())
    input.fold(groupStrings) { acc, s ->
        if (s.isEmpty()) {
            acc.add(mutableListOf())
        } else {
            acc.last().add(s)
        }
        acc
    }
    return groupStrings
}

fun <T> parseGrid(lines: List<String>, costGrid: MutableMap<Position, T>, neighbourGrid: MutableMap<Position, MutableList<MoveAction>>, transform: (String) -> T) {

    lines.forEachIndexed { y, s ->
        s.forEachIndexed { x, c ->
            costGrid[Position(x, y)] = transform.invoke(c.toString())
            if (neighbourGrid[Position(x, y)] == null) {
                neighbourGrid[Position(x, y)] = mutableListOf()
            }
            if (x != s.length - 1) {
                neighbourGrid[Position(x, y)]!!.add(MoveAction(Position(x + 1, y), Direction.EAST))
            }
            if (x != 0) {
                neighbourGrid[Position(x, y)]!!.add(MoveAction(Position(x - 1, y), Direction.WEST))
            }
            if (y != lines.size - 1) {
                neighbourGrid[Position(x, y)]!!.add(MoveAction(Position(x, y + 1), Direction.SOUTH))
            }
            if (y != 0) {
                neighbourGrid[Position(x, y)]!!.add(MoveAction(Position(x, y - 1), Direction.NORTH))
            }
        }
    }
}

fun <T> transpose(list: List<List<T>>): List<List<T>> {
    val N = list.stream().mapToInt { l: List<T> -> l.size }.max().orElse(-1)
    val iterList = list.stream().map { it: List<T> -> it.iterator() }.collect(Collectors.toList())
    return IntStream.range(0, N)
        .mapToObj { n: Int ->
            iterList.stream()
                .filter { it: Iterator<T> -> it.hasNext() }
                .map { m: Iterator<T> -> m.next() }
                .collect(Collectors.toList())
        }
        .collect(Collectors.toList())
}


inline fun <reified T> transpose(twoDArray: Array<Array<T>>): Array<Array<T>> {
    val cols = twoDArray[0].size
    val rows = twoDArray.size

    return Array(cols) { j ->
        Array(rows) { i ->
            twoDArray[i][j]
        }
    }
}

fun transposeListString(input: List<String>): List<String> {

    val intrange = IntRange(1, input[0].length)
    val transposed = transpose(input.map { it.split("").slice(intrange) }.toList())
    return transposed.map { it.reduce{acc, s -> acc + s} }

}

enum class Direction(val symbolULDR: String, val symbolNumber: String) {
   NORTH("U", "3"), EAST("R", "0"), SOUTH("D", "1"), WEST("L", "2");

    fun turnRight(): Direction {
        when (this) {
            NORTH -> return EAST
            EAST -> return SOUTH
            SOUTH -> return WEST
            WEST -> return NORTH
        }
    }

    companion object {
        fun getByULDR(input: String): Direction {
            return Direction.entries.firstOrNull { part -> input == part.symbolULDR }!!
        }
        fun getByNumber(input: String): Direction {
            return Direction.entries.firstOrNull { part -> input == part.symbolNumber }!!
        }
        fun isOppositeDirection(dir: Direction, dir2: Direction) : Boolean {
            return (2 + dir2.symbolNumber.toInt()) % 4 == dir.symbolNumber.toInt()
        }
        fun isNextDirection(dir: Direction, dir2: Direction) : Boolean {
            return abs((dir.symbolNumber.toInt() - dir2.symbolNumber.toInt())) % 2 == 1
        }
    }
}

inline fun <reified T> revertRows(input: Array<Array<T>>): Array<Array<T>> {
    return input.map { return@map it.reversed().toTypedArray() }.toTypedArray()
}

fun <T> printList(input: List<T>) {
    println("-------------------------------------------")
    input.forEach {
        println(it)
    }
}
fun print2DList(input: List<List<String>>) {
    println("-------------------------------------------")
    input.forEach {
        it.forEach { s ->
            print(s)
        }
        println()
    }
}
fun <T> print2DArray(input: Array<Array<T>>) {
    val time = Instant.now().atZone(ZoneId.systemDefault())
    //val fullPath = Path("src\\main\\kotlin\\input\\${input.size}_${time.minute}_${time.second}_output.txt").absolutePathString()
    val fullPath = Path("src\\main\\kotlin\\input\\output.txt").absolutePathString()
    val writer = Path(fullPath)
    writer.writeText("")
    input.forEach {
        it.forEach { s ->
            writer.appendText(s.toString())
        }
        writer.appendText(System.lineSeparator())
    }
}

fun <T> printGridOfPoints(grid :MutableMap<Position, T>) {

    for (y in grid.keys.minOf{it.y} .. grid.keys.maxOf{it.y}) {
        for (x in grid.keys.minOf{it.x} .. grid.keys.maxOf{it.x}) {
            print(grid[Position(x, y)].toString())
        }
        println("")
    }
}

fun findLCMOfListOfNumbers(numbers: List<Long>): Long {
    var result = numbers[0]
    for (i in 1 until numbers.size) {
        result = calculateLCM(result, numbers[i])
    }
    return result
}

fun calculateLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

fun calculateLCMOfListOfNumbers(numbers: List<Long>): Long {
    var result = 1L
    for (number in numbers) {
        result = (result * number) / calculateGCD(result, number)
    }
    return result
}

fun calculateGCD(a: Long, b: Long): Long {
    var num1 = a
    var num2 = b
    while (num2 != 0L) {
        val temp = num2
        num2 = num1 % num2
        num1 = temp
    }
    return num1
}
