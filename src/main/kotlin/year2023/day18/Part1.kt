package year2023.day18

import commons.Coordinates
import commons.Part.Companion.part1

fun main() {
    part1.runTest()
    part1.run()
}

val part1 = part1(inputParser, 62L) { digDirections ->
    calculateDigArea(digDirections)
}

fun calculateDigArea(digDirections: List<DigDirection>): Long {
    val trenchCorners = digDirections.fold(listOf(Coordinates(0, 0))) { acc, digDirection ->
        acc + digDirection.direction.move(acc.last(), digDirection.length)
    }

    val shoeLace = trenchCorners
        .zipWithNext { (x1, y1), (x2, y2) -> x1 * y2 - x2 * y1 }
        .sum()

    return (shoeLace + digDirections.sumOf { it.length }) / 2 + 1
}