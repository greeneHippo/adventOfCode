package nl.groen.day19

import nl.groen.readLines

class Part(val x: Int, val m: Int, val a: Int, val s: Int)
class Workflow(val name :String, val steps: List<WorkflowStep>)
class WorkflowStep(val s: String, val greater: Boolean, val comparisonValue: Int, val result: String)

private fun partOne (lines : List<String>): Long {

    val indexEmptyLine = lines.indexOfFirst { it.isEmpty() }
    val workflows = lines.subList(0, indexEmptyLine).associate{
            val splitOnAccolade = it.substring(0,it.length-1).split("{")
            val workflowSteps = splitOnAccolade[1].split(",").map { s -> createWorkflowStep(s)}.toList()
            Pair(splitOnAccolade[0], workflowSteps)
    }
    val parts = lines.subList(indexEmptyLine+1, lines.size).map {
        val splitOnComma =  it.substring(1, it.length-1).split(",")
        return@map Part(splitOnComma[0].substring(2).toInt(),
                        splitOnComma[1].substring(2).toInt(),
                        splitOnComma[2].substring(2).toInt(),
                        splitOnComma[3].substring(2).toInt()
        )
    }

    val result = parts
        .filter { "A" == evaluate(workflows, it) }
        .map { it.x.toLong() + it.m.toLong() + it.a.toLong() + it.s.toLong() }
        .reduce { acc, i -> acc + i}

    return result
}

fun evaluate(workflows: Map<String, List<WorkflowStep>>, part: Part): String {

    var result = "in"
    do {
        result = apply(workflows[result]!!, part)
    } while (result != "A" && result != "R")

    return result
}

fun apply(workflow: List<WorkflowStep>, part: Part): String {

    for (step in workflow) {
        if (step.s == "*") {
            return step.result
        }
        if (step.s == "x") {
            if (step.greater && part.x > step.comparisonValue) {
                return step.result
            }
            if (!step.greater && part.x < step.comparisonValue) {
                return step.result
            }
        }
        if (step.s == "m") {
            if (step.greater && part.m > step.comparisonValue) {
                return step.result
            }
            if (!step.greater && part.m < step.comparisonValue) {
                return step.result
            }
        }
        if (step.s == "a") {
            if (step.greater && part.a > step.comparisonValue) {
                return step.result
            }
            if (!step.greater && part.a < step.comparisonValue) {
                return step.result
            }
        }
        if (step.s == "s") {
            if (step.greater && part.s > step.comparisonValue) {
                return step.result
            }
            if (!step.greater && part.s < step.comparisonValue) {
                return step.result
            }
        }
    }

    throw RuntimeException("not possible")
}


fun createWorkflowStep(input: String) : WorkflowStep {

    val split = input.split(":")
    return if (split.count() == 2) {
        WorkflowStep(split[0].substring(0,1), split[0].substring(1,2) == ">", split[0].substring(2).toInt(), split[1])
    } else {
        WorkflowStep("*", true, -1, split[0])
    }

}

private fun partTwo (lines : List<String>): Long {

    return 0L
}

fun main(args : Array<String>) {

    val lines = readLines("day19")
    println(partOne(lines))
    println(partTwo(lines))

}

