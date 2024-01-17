package nl.groen.day24

import nl.groen.readLines
import org.jetbrains.kotlinx.multik.api.linalg.solve
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray

data class PositionDouble(val x: Double, val y: Double, val z: Double)
data class Hailstone(val index : Int, val x: Double, val y: Double, val z: Double, val vx: Double, val vy: Double, val vz: Double)
data class HailstoneLong(val index : Int, val x: Long, val y: Long, val z: Long, val vx: Long, val vy: Long, val vz: Long)
private fun partOne (lines : List<String>, min :Double, max :Double): Long {

    val hailstones = lines.mapIndexed { index, line ->

        val split = line.split("@")
        val position = split[0].split(",").map { s -> s.trim().toDouble() }
        val velocity = split[1].split(",").map { s -> s.trim().toDouble() }
        return@mapIndexed Hailstone(index, position[0], position[1], position[2], velocity[0], velocity[1], velocity[2])
    }
    var result = 0L
    for (i in hailstones.indices) {
        for ( j in i ..< hailstones.size) {
            if (j == i) {
                continue
            }
            val position = determineCollisionXY(hailstones, i, j)
            if (position != null && position.x in min..max && (position.y in min..max)) {
                result++
            }
        }
    }

    return result
}

private fun determineCollisionXY(hailstones: List<Hailstone>, i: Int, j: Int) : PositionDouble? {
    val first = hailstones[i]
    val second = hailstones[j]
    val timeFirst = (second.x - first.x + (second.vx / second.vy) * (first.y - second.y)) / (first.vx - (first.vy * second.vx / second.vy))

    if (timeFirst < 0) {
        return null
    }

    val timeSecond = (first.x - second.x + (first.vx / first.vy) * (second.y - first.y)) / (second.vx - (second.vy * first.vx / first.vy))
    if (timeSecond < 0) {
        return null
    }

    val newPosition = PositionDouble(first.x + first.vx * timeFirst, first.y + first.vy * timeFirst, first.z)

    return newPosition
}

private fun determineCollisionXZ(hailstones: List<Hailstone>, i: Int, j: Int) : PositionDouble? {
    val first = hailstones[i]
    val second = hailstones[j]
    val timeFirst = (second.x - first.x + (second.vx / second.vz) * (first.z - second.z)) / (first.vx - (first.vz * second.vx / second.vz))

    if (timeFirst < 0) {
        return null
    }

    val timeSecond = (first.x - second.x + (first.vx / first.vz) * (second.z - first.z)) / (second.vx - (second.vz * first.vx / first.vz))
    if (timeSecond < 0) {
        return null
    }

    val newPosition = PositionDouble(first.x + first.vx * timeFirst, first.z + first.vz * timeFirst, first.z)

    return newPosition
}

private fun partTwo (lines : List<String>): Long {

    val hailstones = lines.mapIndexed { index, line ->

        val split = line.split("@")
        val position = split[0].split(",").map { s -> s.trim().toDouble() }
        val velocity = split[1].split(",").map { s -> s.trim().toDouble() }
        return@mapIndexed Hailstone(index, position[0], position[1], position[2], velocity[0], velocity[1], velocity[2])
    }

    val hailstonesSameY = hailstones.groupBy { it.y }.filter { it.value.size > 1 }.values.first()

    var y0 = hailstonesSameY[0].y
    val vy0 = 25.0
//    println("times: $t1 - $t2")
//
//    println("collision XY: " + determineCollisionXY(hailstones, hailstonesSameY[0].index, hailstonesSameY[1].index))
//    println("collision XZ: " + determineCollisionXZ(hailstones, hailstonesSameY[0].index, hailstonesSameY[1].index))
//
//    val collision_x1 = PositionDouble(hailstones[0].x + t1*hailstones[0].vx, hailstones[0].y + t1*hailstones[0].vy, hailstones[0].z + t1*hailstones[0].vz)
//    println("$collision_x1")
    var i = 0
    do {
        val first = hailstones[0]
        val second = hailstones[1]
        val t1 =(y0-first.y)/(first.vy-25)
        val t2 =(y0-second.y)/(second.vy-25)


        // x0 v0x z0 v0z
        val a = mk.ndarray(mk[
            mk[1.0, t1 , 0.0, 0.0],
            mk[0.0, 0.0, 1.0, t1 ],
            mk[1.0, t2 , 0.0, 0.0],
            mk[0.0, 0.0, 1.0, t2 ]
        ])
        val b = mk.ndarray(mk[
            first.x + first.vx*t1,
            first.z + first.vz*t1,
            second.x + second.vx*t2,
            second.z + second.vz*t2
        ])
        val result = mk.linalg.solve(a, b)

        val x0 = result.data[0]
        val vx0 = result.data[1]
        val z0 = result.data[2]
        val vz0 = result.data[3]

        val third = hailstones[2]
        val t3 =(y0-third.y)/(hailstones[2].vy-25)

        if (
            (x0 + vx0 * t3) == (third.x + third.vx * t3) &&
            (y0 + vy0 * t3) == (third.y + third.vy * t3) &&
            (z0 + vz0 * t3) == (third.z + third.vz * t3)
        ) {
            return (x0 + y0 + z0).toLong()
        }

        i++
        if (i % 10000 == 0) {
            println(i)
        }

    } while (true)
}

fun main(args : Array<String>) {

    val lines = readLines("day24")
    println(partOne(lines, 200000000000000.0, 400000000000000.0 ))
    println(partTwo(lines))

}

