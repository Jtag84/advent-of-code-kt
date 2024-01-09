package year2023.day21

import commons.*
import commons.Part.Companion.part1
import year2023.day21.GardenElement.ROCK
import year2023.day21.GardenElement.START

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 42) { gardenMap ->
    val start = gardenMap.first.entries.first { it.value == START }.key
    generateSequence(setOf(start)) { currentPossibleCoordinates ->
        currentPossibleCoordinates
            .flatMap { nextSteps(gardenMap, it) }.toSet()
    }.drop(64).first().count()
}

val cache = hashMapOf<Coordinates2d, Set<Coordinates2d>>()

fun nextSteps(gardenMap: Garden, coordinates: Coordinates2d): Set<Coordinates2d> {
    return cache.getOrPut(coordinates) {
        val (map, max) = gardenMap
        val (maxX, maxY) = max
        setOf(coordinates.north(), coordinates.east(), coordinates.west(), coordinates.south())
            .filter { it.x in 0..maxX && it.y in 0..maxY }
            .filter { map[it]!! != ROCK }
            .toSet()
    }
}