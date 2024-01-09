package year2023.day23

import commons.*
import commons.Part.Companion.part1
import year2023.day23.TrailMapElement.*

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 94) { trailMap ->
    val maxX = trailMap.maxOf { it.key.x }
    val maxY = trailMap.maxOf { it.key.y }
    val start = Coordinates2d(1, 0)
    val goal = Coordinates2d(maxX - 1, maxY)
    findAllPaths(trailMap, goal, maxX, maxY, listOf(start)).maxOf { it.count() - 1 }
}

fun findAllPaths(
    trailMap: TrailMap,
    goal: Coordinates2d,
    maxX: Long,
    maxY: Long,
    currentPath: List<Coordinates2d>
): List<List<Coordinates2d>> {
    val last2 = currentPath.takeLast(2)
    val last = last2.last()

    if (last == goal) {
        return listOf(currentPath)
    }

    val beforeLast = last2.first()
    val nextSteps = when (trailMap[last]!!) {
        FOREST -> throw IllegalStateException()
        PATH -> last.cardinals()
            .filter { it.x in 0..maxX && it.y in 0..maxY }
            .toSet()

        SLOPE_NORTH -> setOf(last.north())
        SLOPE_SOUTH -> setOf(last.south())
        SLOPE_EAST -> setOf(last.east())
        SLOPE_WEST -> setOf(last.west())
    }
        .filter { trailMap[it]!! != FOREST } - beforeLast

    return nextSteps.flatMap { findAllPaths(trailMap, goal, maxX, maxY, currentPath + it) }
}