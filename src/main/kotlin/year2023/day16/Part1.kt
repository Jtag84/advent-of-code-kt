package year2023.day16

import commons.*
import commons.Direction.*
import commons.Part.Companion.part1
import year2023.day16.GridElement.*

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 46) { grid ->
    movingBeam(grid, Coordinates(0, 0) to RIGHT, mutableSetOf()).map { it.first }.distinct().count()
}

fun movingBeam(grid: Grid, position: Position, visitedPositions: MutableSet<Position>): List<Position> {
    val (coordinate, direction) = position
    val currentGridElement = grid[coordinate]
    if (currentGridElement == null || visitedPositions.contains(position)) {
        return emptyList()
    }
    visitedPositions.add(position)
    val nextPositions = when {
        currentGridElement == EMPTY -> listOf(direction.move(coordinate) to direction)
        currentGridElement == LEFT_MIRROR && direction == RIGHT -> listOf(coordinate.down() to DOWN)
        currentGridElement == LEFT_MIRROR && direction == UP -> listOf(coordinate.left() to LEFT)
        currentGridElement == LEFT_MIRROR && direction == LEFT -> listOf(coordinate.up() to UP)
        currentGridElement == LEFT_MIRROR && direction == DOWN -> listOf(coordinate.right() to RIGHT)
        currentGridElement == RIGHT_MIRROR && direction == RIGHT -> listOf(coordinate.up() to UP)
        currentGridElement == RIGHT_MIRROR && direction == UP -> listOf(coordinate.right() to RIGHT)
        currentGridElement == RIGHT_MIRROR && direction == LEFT -> listOf(coordinate.down() to DOWN)
        currentGridElement == RIGHT_MIRROR && direction == DOWN -> listOf(coordinate.left() to LEFT)
        currentGridElement == VERTICAL_SPLITTER && (direction in setOf(RIGHT, LEFT)) -> listOf(
            coordinate.up() to UP,
            coordinate.down() to DOWN
        )

        currentGridElement == VERTICAL_SPLITTER && (direction in setOf(
            UP,
            DOWN
        )) -> listOf(direction.move(coordinate) to direction)

        currentGridElement == HORIZONTAL_SPLITTER && (direction in setOf(RIGHT, LEFT)) -> listOf(
            direction.move(
                coordinate
            ) to direction
        )

        currentGridElement == HORIZONTAL_SPLITTER && (direction in setOf(UP, DOWN)) -> listOf(
            coordinate.left() to LEFT,
            coordinate.right() to RIGHT
        )

        else -> throw IllegalStateException()
    }

    return listOf(position) + nextPositions.flatMap {
        movingBeam(grid, it, visitedPositions)
    }
}

