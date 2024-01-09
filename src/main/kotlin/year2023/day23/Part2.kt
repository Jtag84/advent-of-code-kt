package year2023.day23

import commons.Coordinates2d
import commons.Part.Companion.part2
import commons.cardinals
import year2023.day23.TrailMapElement.FOREST

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 154L) { trailMap ->
    findLongestPath(trailMap)
}


fun findLongestPath(trailMap: TrailMap): Long {
    val maxX = trailMap.maxOf { it.key.x }
    val maxY = trailMap.maxOf { it.key.y }
    val start = Coordinates2d(1, 0)
    val goal = Coordinates2d(maxX - 1, maxY)

    val intersections = mutableMapOf<Coordinates2d, MutableList<Pair<Coordinates2d, Long>>>()
    getIntersections(trailMap, intersections, maxX, maxY, start, goal, start, start, 0)

    return findLongestPathFromIntersection(intersections, goal, listOf(start), 0)
}

fun findLongestPathFromIntersection(
    intersections: MutableMap<Coordinates2d, MutableList<Pair<Coordinates2d, Long>>>,
    goal: Coordinates2d,
    currentIntersectionPath: List<Coordinates2d>,
    currentLength: Long
): Long {
    val lastIntersection = currentIntersectionPath.last()
    if (lastIntersection == goal) {
        return currentLength
    }

    return intersections[lastIntersection]!!
        .filterNot { currentIntersectionPath.contains(it.first) }
        .maxOfOrNull {
            findLongestPathFromIntersection(
                intersections,
                goal,
                currentIntersectionPath + it.first,
                currentLength + it.second
            )
        } ?: 0
}

tailrec fun getIntersections(
    trailMap: Map<Coordinates2d, TrailMapElement>,
    intersections: MutableMap<Coordinates2d, MutableList<Pair<Coordinates2d, Long>>>,
    maxX: Long,
    maxY: Long,
    startIntersection: Coordinates2d,
    goal: Coordinates2d,
    previousStep: Coordinates2d,
    currentStep: Coordinates2d,
    numberOfSteps: Long
) {
    if (intersections[currentStep]?.map { it.first }?.contains(startIntersection)?.not() != false) {
        if (currentStep == goal) {
            intersections.getOrPut(startIntersection) { mutableListOf() }.add(currentStep to numberOfSteps)
            intersections.getOrPut(currentStep) { mutableListOf() }.add(startIntersection to numberOfSteps)
        } else {
            val nextSteps = when (trailMap[currentStep]!!) {
                FOREST -> throw IllegalStateException()
                else -> currentStep.cardinals()
                    .filter { it.x in 0..maxX && it.y in 0..maxY }
                    .filter { trailMap[it]!! != FOREST }
                    .toSet() - previousStep
            }

            when {
                nextSteps.size == 1 -> {
                    getIntersections(
                        trailMap,
                        intersections,
                        maxX,
                        maxY,
                        startIntersection,
                        goal,
                        currentStep,
                        nextSteps.first(),
                        numberOfSteps + 1
                    )
                }

                nextSteps.size > 1 -> {
                    intersections.getOrPut(startIntersection) { mutableListOf() }.add(currentStep to numberOfSteps)
                    intersections.getOrPut(currentStep) { mutableListOf() }.add(startIntersection to numberOfSteps)
                    nextSteps.forEach {
                        getIntersections(
                            trailMap,
                            intersections,
                            maxX,
                            maxY,
                            currentStep,
                            goal,
                            currentStep,
                            it,
                            1
                        )
                    }
                }
            }
        }
    }
}