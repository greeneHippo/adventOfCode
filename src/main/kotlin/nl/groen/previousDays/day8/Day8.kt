package nl.groen.previousDays.day8

import nl.groen.readLines
import org.apache.commons.lang3.StringUtils

private fun partOne (lines : List<String>): Long {

    val instructions = lines[0]
    val nodes = lines.subList(2, lines.size).associate {
        val splitted = StringUtils.split(it, "=")
        val left = StringUtils.substring(splitted[1], 2, 5)
        val right = StringUtils.substring(splitted[1], 7, 10)
        val node = Pair(left, right)
        splitted[0].trim() to node
    }

    return determinesStepsFromSingleNodeToZZZ(nodes, instructions, "AAA")
}

private fun determinesStepsFromSingleNodeToZZZ(nodes: Map<String, Pair<String, String>>, instructions: String, startingNode: String): Long {
    var i = 0
    var node = nodes[startingNode]
    println("AAA")
    do {
        if (instructions[i % instructions.length] == 'L') {
            println("left: ${node?.first}")
            if (node?.first == "ZZZ") {
                return i + 1L
            }
            node = nodes[node?.first]

        } else {
            println("right: ${node?.second}")
            if (node?.second == "ZZZ") {
                return i + 1L
            }
            node = nodes[node?.second]
        }
        i++
    } while (true)
}

private fun determinesStepsFromSingleNode(nodes: Map<String, Pair<String, String>>, instructions: String, startingNode: String): Long {
    var i = 0
    var node = nodes[startingNode]
    do {
        if (instructions[i % instructions.length] == 'L') {
            //println("left: ${node?.first}")
            if (node?.first?.substring(2,3) == "Z") {
                return i + 1L
            }
            node = nodes[node?.first]

        } else {
            //println("right: ${node?.second}")
            if (node?.second?.substring(2,3) == "Z") {
                return i + 1L
            }
            node = nodes[node?.second]
        }
        i++
    } while (true)
}

private fun partTwo (lines : List<String>): Long {

    val instructions = lines[0]
    val nodes = lines.subList(2, lines.size).associate {
        val splitted = StringUtils.split(it, "=")
        val left = StringUtils.substring(splitted[1], 2, 5)
        val right = StringUtils.substring(splitted[1], 7, 10)
        val node = Pair(left, right)
        splitted[0].trim() to node
    }

    var startingNodes = nodes.keys.filter { it.substring(2,3) == "A" }
    var steps = mutableListOf<Long>()
    for (startingNode in startingNodes) {
        var step = determinesStepsFromSingleNode(nodes, instructions, startingNode)
        steps.add(step)
        println("$startingNode -> $step")
    }


    return steps.reduce{element1, element2 -> leastCommonDemoninator(element1, element2) }
}

fun main(args : Array<String>) {

    val lines = readLines("day8")
    //println(partOne(lines))
    println(partTwo(lines))

}

fun leastCommonDemoninator(n1: Long, n2: Long): Long {

    // maximum number between n1 and n2 is stored in lcm
    var lcm: Long = if (n1 > n2) n1 else n2

    // Always true
    while (true) {
        if (lcm % n1 == 0L && lcm % n2 == 0L) {
            println("The LCM of $n1 and $n2 is $lcm.")
            return lcm
        }
        lcm += n1
    }
}

