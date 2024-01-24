package nl.groen.year2023.day20

import nl.groen.calculateLCMOfListOfNumbers
import nl.groen.readInput
import java.util.*

enum class ModuleType(val symbol: String) {
    OUTPUT("O"), BROADCASTER("b"), FLIPFLOP("%"), CONJUNCTION("&");

    companion object {
        fun getBySymbol(input: String): ModuleType {
            return ModuleType.entries.firstOrNull { part -> input == part.symbol }!!
        }
    }
}
abstract class Module(val name: String, val type : ModuleType, val destinations: List<String>) {
    var status: Boolean = false

    abstract fun processPulse(isHigh: Boolean, source: String) : Boolean?
}

class Output(name: String, destinations: List<String>) : Module(name, ModuleType.OUTPUT, destinations) {
    override fun processPulse(isHigh: Boolean, source: String): Boolean? {
        return null
    }
}
class Broadcaster(name: String, destinations: List<String>) : Module(name, ModuleType.BROADCASTER, destinations) {
    override fun processPulse(isHigh: Boolean, source: String): Boolean {
        return isHigh
    }
}
class FlipFlop(name: String, destinations: List<String>) : Module(name, ModuleType.FLIPFLOP, destinations) {
    override fun processPulse(isHigh: Boolean, source: String): Boolean? {
        if (isHigh) {
            return null
        }

        status = !status
        return status
    }
}
class Conjunction(name: String, destinations: List<String>) : Module(name, ModuleType.CONJUNCTION, destinations) {

    var sources : MutableMap<String, Boolean> = mutableMapOf()
    override fun processPulse(isHigh: Boolean, source: String): Boolean {
        sources[source] = isHigh
        return !sources.all { it.value == true }
    }
}

data class State(val order: Long, val isHigh: Boolean, val source: String, val destination: String)

private fun partOne (lines : List<String>): Long {

    val modules: MutableMap<String, Module> = parseLines(lines)

    val queue = PriorityQueue { state1: State, state2: State -> state1.order.compareTo(state2.order) }
    var i = 1L
    var numberLowPulses = 0L
    var numberHighPulses = 0L
    queue.add(State(i, false,"button", "broadcaster"))
    while (!queue.isEmpty()) {
        val state = queue.poll()
        //println("${state.source} -${if (state.isHigh) {"high"} else "low"}-> ${state.destination}")
        if (state.isHigh) {
            numberHighPulses++
        } else {
            numberLowPulses++
        }
        var module = modules[state.destination]
        if (module == null) {
            module = Output(state.destination, mutableListOf())
            //("Create new output module ${module.name}")
            modules[state.destination] = module
        }
        val result = module.processPulse(state.isHigh, state.source)

        if (result != null) {
            for (destination in module.destinations) {
                queue.add(State(i, result, module.name, destination))
            }

        }

        if (queue.isEmpty() && i < 1000) {
            i++
            queue.add(State(i, false,"button", "broadcaster"))
        }
    }

    println("numberLowPulses: $numberLowPulses numberHighPulses: $numberHighPulses")
    return numberLowPulses * numberHighPulses
}

private fun parseLines(lines: List<String>): MutableMap<String, Module> {
    val modules: MutableMap<String, Module> = lines
        .map { it.split("->") }
        .associate {
            val type = ModuleType.getBySymbol(it[0].substring(0, 1))
            val destinations = it[1].trim().split(", ")
            val module = when (type) {
                ModuleType.BROADCASTER -> Broadcaster("broadcaster", destinations)
                ModuleType.FLIPFLOP -> FlipFlop(it[0].trim().replace(ModuleType.FLIPFLOP.symbol, ""), destinations)
                ModuleType.CONJUNCTION -> Conjunction(it[0].trim().replace(ModuleType.CONJUNCTION.symbol, ""), destinations)
                ModuleType.OUTPUT -> TODO()
            }

            return@associate Pair(module.name, module)
        }.toMutableMap()

    modules.values.filter { it.type == ModuleType.CONJUNCTION }.forEach {
        (it as Conjunction).sources = modules.values.filter { m -> m.destinations.contains(it.name) }.associate { m -> m.name to false }.toMutableMap()
    }

    modules["output"] = Output("output", mutableListOf())
    return modules
}

private fun partTwo (lines : List<String>): Long {

    val modules: MutableMap<String, Module> = parseLines(lines)

    val conjunctionModule = modules.values.filter { it.destinations.contains("rx") }.first()
    var modulesToConjunction = modules.values.filter { it.destinations.contains(conjunctionModule.name) }.toList()
    var least: List<List<Long>> = modulesToConjunction.map { determineStepsWhenModuleTrue(it.name, parseLines(lines)) }

    val differences = least.map {
        val differences = mutableListOf<Long>()
        for (i in 0..<it.size-1) {
            if (i==0) {
                differences.add(it[0])
            } else {
                differences.add(it[i+1]-it[i])
            }

        }
        return@map differences.first()
    }
    return calculateLCMOfListOfNumbers(differences)
//    return -1L
}

private fun determineStepsWhenModuleTrue(moduleName: String, modules: MutableMap<String, Module>) : List<Long> {

    val queue = PriorityQueue { state1: State, state2: State -> state1.order.compareTo(state2.order) }
    var i = 1L
    var order = 0L
    var resultList : MutableList<Long> = mutableListOf()
    println("determineStepsWhenModuleTrue for module $moduleName")

    queue.add(State(i, false,"button", "broadcaster"))
    while (!queue.isEmpty()) {
        val state = queue.poll()

        var module = modules[state.destination]
        if (module == null) {
            module = Output(state.destination, mutableListOf())
            //println("Create new output module ${module.name}")
            modules[state.destination] = module
        }

        val result = module.processPulse(state.isHigh, state.source)

        if (module.name == moduleName) {
            if (result == true) {
                println("$moduleName -> $i")
                resultList.add(i)
            }
        }

        if (result != null) {
            for (destination in module.destinations) {
                order++
                queue.add(State(order, result, module.name, destination))
            }

        }

        if (queue.isEmpty()) {

            if (i<2E4) {
                i++
                queue.add(State(0, false,"button", "broadcaster"))
            }
        }
    }

    return resultList
}

fun main(args : Array<String>) {

    val lines = readInput("day20")
    println(partOne(lines))
    println(partTwo(lines))

}

