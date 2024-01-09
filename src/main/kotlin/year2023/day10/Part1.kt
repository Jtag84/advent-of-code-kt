package year2023.day10

import commons.Coordinates2d
import commons.Part.Companion.part1
import year2023.day10.Direction.*
import kotlin.math.roundToInt

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 4) { pipeMap ->
    val (startDirection, _) = getStartDirectionAndFinalPipeMap(pipeMap)
    val pathLength = getPath(pipeMap, startDirection).count()
    (pathLength / 2.0).roundToInt()
}

fun getPath(
    pipeMap: Map<Coordinates2d, Tile>,
    startDirection: Pair<Coordinates2d, Direction>
): Sequence<Pair<Coordinates2d, Direction>> {
    return generateSequence(startDirection) { nextPositionDirection(pipeMap, it) }
        .takeWhile { it.first != startDirection.first || it.second == startDirection.second }
}

fun getStartDirectionAndFinalPipeMap(pipeMap: Map<Coordinates2d, Tile>): Pair<Pair<Coordinates2d, Direction>, Map<Coordinates2d, Tile>> {
    val start = pipeMap.entries.find { (_, tile) -> tile == Tile.START }?.key!!
    val startAllowedDirections = listOf(Pair(start, NORTH), Pair(start, SOUTH), Pair(start, EAST), Pair(start, WEST))
        .filter { nextPositionDirection(pipeMap, it) != null }.toSet()

    val startTile = Tile.toTile(startAllowedDirections.map { it.second.opposite() }.toSet())
    val finalPipeMap = pipeMap.toMutableMap()
    finalPipeMap.replace(start, startTile)
    val startDirection = startAllowedDirections.first()
    return Pair(startDirection, finalPipeMap.toMap())
}

private fun nextPositionDirection(
    pipeMap: Map<Coordinates2d, Tile>,
    currentPositionDirection: Pair<Coordinates2d, Direction?>,
): Pair<Coordinates2d, Direction>? {
    return currentPositionDirection.second?.let { direction ->
        val nextPosition = direction.move(currentPositionDirection.first)
        val nextDirection = pipeMap[nextPosition]!!.nextDirection(direction)
        nextDirection?.let { Pair(nextPosition, it) }
    }
}

