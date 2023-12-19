package year2023.day10

import commons.Coordinates
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
    pipeMap: Map<Coordinates, Tile>,
    startDirection: Pair<Coordinates, Direction>
): Sequence<Pair<Coordinates, Direction>> {
    return generateSequence(startDirection) { nextPositionDirection(pipeMap, it) }
        .takeWhile { it.first != startDirection.first || it.second == startDirection.second }
}

fun getStartDirectionAndFinalPipeMap(pipeMap: Map<Coordinates, Tile>): Pair<Pair<Coordinates, Direction>, Map<Coordinates, Tile>> {
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
    pipeMap: Map<Coordinates, Tile>,
    currentPositionDirection: Pair<Coordinates, Direction?>,
): Pair<Coordinates, Direction>? {
    return currentPositionDirection.second?.let { direction ->
        val nextPosition = direction.move(currentPositionDirection.first)
        val nextDirection = pipeMap[nextPosition]!!.nextDirection(direction)
        nextDirection?.let { Pair(nextPosition, it) }
    }
}

