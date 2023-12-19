package year2023.day14

import arrow.core.partially1
import commons.Coordinates
import commons.Part.Companion.part2
import commons.right
import commons.rotateClockwise

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 64L) { platformMap ->
    val numberOfRows = platformMap.maxOf { it.key.y } + 1

    // looking at the generated sequence, after a large number like 200 it repeats every 14 elements
    val repeatingSequence = generateSequence(platformMap, ::tiltCycle).drop(200).take(14)
        .map {
            it.toList()
                .filter { it.second == Rock.ROUND_ROCK }
                .map { it.first }
                .sumOf(calculateRockLoad.partially1(numberOfRows))
        }
        .toList()

    repeatingSequence[(1_000_000_000 - 200) % 14]
}

val cache = hashMapOf<Map<Coordinates, Rock>, Map<Coordinates, Rock>>()

fun tiltCycle(platformMap: Map<Coordinates, Rock>): Map<Coordinates, Rock> {
    return cache.getOrPut(platformMap) {
        val tiltedNorthMap = tiltNorth(platformMap)
        val tiltedWestMap = rotateMapCounterClockwiseAndTiltNorth(tiltedNorthMap)
        val tiltedSouthMap = rotateMapCounterClockwiseAndTiltNorth(tiltedWestMap)
        val tiltedEastMap = rotateMapCounterClockwiseAndTiltNorth(tiltedSouthMap)
        rotateCounterClockwise(tiltedEastMap)
    }
}

fun rotateMapCounterClockwiseAndTiltNorth(platformMap: Map<Coordinates, Rock>): Map<Coordinates, Rock> {
    val rotatedMap = rotateCounterClockwise(platformMap)
    return tiltNorth(rotatedMap)
}

private fun rotateCounterClockwise(platformMap: Map<Coordinates, Rock>): Map<Coordinates, Rock> {
    val numberOfRows = platformMap.maxOf { it.key.y }
    val rotatedMap = platformMap.toList().associate { it.first.rotateClockwise().right(numberOfRows) to it.second }
    return rotatedMap
}

fun tiltNorth(platformMap: Map<Coordinates, Rock>): Map<Coordinates, Rock> {
    val rocksByRow = platformMap.entries.groupBy({ it.key.y }, { it.key to it.value })
    val tiltedNorthRock = tiltNorth(rocksByRow)
    val newPlatformMap = platformMap.toMutableMap()
    newPlatformMap.replaceAll { coordinates, rock ->
        when {
            tiltedNorthRock.contains(coordinates) -> Rock.ROUND_ROCK
            rock == Rock.ROUND_ROCK -> Rock.EMPTY
            else -> rock
        }
    }

    return newPlatformMap.toMap()
}