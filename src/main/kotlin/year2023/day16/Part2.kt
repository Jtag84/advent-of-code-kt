package year2023.day16

import commons.Coordinates
import commons.Direction.*
import commons.Part.Companion.part2

fun main() {
    part2.runTest()
    part2.run()
}

val part2 = part2(inputParser, 51) { grid ->
    val maxX = grid.keys.map { it.x }.max()
    val maxY = grid.keys.map { it.y }.max()
    val topAndBottomStartPositions =
        (0..maxX).flatMap { listOf(Coordinates(it, 0) to DOWN, Coordinates(it, maxY) to UP) }
    val leftAndRightStartPositions =
        (0..maxY).flatMap { listOf(Coordinates(0, it) to RIGHT, Coordinates(maxX, it) to LEFT) }

    (topAndBottomStartPositions + leftAndRightStartPositions).asSequence().map { startPosition ->
        movingBeam(grid, startPosition, mutableSetOf()).map { it.first }.distinct().count()
    }
        .max()
}