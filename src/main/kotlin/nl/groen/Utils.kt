package nl.groen

import java.util.stream.Collectors
import java.util.stream.IntStream
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.readLines

fun readLines (input : String): List<String> {

    try {
        val fullPath = Path("src\\main\\kotlin\\input\\" + input + "_input.txt").absolutePathString()
        return Path(fullPath).readLines()
    } catch (e: Exception) {
    }

    val fullPath = Path("src\\main\\kotlin\\input\\old\\" + input + "_input.txt").absolutePathString()
    return Path(fullPath).readLines()
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

    companion object {
        fun getByULDR(input: String): Direction {
            return Direction.entries.firstOrNull { part -> input == part.symbolULDR }!!
        }
        fun getByNumber(input: String): Direction {
            return Direction.entries.firstOrNull { part -> input == part.symbolNumber }!!
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
    println("-------------------------------------------")
    input.forEach {
        it.forEach { s ->
            print(s)
        }
        println()
    }
}
