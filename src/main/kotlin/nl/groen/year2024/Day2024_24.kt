package nl.groen.year2024

import nl.groen.groupStringsOnEmptyLine
import nl.groen.power
import nl.groen.println
import nl.groen.readInput

enum class Operation(val symbol : String) {
    XOR("XOR"),
    OR("OR"),
    AND("AND");

    companion object {
        fun getBySymbol(input: String): Operation {
            return Operation.entries.firstOrNull { part -> input == part.symbol }!!
        }
    }
}

fun main() {

    data class Gate(val input1: String, val input2: String, var output: String, val operation: Operation )

    fun List<Gate>.setNewOutputForGate(gate : Gate, output: String) {
        this.first { gate.input1 == it.input1 && gate.input2 == it.input2 && gate.operation == it.operation }.output = output
    }

    fun getListOfXAndY(gates: List<Gate>, seen: MutableSet<String>, gate: Gate) : List<String> {

        if (seen.contains(gate.output)) {
            return listOf()
        }

        val firstGate = gates.firstOrNull{it.output == gate.input1}
        val secondGate = gates.firstOrNull{it.output == gate.input2}

        seen.add(gate.output)

        val list = mutableListOf<String>()
        list.add(gate.output)
        list.addAll(if (seen.contains(gate.input1)) listOf() else if (firstGate == null) listOf(gate.input1) else getListOfXAndY(gates, seen, firstGate))
        list.addAll(if (seen.contains(gate.input2)) listOf() else if (secondGate == null) listOf(gate.input2) else getListOfXAndY(gates, seen, secondGate))
        seen.add(gate.input1)
        seen.add(gate.input2)
        return list
    }

    fun getValueOnWire(xyInputValues: MutableMap<String, Boolean>, string : String) = xyInputValues.filter { it.key.startsWith(string) }.entries.sortedBy { it.key }.reversed().map {
        if (it.value) "1" else "0"
    }.joinToString(separator = "") { it }.toLong(2)

    fun runProgram(gates: List<Gate>, xyInputValues: Map<String, Boolean>): MutableMap<String, Boolean> {

        val outputValues = xyInputValues.toMutableMap()
        while (gates.any { outputValues[it.output] == null }) {

            // Check if we reached a situation in which all gates that still need to be calculated have not valid inputs yet
            if (gates.none { gate -> outputValues[gate.output] == null && outputValues[gate.input1] != null && outputValues[gate.input2] != null }) {
                return outputValues
            }
            val gate = gates.first { outputValues[it.output] == null && outputValues[it.input1] != null && outputValues[it.input2] != null }
            outputValues[gate.output] = when (gate.operation) {
                Operation.OR -> outputValues[gate.input1]!! || outputValues[gate.input2]!!
                Operation.AND -> outputValues[gate.input1]!! && outputValues[gate.input2]!!
                Operation.XOR -> outputValues[gate.input1]!! xor outputValues[gate.input2]!!
            }

        }

        return outputValues
    }

    fun isProgramValidForInput(gatesSimulation: List<Gate>, wiresInput: Map<String, Boolean>, x: Long, y: Long) : Boolean {

        val xyInputValues = wiresInput.toMutableMap()
        val xString = x.toString(2).reversed()
        xString.forEachIndexed { index, char ->
            if (char == '1') {
                val string = if (index < 10 ) "x0$index" else "x$index"
                xyInputValues[xyInputValues.keys.filter { it == string }.minOf { it }] = true
            }
        }
        val yString = y.toString(2).reversed()
        yString.forEachIndexed { index, char ->
            if (char == '1') {
                val string = if (index < 10 ) "y0$index" else "y$index"
                xyInputValues[xyInputValues.keys.filter { it == string }.minOf { it }] = true
            }
        }
        val resultProgram = runProgram(gatesSimulation, xyInputValues)
        return getValueOnWire(resultProgram, "x") + getValueOnWire(resultProgram, "y") == getValueOnWire(resultProgram, "z")

    }
    fun init(strings: List<List<String>>): Pair<List<Gate>, MutableMap<String, Boolean>> {
        val gates = strings[1].map { it.split(" -> ", " ") }.map { Gate(it[0], it[2], it[3], Operation.getBySymbol(it[1])) }
        val xyInputValues: MutableMap<String, Boolean> = strings[0].map { it.split(": ") }.associate { it[0] to (it[1] == "1") }.toMutableMap()
        return Pair(gates, xyInputValues)
    }

    fun part1 (input : List<String>): Long {

        val strings = groupStringsOnEmptyLine(input)
        val (gates, xyInputValues: MutableMap<String, Boolean>) = init(strings)

        val resultProgram = runProgram(gates, xyInputValues)

        val output = resultProgram.filter { it.key.startsWith("z") }.entries.sortedBy { it.key}.reversed().map {
            if (it.value) "1" else "0"
        }.joinToString(separator = "") { it }


        return output.toLong(2)
    }

    fun applyAlreadySwappedGates(gatesSimulation: List<Gate>, swappedGate: Pair<String, String>?) {
        if (swappedGate == null) {
            return
        }

        val gate1 = gatesSimulation.firstOrNull { swappedGate.first == it.output }
        val gate2 = gatesSimulation.firstOrNull { swappedGate.second == it.output }

        if (gate1 == null || gate2 == null) {
            return
        }

        if (gate1 == gate2) {
            return
        }
        val newOutputGate1 = gate2.output
        val newOutputGate2 = gate1.output
        gatesSimulation.setNewOutputForGate(gate1, newOutputGate1)
        gatesSimulation.setNewOutputForGate(gate2, newOutputGate2)
    }

    fun swapGates(
        gatesToIterateOver: Set<String>,
        strings: List<List<String>>,
        configurationsToTest: List<Pair<Long, Long>>,
        successSwaps: MutableList<Pair<String, String>>,
        alreadySuccessfulSwaps: List<Pair<String, String>>,
    ) {
        val gatesToIterateOver = gatesToIterateOver.toMutableList()
        gatesToIterateOver.forEachIndexed { index, gate1Id ->
            for (i in index + 1..<gatesToIterateOver.size) {
                
                val gate2Id = gatesToIterateOver[i]

                val (gatesSimulation, xyInputValues: MutableMap<String, Boolean>) = init(strings)
                alreadySuccessfulSwaps.forEach { swappedGate -> applyAlreadySwappedGates(gatesSimulation, swappedGate) }

                val gate1 = gatesSimulation.first { gate1Id == it.output }
                val gate2 = gatesSimulation.first { gate2Id == it.output }

                val newOutputGate1 = gate2.output
                val newOutputGate2 = gate1.output
                gatesSimulation.setNewOutputForGate(gate1, newOutputGate1)
                gatesSimulation.setNewOutputForGate(gate2, newOutputGate2)

                xyInputValues.forEach { (t, _) -> xyInputValues[t] = false }

                // Check that the program is valid for all configurationsToTest
                val result = configurationsToTest.all { isProgramValidForInput(gatesSimulation, xyInputValues, it.first, it.second) }

                if (result) successSwaps.add(Pair(gate1Id, gate2Id))
            }
        }
    }

    fun determineGateToSwap(
        strings: List<List<String>>,
        zGates: Map<String, Set<String>>,
        successfulSwaps: List<Pair<String, String>>,
        zOutput1: String,
        zOutput2: String,
        configurationsToTest: List<Pair<Long, Long>>
    ) : List<Pair<String, String>> {

        val gatesToIterateOver = zGates[zOutput1]!!.toMutableSet()
        gatesToIterateOver.addAll(zGates[zOutput2]!!)
        gatesToIterateOver.removeIf { it.startsWith("x") || it.startsWith("y") }

        val successSwaps = mutableListOf<Pair<String, String>>()
        swapGates(gatesToIterateOver, strings, configurationsToTest, successSwaps, successfulSwaps)

        return successSwaps
    }

    fun printFailures(xyInputValues: MutableMap<String, Boolean>, strings: List<List<String>>) {
        xyInputValues.forEach { (key, _) ->
            val (gates, xyInputValues: MutableMap<String, Boolean>) = init(strings)

            xyInputValues.forEach { (t, _) ->
                xyInputValues[t] = false
            }
            xyInputValues[key] = true

            val resultProgram = runProgram(gates, xyInputValues)

            if (getValueOnWire(resultProgram, "x") + getValueOnWire(resultProgram, "y") != getValueOnWire(resultProgram, "z")) {
                println(key)
                println("x:" + getValueOnWire(resultProgram, "x"))
                println("y:" + getValueOnWire(resultProgram, "y"))
                println("z:" + getValueOnWire(resultProgram, "z"))
                println()
                println("------------------------------------")
            }
        }
    }

    fun part2 (input : List<String>): String {

        val strings = groupStringsOnEmptyLine(input)
        val (gates, xyInputValues: MutableMap<String, Boolean>) = init(strings)

        // First, do crude check to find bits where the addition fails.
        printFailures(xyInputValues, strings)

        // Second, map the gates that are 'new' for this bit
        val seen = mutableSetOf<String>()
        val relevantGatesPerZOutput = gates
            .filter { gate -> gate.output.startsWith("z")}
            .sortedBy { it.output }
            .associate { gate -> gate.output to getListOfXAndY(gates, seen, gate).toSet()
        }

        // Third, for each of the four failures, swap the relevant gates to find what does work.
        // What would be nice, is to take the printedFailures as input, loop over them, and use the specific set of 8 possibilities of input x 0/1, y 0/1 and carry bit 0/1 as config to test
        val successfulSwaps = mutableListOf<Pair<String, String>>()
        successfulSwaps.addAll(
            determineGateToSwap(strings, relevantGatesPerZOutput, listOf(),
            "z09", "z10", listOf(
                Pair(power(2, 9), 0),
                Pair(0,power(2, 9)),
                Pair(power(2, 9)-1,power(2, 9)+1)
            )
        ))
        successfulSwaps.addAll(
              determineGateToSwap(strings, relevantGatesPerZOutput, successfulSwaps,
                  "z20", "z21", listOf(
                      Pair(power(2, 21), 0),
                      Pair(0, power(2, 21)),
                      Pair(power(2, 20), 0),
                      Pair(0, power(2, 20)),
                      Pair(
                          power(2, 20),
                          power(2, 20)
                      )
                      ,Pair(748576, 1197152)
                      ,Pair(948576, 1797152)
                  )
              )
        )
        successfulSwaps.addAll(
        determineGateToSwap(strings, relevantGatesPerZOutput, successfulSwaps,
            "z30", "z31", listOf(
                Pair(power(2, 30), 0),
                Pair(0, power(2, 30)),
                Pair(power(2, 31), 0),
                Pair(0, power(2, 31)),
                Pair(
                    power(2, 30),
                    power(2, 30)
                ),
                Pair(
                    power(2, 31),
                    power(2, 31)
                ),
                Pair(
                    power(2, 31)+14,
                    power(2, 31)-14
                )
            )
        )
        )
        successfulSwaps.addAll(
        determineGateToSwap(strings, relevantGatesPerZOutput, successfulSwaps,
            "z34", "z35", listOf(
                Pair(power(2, 34), 0),
                Pair(0, power(2, 34)),
                Pair(power(2, 35), 0),
                Pair(0, power(2, 35)),
                Pair(
                    power(2, 34),
                    power(2, 34)
                )
            )))

        return successfulSwaps.flatMap { listOf(it.first, it.second) }.sorted().joinToString(separator = ",")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day24_test")
    val input = readInput("2024","Day24")

    check(part1(testInput) == 2024L)
    part1(input).println()

    part2(input).println()

}




