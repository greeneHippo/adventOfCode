package nl.groen.year2024

import nl.groen.println
import nl.groen.readInput
import java.util.*

fun main() {

    data class DiskElement(val id: Long?, val range :Long)

    fun calculateCheckSum(disk: TreeMap<Long, DiskElement>): Long {
        var result = 0L
        disk.filter { it.value.id != null }.forEach { pair ->
            for (i in LongRange(pair.key, pair.key + pair.value.range - 1)) {
                result += pair.value.id!! * i
            }
//            println("$$pair $result")
        }

        return result
    }

    fun fragment(disk: TreeMap<Long, DiskElement>) {
        while (true) {
            val firstEmptyEntry = disk.filter { it.value.id == null }.minByOrNull { it.key }
            val lastFilledEntry = disk.filter { it.value.id != null }.maxByOrNull { it.key }

            if (firstEmptyEntry!!.key > lastFilledEntry!!.key) {
                break
            }
            val emptyDiskElement = firstEmptyEntry.value
            val lastFilledDiskElement = lastFilledEntry.value

            if (emptyDiskElement.range > lastFilledDiskElement.range) {
                disk[firstEmptyEntry.key] = DiskElement(lastFilledDiskElement.id, lastFilledDiskElement.range)
                disk[firstEmptyEntry.key + lastFilledDiskElement.range] =
                    DiskElement(null, emptyDiskElement.range - lastFilledDiskElement.range)
                disk.remove(lastFilledEntry.key)
            }
            if (emptyDiskElement.range == lastFilledDiskElement.range) {
                disk[firstEmptyEntry.key] = DiskElement(lastFilledDiskElement.id, lastFilledDiskElement.range)
                disk.remove(lastFilledEntry.key)
            }
            if (emptyDiskElement.range < lastFilledDiskElement.range) {
                disk[firstEmptyEntry.key] = DiskElement(lastFilledDiskElement.id, emptyDiskElement.range)
                disk[lastFilledEntry.key] =
                    DiskElement(lastFilledDiskElement.id, lastFilledDiskElement.range - emptyDiskElement.range)
            }

        }
    }

    fun fragmentFullFiles(disk: TreeMap<Long, DiskElement>) {
        for (index in disk.filter { it.value.id != null }.keys.sortedDescending()) {
            val diskElement = disk[index]!!
            val emptyDiskElement = disk
                .filter { it.value.id == null && it.value.range >= diskElement.range}
                .minByOrNull { it.key }

            if (emptyDiskElement == null || emptyDiskElement.key > index) continue

            if (emptyDiskElement.value.range == diskElement.range) {
                disk[emptyDiskElement.key] = DiskElement(diskElement.id, diskElement.range)
                disk.remove(index)
            }
            if (emptyDiskElement.value.range > diskElement.range) {
                disk[emptyDiskElement.key] = DiskElement(diskElement.id, diskElement.range)
                disk[emptyDiskElement.key + diskElement.range] =
                    DiskElement(null, emptyDiskElement.value.range - diskElement.range)
                disk.remove(index)
            }
        }
    }

    fun init(input: List<String>): TreeMap<Long, DiskElement> {
        val disk: TreeMap<Long, DiskElement> = TreeMap<Long, DiskElement>()
        var diskIndex = 0L
        var id = 0L
        input.first().toList().foldIndexed(disk) { index, acc, int ->
            val numberOfElements = int.toString().toLong()
            if (index % 2 == 0) {
                acc[diskIndex] = (DiskElement(id, numberOfElements))
                id += 1
            } else {
                acc[diskIndex] = (DiskElement(null, numberOfElements))
            }
            diskIndex += numberOfElements
            acc
        }
        return disk
    }

    fun part1 (input : List<String>): Long {

        val disk: TreeMap<Long, DiskElement> = init(input)

        fragment(disk)

        return calculateCheckSum(disk)
    }

    fun part2 (input : List<String>): Long {

        val disk: TreeMap<Long, DiskElement> = init(input)

        fragmentFullFiles(disk)

        return calculateCheckSum(disk)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("2024","Day09_test")
    val input = readInput("2024","Day09")

    check(part1(testInput) == 1928L)
    part1(input).println()

    check(part2(testInput) == 2858L)
    part2(input).println()

}

