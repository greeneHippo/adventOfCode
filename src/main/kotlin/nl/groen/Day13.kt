package nl.groen

fun main() {

    data class Element(val string: String, val wasList: Boolean = true);

    fun splitIndex(string: String): Int {
        val indices = string.mapIndexed { i, char ->
            val subString = string.substring(0, i)
            val isSplit = char == ',' && subString.count { it == '[' } == subString.count { it == ']' } + 1

            if (isSplit) {
                return@mapIndexed i
            } else {
                return@mapIndexed 0
            }
        }

        return if (indices.count { it != 0} == 0) {
            return 0
        } else {
            indices.first{it != 0}
        }
    }

    fun split(string: String, splitIndex: Int): Pair<String, String> {
        val firstPart = string.substring(1, splitIndex)
        val secondPart = '[' + string.substring(splitIndex + 1, string.length)
        return Pair(firstPart, secondPart)
    }

    fun removeBrackets(element: Element): Element {
        return if (element.string.isNotEmpty() && element.string.first() == '[' && element.string.last() == ']') {
            Element(element.string.drop(1).dropLast(1), element.wasList)
        } else {
            Element(element.string, false)
        }
    }

    fun isList(element: Element): Boolean {
        return element.string.isNotEmpty() && element.string.first() == '[' && element.string.last() == ']'
    }
    
    fun  inOrder(first: Element, second: Element): Boolean? {

        val splitIndexFirst = splitIndex(first.string)
        val splitIndexSecond = splitIndex(second.string)

        // We can split both sides of the string so let's do that first and run it recursively.
        if (splitIndexFirst != 0 && splitIndexSecond != 0) {

            val (firstLeft, firstRight) = split(first.string, splitIndexFirst)
            val (secondLeft, secondRight) = split(second.string, splitIndexSecond)

            val result = inOrder(Element(firstLeft, first.wasList), Element(secondLeft, first.wasList))
            return result ?: inOrder(Element(firstRight, first.wasList), Element(secondRight, first.wasList))
        }

        if (isList(first) || isList(second)) {
            return inOrder(removeBrackets(first), removeBrackets(second))
        }

        // No lists in list anymore. Compare the values
        var listFirst = if (first.string.isNotEmpty()) {
            first.string.split(',').map { it.toInt() }
        } else {
            mutableListOf()
        }
        var listSecond = if (second.string.isNotEmpty()) {
            second.string.split(',').map { it.toInt() }
        } else {
            mutableListOf()
        }
        while (true) {

            // If both list end up empty simultaneously. The order is correct if the first list does not originate from a list.
            if (first.wasList == second.wasList && listFirst.isEmpty() && listSecond.isEmpty()) {
                return null
            }

            // If both have the same level of lists, if the second runs out, it is false, if the first runs out true
            if (first.wasList == second.wasList) {
                if (listSecond.isEmpty()) {
                    return false
                } else if (listFirst.isEmpty()) {
                    return true
                }
            }

            // If both do not have the same level of lists, the first can only run out when the second is a list, and vice versa
            if (listSecond.isEmpty()) {
                return false
            }
            if (listFirst.isEmpty()) {
                return true
            }

            if (listFirst.first() != listSecond.first()) {
                return listFirst.first() < listSecond.first()
            }

            listFirst = listFirst.drop(1)
            listSecond = listSecond.drop(1)
        }

        return true
    }

    fun part1 (input : List<String>): Long {

        val groupStrings : MutableList<MutableList<Element>> = mutableListOf(mutableListOf())
        input.fold(groupStrings) { acc, s ->
            if (s.isEmpty()) {
                acc.add(mutableListOf())
            } else {
                acc.last().add(Element(s, true))
            }
            acc
        }

        val indexStringInOrder = groupStrings
            .mapIndexed { index, strings -> if (inOrder(strings[0], strings[1]) != false) { println("${index+1} inOrder"); return@mapIndexed index + 1L} else { println("${index+1} notInOrder");return@mapIndexed 0L }}
            .filter { it != 0L }

        return indexStringInOrder.reduce { acc, long -> acc + long }
    }

    fun part2 (input : List<String>): Long {

        return input.size.toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    val input = readInput("Day13")

    check(part1(testInput) == 13L)
    part1(input).println()

    check(part2(testInput) == 1L)
    part2(input).println()

}

