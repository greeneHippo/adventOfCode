package nl.groen.year2023.day11

import kotlin.math.abs

class GalaxyCoordinates(val x: Int, val y: Int) {

    fun shortestPathToGalaxy(otherGalaxy: GalaxyCoordinates) :Int {

        return abs(this.x - otherGalaxy.x) + abs(this.y - otherGalaxy.y)
    }
}
