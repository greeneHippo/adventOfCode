package nl.groen

data class Pointer(val name :String, var size : Long, var subPointers: List<String>)
fun main() {

    fun determineSize(pointers: MutableMap<String, Pointer>, directoryName: String) : Long {
        val pointer = pointers[directoryName]!!
        return pointer.size.toLong() + pointer.subPointers.sumOf { determineSize(pointers, directoryName + "/" + it) }
    }

    fun determineDirectorySizes(input: List<String>): List<Long> {
        val actions: MutableList<MutableList<String>> = mutableListOf()
        input.fold(actions) { acc, s ->
            if (s.startsWith("$")) {
                acc.add(mutableListOf())
                acc.last().add(s)
            } else {
                acc.last().add(s)
            }
            acc
        }

        val pointers: MutableMap<String, Pointer> = mutableMapOf()
        var currentPointer = ""
        actions.fold(pointers) { acc, action ->

            val command = action.first()

            if (command.contains("cd /")) {
                val name = ""
                currentPointer = ""
                acc[currentPointer] = Pointer(name, 0, mutableListOf())
            } else if (command.contains("cd ..")) {
                currentPointer = currentPointer.replaceAfterLast("/", "").dropLast(1)
            } else if (command.contains("cd")) {
                val name = command.drop(5).trim()
                currentPointer = currentPointer + "/" + name
                acc[currentPointer] = Pointer(name, 0, mutableListOf())
            } else if (command.startsWith("$ ls")) {
                val directories = action.subList(1, action.size).map { it.toString() }.filter { it.startsWith("dir") }.map {
                    val (_, name) = it.split(" ")
                    name
                }
                val files = action.subList(1, action.size).map { it.toString() }.filter { !it.startsWith("dir") }.map {
                    val (size, name) = it.split(" ")
                    Pointer(currentPointer + "/" + name, size.toLong(), mutableListOf())
                }

                acc[currentPointer]!!.subPointers = directories
                acc[currentPointer]!!.size = files.sumOf { it.size }
                return@fold acc
            } else {
                throw Error("not implemented")
            }

            println(currentPointer)

            acc
        }

        val directorySizes = pointers.map { t ->
            determineSize(pointers, t.key)
        }
        return directorySizes
    }

    fun part1 (input : List<String>): Long {

        val directorySizes = determineDirectorySizes(input)

        return directorySizes.filter { it <= 100000L }.sum()
    }

    fun part2 (input : List<String>): Long {

        val directorySizes = determineDirectorySizes(input)

        val amountOfSpaceToDelete = directorySizes.max() - 40000000L
        return directorySizes.filter { it >= amountOfSpaceToDelete }.min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    val input = readInput("Day07")

    check(part1(testInput) == 95437L)
    part1(input).println()

    check(part2(testInput) == 24933642L)
    part2(input).println()

}

