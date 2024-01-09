package year2023.day16

import commons.Coordinates2d
import commons.Direction.*
import commons.Part.Companion.part2

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 51) { grid ->
    val maxX = grid.keys.map { it.x }.max()
    val maxY = grid.keys.map { it.y }.max()
    val topAndBottomStartPositions =
        (0..maxX).flatMap { listOf(Coordinates2d(it, 0) to DOWN, Coordinates2d(it, maxY) to UP) }
    val leftAndRightStartPositions =
        (0..maxY).flatMap { listOf(Coordinates2d(0, it) to RIGHT, Coordinates2d(maxX, it) to LEFT) }

    (topAndBottomStartPositions + leftAndRightStartPositions).asSequence().map { startPosition ->
        movingBeam(grid, startPosition, mutableSetOf()).map { it.first }.distinct().count()
    }
        .max()
}