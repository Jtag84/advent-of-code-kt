package year2019.day12

import arrow.core.andThen
import com.google.common.collect.Sets
import commons.Coordinates3d
import commons.Part.Companion.part1
import kotlin.math.abs

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 1940L) { moonPositions ->
    val numberOfSteps = if(isTest) {100} else {1000}
    generateSequence(moonPositions, ::applyGravity.andThen(::caculateNewPosition) )
        .drop(numberOfSteps)
        .first()
        .sumOf { calculateEnergy(it) }
}

fun calculateEnergy(moonPosition: MoonPosition) : Long {
    val (x, y, z) = moonPosition.first
    val (vx, vy, vz) = moonPosition.second
    return (abs(x) + abs(y) + abs(z)) * (abs(vx) + abs(vy) + abs(vz))
}

fun caculateNewPosition(currentMoonPositions: Set<MoonPosition>): Set<MoonPosition> {
    return currentMoonPositions.map {(coordinates, velocity) ->
        val (x, y, z) = coordinates
        val (vx, vy, vz) = velocity

        Coordinates3d(x + vx, y + vy, z + vz) to velocity
    }.toSet()
}

fun applyGravity(currentMoonPositions: Set<MoonPosition>): Set<MoonPosition> {
    val coordinatesToVelocityMap = currentMoonPositions.toMap().toMutableMap()
    Sets.combinations(currentMoonPositions, 2).forEach {
        val moon1 = it.first().first
        val (moon1X, moon1Y, moon1Z) = moon1
        val moon2 = it.last().first
        val (moon2X, moon2Y, moon2Z) = moon2

        val (moon1VelocityX, moon1VelocityY, moon1VelocityZ) = coordinatesToVelocityMap[moon1]!!
        val (moon2VelocityX, moon2VelocityY, moon2VelocityZ) = coordinatesToVelocityMap[moon2]!!

        var newMoon1VelocityX = moon1VelocityX
        var newMoon1VelocityY = moon1VelocityY
        var newMoon1VelocityZ = moon1VelocityZ
        var newMoon2VelocityX = moon2VelocityX
        var newMoon2VelocityY = moon2VelocityY
        var newMoon2VelocityZ = moon2VelocityZ

        when {
            moon1X < moon2X -> {
                newMoon1VelocityX++
                newMoon2VelocityX--
            }
            moon1X > moon2X -> {
                newMoon1VelocityX--
                newMoon2VelocityX++
            }
        }

        when {
            moon1Y < moon2Y -> {
                newMoon1VelocityY++
                newMoon2VelocityY--
            }
            moon1Y > moon2Y -> {
                newMoon1VelocityY--
                newMoon2VelocityY++
            }
        }

        when {
            moon1Z < moon2Z -> {
                newMoon1VelocityZ++
                newMoon2VelocityZ--
            }
            moon1Z > moon2Z -> {
                newMoon1VelocityZ--
                newMoon2VelocityZ++
            }
        }

        coordinatesToVelocityMap[moon1] = Velocity(newMoon1VelocityX, newMoon1VelocityY, newMoon1VelocityZ)
        coordinatesToVelocityMap[moon2] = Velocity(newMoon2VelocityX, newMoon2VelocityY, newMoon2VelocityZ)
    }
    return coordinatesToVelocityMap.entries.map { it.key to it.value }.toSet()
}