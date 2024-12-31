package nl.groen.year2024

import nl.groen.groupStringsOnEmptyLine
import nl.groen.power
import nl.groen.println
import nl.groen.readInput

fun main() {

    class Register(var a: Long, var b: Long, var c: Long) {
        val initialValueA = a
        val output: MutableList<Long> = mutableListOf()

        fun performOperation(opcode: Long, literalOperand: Long, pointer: Int, program: List<Long>, initialValue: Long) :Int {

            val comboOperand: Long = when (literalOperand) {
                0L -> 0L
                1L -> 1L
                2L -> 2L
                3L -> 3L
                4L -> a
                5L -> b
                6L -> c
                7L -> {
                    if (opcode == 0L || opcode == 2L || opcode == 5L ) { error("not allowed")}
                    0L}
                else -> error("not allowed")
            }

            when(opcode) {
                0L -> a = a.div(power(2L, comboOperand))
                1L -> b = b xor literalOperand
                2L -> b = comboOperand % 8L
                3L -> if (a != 0L) return literalOperand.toInt()
                4L -> b = b xor c
                5L -> {
//                  if (output.isEmpty() && comboOperand % 8L == 2L) println(initialValueA)
                    output.add(comboOperand % 8L)
                    if (output.size > 12 && output == program.take(output.size).toMutableList()) println("$initialValue - ${"%o".format(initialValue)} - $output")
                    if (output != program.take(output.size).toMutableList()) return program.size
                }
                6L -> b = a.div(power(2L, comboOperand))
                7L -> c = a.div(power(2L, comboOperand))
            }

            return pointer + 2
        }


    }

    fun runProgram(program: Map<Int, Long>, register: Register): String {
        var pointer = 0
        val initialValue = register.a
        while (program.containsKey(pointer)) {
            pointer = register.performOperation(program[pointer]!!, program[pointer + 1]!!, pointer, program.values.toList(), initialValue)
        }

//        if (register.output.take(2) == listOf(2L,4L)) {
//            println("Match on first two: $initialValue")
//            println(register.output.joinToString(","))
//        }
        return register.output.joinToString(",")
    }

    fun part1 (input : List<String>): String {

        val groups = groupStringsOnEmptyLine(input)
        val register = Register(
            groups[0][0].split(": ")[1].toLong(),
            groups[0][1].split(": ")[1].toLong(),
            groups[0][2].split(": ")[1].toLong()
        )
        val program = groups[1][0].split("Program: ",",").drop(1).mapIndexed { index, s -> index to s.toLong() }.toMap()

        return runProgram(program, register)
    }

    fun part2 (input : List<String>, initialValue: Long): Long {

        val groups = groupStringsOnEmptyLine(input)
        val originalRegister = Register(

            groups[0][0].split(": ")[1].toLong(),
            groups[0][1].split(": ")[1].toLong(),
            groups[0][2].split(": ")[1].toLong()
        )
        val program = groups[1][0].split("Program: ",",").drop(1).mapIndexed { index, s -> index to s.toLong() }.toMap()
        val programString = program.values.toList().joinToString(",")

        var initialValueA = initialValue
        var register = Register(initialValueA, originalRegister.b, originalRegister.c)
        println(initialValueA)
        println("$initialValueA - " + runProgram(program, register))
        var iterations = 16777216L
//        while (runProgram(program, register) != programString && iterations < 1024) {
        while (true) {

            initialValueA = ("%o".format(iterations).take(8) + "366" + "%o".format(iterations).takeLast(1) + "1633").toLong(8)
            register = Register(initialValueA, originalRegister.b, originalRegister.c)
            if (runProgram(program, register) == programString) {
                return initialValueA
            }
            initialValueA = ("%o".format(iterations).take(8) + "366" + "%o".format(iterations).takeLast(1) + "1635").toLong(8)
            register = Register(initialValueA, originalRegister.b, originalRegister.c)
            if (runProgram(program, register) == programString) {
                return initialValueA
            }
            initialValueA++
//            if (initialValueA % 1L == 0L) {
//                println("$initialValueA - " + runProgram(program, register))
//            }
            iterations++
        }

        return initialValueA
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day17_test")
    val testInputPart2 = readInput("2024","Day17_test2")
    val input = readInput("2024","Day17")

//    check(part1(testInput) == "4,6,3,5,6,3,5,2,1,0")
//    println("Test successful")
//    part1(input).println()

//    check(part2(testInputPart2, 0L) == 117440L)
    println("Test successful")
    part2(input, 1000000000001633).println()

}

