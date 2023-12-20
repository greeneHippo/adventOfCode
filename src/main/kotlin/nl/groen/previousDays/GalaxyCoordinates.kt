package nl.groen.previousDays

import kotlin.math.abs

class GalaxyCoordinates(val x: Int, val y: Int) {

    fun shortestPathToGalaxy(otherGalaxy: GalaxyCoordinates) :Int {

        return abs(this.x - otherGalaxy.x) + abs(this.y - otherGalaxy.y)
    }
}
