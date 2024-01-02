package nl.groen.day19

import nl.groen.readLines

class Part(val x: Int, val m: Int, val a: Int, val s: Int)
class PartRange(val x: IntRange, val m: IntRange, val a: IntRange, val s: IntRange)
class Workflow(val name :String, val steps: List<WorkflowStep>)
class WorkflowStep(val field: String, val greater: Boolean, val comparisonValue: Int, val result: String)

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
        if (step.field == "*") {
            return step.result
        }
        if (step.field == "x") {
            if (step.greater && part.x > step.comparisonValue) {
                return step.result
            }
            if (!step.greater && part.x < step.comparisonValue) {
                return step.result
            }
        }
        if (step.field == "m") {
            if (step.greater && part.m > step.comparisonValue) {
                return step.result
            }
            if (!step.greater && part.m < step.comparisonValue) {
                return step.result
            }
        }
        if (step.field == "a") {
            if (step.greater && part.a > step.comparisonValue) {
                return step.result
            }
            if (!step.greater && part.a < step.comparisonValue) {
                return step.result
            }
        }
        if (step.field == "s") {
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

    val indexEmptyLine = lines.indexOfFirst { it.isEmpty() }
    val workflows = lines.subList(0, indexEmptyLine).associate{
        val splitOnAccolade = it.substring(0,it.length-1).split("{")
        val workflowSteps = splitOnAccolade[1].split(",").map { s -> createWorkflowStep(s)}.toList()
        Pair(splitOnAccolade[0], workflowSteps)
    }


    var partRanges = hashMapOf(Pair(PartRange(IntRange(1, 4000), IntRange(1, 4000), IntRange(1, 4000), IntRange(1, 4000)), "in"))
    do {
        println(partRanges.values)
        partRanges = evaluateAllRanges(partRanges, workflows)
    } while (partRanges.values.any { it != "A" && it != "R"})

    return partRanges.filterValues { s -> s != "R" }
        .keys.map { it.x.count().toLong() * it.m.count().toLong() * it.a.count().toLong() * it.s.count().toLong()}.reduce { acc, i -> acc + i}
}

fun evaluateAllRanges(partRanges: HashMap<PartRange, String>, workflows: Map<String, List<WorkflowStep>>): HashMap<PartRange, String> {

    var result : HashMap<PartRange, String> = hashMapOf()

    partRanges.forEach{

        if (it.value == "A") {
            result[it.key] = it.value
            return@forEach
        }
        if (it.value == "R") {
            return@forEach
        }

        val workflowToProcess = workflows[it.value]
        val intermediateResult = applyToPartRange(it, workflowToProcess!!)
        result.putAll(intermediateResult)

    }

    return result
}

fun applyToPartRange(pair: Map.Entry<PartRange, String>, workflowToProcess: List<WorkflowStep>): HashMap<PartRange, String> {

    val result : HashMap<PartRange, String> = hashMapOf()
    var rangeToEvaluate = pair.key
    for (step in workflowToProcess) {
        if (step.field == "*") {
            result[rangeToEvaluate] = step.result
        }
        if (step.field == "x") {

            if (step.greater) {
                if (rangeToEvaluate.x.first > step.comparisonValue) {
                    result[rangeToEvaluate] = step.result
                } else if(rangeToEvaluate.x.last < step.comparisonValue) {
                    continue
                } else {
                    val higherPart = PartRange(IntRange(step.comparisonValue+1, rangeToEvaluate.x.last), rangeToEvaluate.m, rangeToEvaluate.a, rangeToEvaluate.s)
                    result[higherPart] = step.result
                    val lowerPart = PartRange(IntRange(rangeToEvaluate.x.first, step.comparisonValue), rangeToEvaluate.m, rangeToEvaluate.a,rangeToEvaluate.s)
                    rangeToEvaluate = lowerPart

                }
            } else {
                if (rangeToEvaluate.x.first > step.comparisonValue) {
                    continue
                } else if(rangeToEvaluate.x.last < step.comparisonValue) {
                    result[rangeToEvaluate] = step.result
                } else {
                    val lowerPart = PartRange(IntRange(rangeToEvaluate.x.first, step.comparisonValue-1), rangeToEvaluate.m, rangeToEvaluate.a, rangeToEvaluate.s)
                    result[lowerPart] = step.result
                    val higherPart = PartRange(IntRange(step.comparisonValue, rangeToEvaluate.x.last), rangeToEvaluate.m, rangeToEvaluate.a, rangeToEvaluate.s)
                    rangeToEvaluate = higherPart
                }
            }
        }
        if (step.field == "m") {

            if (step.greater) {
                if (rangeToEvaluate.m.first > step.comparisonValue) {
                    result[rangeToEvaluate] = step.result
                } else if(rangeToEvaluate.m.last < step.comparisonValue) {
                    continue
                } else {
                    val higherPart = PartRange(rangeToEvaluate.x, IntRange(step.comparisonValue+1, rangeToEvaluate.m.last) , rangeToEvaluate.a, rangeToEvaluate.s)
                    result[higherPart] = step.result
                    val lowerPart = PartRange(rangeToEvaluate.x, IntRange(rangeToEvaluate.m.first, step.comparisonValue), rangeToEvaluate.a,rangeToEvaluate.s)
                    rangeToEvaluate = lowerPart

                }
            } else {
                if (rangeToEvaluate.m.first > step.comparisonValue) {
                    continue
                } else if(rangeToEvaluate.m.last < step.comparisonValue) {
                    result[rangeToEvaluate] = step.result
                } else {
                    val lowerPart = PartRange(rangeToEvaluate.x, IntRange(rangeToEvaluate.m.first, step.comparisonValue-1), rangeToEvaluate.a, rangeToEvaluate.s)
                    result[lowerPart] = step.result
                    val higherPart = PartRange(rangeToEvaluate.x, IntRange(step.comparisonValue, rangeToEvaluate.m.last), rangeToEvaluate.a, rangeToEvaluate.s)
                    rangeToEvaluate = higherPart
                }
            }
        }
        if (step.field == "a") {

            if (step.greater) {
                if (rangeToEvaluate.a.first > step.comparisonValue) {
                    result[rangeToEvaluate] = step.result
                } else if(rangeToEvaluate.a.last < step.comparisonValue) {
                    continue
                } else {
                    val higherPart = PartRange(rangeToEvaluate.x, rangeToEvaluate.m, IntRange(step.comparisonValue+1, rangeToEvaluate.a.last), rangeToEvaluate.s)
                    result[higherPart] = step.result
                    val lowerPart = PartRange(rangeToEvaluate.x, rangeToEvaluate.m, IntRange(rangeToEvaluate.a.first, step.comparisonValue),rangeToEvaluate.s)
                    rangeToEvaluate = lowerPart

                }
            } else {
                if (rangeToEvaluate.a.first > step.comparisonValue) {
                    continue
                } else if(rangeToEvaluate.a.last < step.comparisonValue) {
                    result[rangeToEvaluate] = step.result
                } else {
                    val lowerPart = PartRange(rangeToEvaluate.x, rangeToEvaluate.m, IntRange(rangeToEvaluate.a.first, step.comparisonValue-1), rangeToEvaluate.s)
                    result[lowerPart] = step.result
                    val higherPart = PartRange(rangeToEvaluate.x, rangeToEvaluate.m, IntRange(step.comparisonValue, rangeToEvaluate.a.last), rangeToEvaluate.s)
                    rangeToEvaluate = higherPart
                }
            }
        }
        if (step.field == "s") {

            if (step.greater) {
                if (rangeToEvaluate.s.first > step.comparisonValue) {
                    result[rangeToEvaluate] = step.result
                } else if(rangeToEvaluate.s.last < step.comparisonValue) {
                    continue
                } else {
                    val higherPart = PartRange(rangeToEvaluate.x, rangeToEvaluate.m, rangeToEvaluate.a, IntRange(step.comparisonValue+1, rangeToEvaluate.s.last))
                    result[higherPart] = step.result
                    val lowerPart = PartRange(rangeToEvaluate.x, rangeToEvaluate.m, rangeToEvaluate.a,IntRange(rangeToEvaluate.s.first, step.comparisonValue))
                    rangeToEvaluate = lowerPart

                }
            } else {
                if (rangeToEvaluate.s.first > step.comparisonValue) {
                    continue
                } else if(rangeToEvaluate.s.last < step.comparisonValue) {
                    result[rangeToEvaluate] = step.result
                } else {
                    val lowerPart = PartRange(rangeToEvaluate.x, rangeToEvaluate.m, rangeToEvaluate.a,IntRange(rangeToEvaluate.s.first, step.comparisonValue-1))
                    result[lowerPart] = step.result
                    val higherPart = PartRange(rangeToEvaluate.x, rangeToEvaluate.m, rangeToEvaluate.a, IntRange(step.comparisonValue, rangeToEvaluate.s.last))
                    rangeToEvaluate = higherPart
                }
            }
        }
    }

        return result
}

fun main(args : Array<String>) {

    val lines = readLines("day19")
    //println(partOne(lines))
    println(partTwo(lines))

}

