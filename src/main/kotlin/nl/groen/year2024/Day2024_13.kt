package nl.groen.year2024

import nl.groen.PositionLong
import nl.groen.groupStringsOnEmptyLine
import nl.groen.println
import nl.groen.readInput
import org.jetbrains.kotlinx.multik.api.linalg.solve
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import java.math.BigDecimal
import java.math.RoundingMode

data class ClawMachine(val priceLocation: PositionLong, val buttonA: PositionLong, val buttonB: PositionLong)

fun main() {

    fun part1 (input : List<String>, offset: Long): Long {

        val groupStrings = groupStringsOnEmptyLine(input)
        val clawMachines = groupStrings.map {
            val buttonAString = it[0].split("Button A: X+",", Y+")
            val buttonBString = it[1].split("Button B: X+",", Y+")
            val priceString = it[2].split("Prize: X=",", Y=")
            return@map ClawMachine(
                PositionLong(priceString[1].toLong() + offset, priceString[2].toLong() + offset),
                PositionLong(buttonAString[1].toLong(), buttonAString[2].toLong()),
                PositionLong(buttonBString[1].toLong(), buttonBString[2].toLong())
            )
        }

        val tokens :List<Long> = clawMachines.map{

            val a = mk.ndarray(mk[
                mk[it.buttonA.x, it.buttonB.x],
                mk[it.buttonA.y, it.buttonB.y]
            ])
            val b = mk.ndarray(mk[
                it.priceLocation.x,
                it.priceLocation.y
            ])

            val result = mk.linalg.solve(a, b).data.map{it.toBigDecimal()}.map { bigDecimal -> bigDecimal.setScale(3, RoundingMode.HALF_EVEN)}
            val remainder = result.map{bigDecimal -> bigDecimal.rem(BigDecimal.valueOf(1)).toDouble()}
            if (remainder[0].equals(0.000) && remainder[1].equals(0.000)) {
                return@map (3L * result[0].toLong()) + result[1].toLong()
            } else {
                return@map null
            }
        }.filterNotNull()

        return tokens.filter { it != Long.MAX_VALUE }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day13_test")
    val input = readInput("2024","Day13")

    check(part1(testInput, 0) == 480L)
    part1(input, 0).println()

    part1(testInput, 10000000000000L).println()
    part1(input, 10000000000000L).println()

}

