package year2023.day21

import commons.*
import commons.Part.Companion.part2

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

private const val NUMBER_OF_STEPS = 26_501_365

val part2 = part2(inputParser, 394693535848011) { gardenMap ->
    val start = gardenMap.first.entries.first { it.value == GardenElement.START }.key

    // This follow a quadratic growth by the garden size. It's also cyclic on each value, so we start by skipping the number of steps
    // from the modulo of the garden size.
    // Then we find a, b, and c in y=a*x^2 + b*x + c

    val gardenSize = gardenMap.second.first + 1
    val firstValuesToDrop = NUMBER_OF_STEPS % gardenSize

    val quadraticGrowthYValues = generateSequence(setOf(start)) { currentPossibleCoordinates ->
        currentPossibleCoordinates.map { nextStepsPart2(gardenMap, it) }
            .flatten()
            .toSet()
    }
        .drop(firstValuesToDrop.toInt())
        .filterIndexed { index, _ -> index % gardenSize.toInt() == 0 }
        .map { it.count() }
        .take(3)
        .toList()

    val firstDerivative = quadraticGrowthYValues.zipWithNext { a, b -> b - a }
    val secondDerivative = firstDerivative.zipWithNext { a, b -> b - a }

    val c = quadraticGrowthYValues[0]
    val a = secondDerivative[0] / 2  // second derivative = 2a
    val b = quadraticGrowthYValues[1] - c - a

    val x = NUMBER_OF_STEPS / gardenSize

    a * (x * x) + b * x + c
}

fun nextStepsPart2(gardenMap: Garden, coordinates: Coordinates): List<Coordinates> {
    val (map, max) = gardenMap
    val (maxX, maxY) = max
    return listOf(coordinates.north(), coordinates.east(), coordinates.west(), coordinates.south())
        .filter {
            map[Coordinates(adjustCoordinates(it.x, maxX), adjustCoordinates(it.y, maxY))]!! != GardenElement.ROCK
        }
}

fun adjustCoordinates(axis: Long, max: Long): Long {
    val adjustedAxis = axis % (max + 1)
    return if (adjustedAxis < 0) {
        (max + 1) + adjustedAxis
    } else {
        adjustedAxis
    }
}