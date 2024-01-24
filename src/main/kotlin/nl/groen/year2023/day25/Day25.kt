package nl.groen.year2023.day25

import nl.groen.readInput
import java.util.*

private fun partOne (lines : List<String>): Long {

    val input :Map<String, List<String>> = lines.map { it.split(":")}.associate { Pair(it[0], it[1].trim().split(" "))}
    val mapOfPairs :MutableMap<String, Set<String>> = mutableMapOf()

    val stringsLHS = input.keys.toMutableSet()
    stringsLHS.addAll(input.values.flatten().toSet())

    for (string in stringsLHS) {

        val next2 = input.filterValues { it.contains(string) }.keys.toMutableSet()
        val next = input[string]?.toSet()
        if (next != null) {
            next2.addAll(next)
        }
        mapOfPairs[string] = next2
    }

    val pairs : Set<Pair<String, String>> = mapOfPairs.map { it.value.map { value -> Pair(it.key, value) } }.flatten().toSet()

    val listStrings = mapOfPairs.keys.sorted()
    val result : MutableMap<Pair<String,String>, Int> = mutableMapOf()

    val relevantPairs :List<Pair<String, String>> = listOf(Pair("gqm", "ddc"), Pair("rks", "kzh"), Pair("tnz", "dgt"))

    relevantPairs.forEach {
        mapOfPairs[it.first] = mapOfPairs[it.first]!!.filter { s -> s != it.second }.toSet()
        mapOfPairs[it.second] = mapOfPairs[it.second]!!.filter { s -> s != it.first }.toSet()
    }

    val componentsInGroup1 :MutableSet<String> = mutableSetOf(relevantPairs.first().first)

    do {
        val newComponentsInLoop :MutableSet<String> = mutableSetOf()
        componentsInGroup1.forEach {
            val newComponents = mapOfPairs[it]!!.filter { symbol -> !componentsInGroup1.contains(symbol) }
            newComponentsInLoop.addAll(newComponents)
        }
        componentsInGroup1.addAll(newComponentsInLoop)
    } while (newComponentsInLoop.size != 0)

    println(componentsInGroup1.size)

//    for (i in listStrings.indices) {
//        val source = listStrings[i]
//        for (destination in mapOfPairs[source]!!) {
//            val copyOfPairs: MutableSet<Pair<String, String>> = pairs.toMutableSet()
//    //        val path : Set<Pair<String,String>> = determinePath(copyOfPairs, listStrings[0], destination)
//    //        path.forEach {
//    //            val currentResult = result[it]
//    //            if (currentResult == null) {
//    //                result[it] = 1
//    //            } else {
//    //                result[it] = currentResult + 1
//    //            }
//    //        }
//
//
//            if (result[Pair(source, destination)] != null) {
//                println("No need to check $destination -> $source (again)")
//                componentsInGroup1++
//                continue
//            }
//
//            copyOfPairs.removeAll(determinePath(copyOfPairs, source, destination))
//            copyOfPairs.removeAll(determinePath(copyOfPairs, source, destination))
//            copyOfPairs.removeAll(determinePath(copyOfPairs, source, destination))
//            val resultFourthTry = determinePath(copyOfPairs, source, destination)
//
//            if (resultFourthTry.isEmpty()) {
//                // Destination belongs to other group
//                println("$destination does not belong to $source")
//                componentsInGroup2++
//            } else {
//                // Destination belongs to this group
//                println("$destination belongs to group of $source")
//                result[Pair(destination, source)] = 1
//                componentsInGroup1++
//            }
//
//        }
//
//
//    }

    return componentsInGroup1.size.toLong() * (mapOfPairs.size-componentsInGroup1.size.toLong())
}

data class State(val cost :Int, val current :String, val visited :List<String>)
fun determinePath(pairs: Set<Pair<String, String>>, source :String, destination :String) :Set<Pair<String, String>> {

    val pq = PriorityQueue { s1: State, s2: State -> s1.cost - s2.cost }
    pq.add(State(0, source, mutableListOf()))

    while (!pq.isEmpty()) {

        val state = pq.poll()

        if (state.cost > 12) {
            // We breken af als het meer dan 12 is.
            return emptySet()
        }

        if (state.current == destination) {
            println("$source -> $destination : ${state.visited.size}")

            val result :MutableSet<Pair<String, String>> = mutableSetOf(Pair(source, state.visited.first()))
            for (i in 0..<state.visited.size-1) {
                result.add(Pair(state.visited[i], state.visited[i+1]))
            }
            return result
        }

        val nextNodes = pairs.filter { it.first == state.current }.filter { !state.visited.contains(it.second) }

        nextNodes.forEach {
            val newVisited: MutableList<String> = state.visited.toMutableList()
            newVisited.add(it.second)
            pq.add(State(state.cost+1, it.second, newVisited))
        }

    }

    return emptySet()

}

private fun partTwo (lines : List<String>): Long {

    return 0L
}

fun main(args : Array<String>) {

    val lines = readInput("day25")
    println(partOne(lines))
    println(partTwo(lines))

}

