package year2023.day10

import com.google.common.collect.Sets
import commons.Coordinates
import commons.Part.Companion.part2
import year2023.day10.Tile.*

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 4) { pipeMap ->
    val (startDirection, finalPipeMap) = getStartDirectionAndFinalPipeMap(pipeMap)
    val path = getPath(pipeMap, startDirection).toSet()

    val pathCoordinates = path.map { it.first }.toSet()
    val allNonLoopTiles = finalPipeMap.keys.minus(pathCoordinates)
    val pathWithoutHorizontals = pathCoordinates.filter { finalPipeMap[it] != HORIZONTAL }.toSet()
    allNonLoopTiles.count { isInLoop(finalPipeMap, pathWithoutHorizontals, it) }
}

fun isInLoop(pipeMap: Map<Coordinates, Tile>, loopPath: Set<Coordinates>, coordinates: Coordinates): Boolean {
    if (coordinates.x == 0L) {
        return false
    }
    val intersection = Sets.intersection(rayCastFrom(coordinates), loopPath)

    val coordinatesTile = intersection.map { Pair(it, pipeMap[it]!!) }.toSet()
    val withoutVerticalTiles = coordinatesTile.filter { it.second != VERTICAL }.sortedBy { it.first.x }

    val verticalTileCount = coordinatesTile.count { it.second == VERTICAL }
    val edgeCount = withoutVerticalTiles.chunked(2).sumOf {
        val firstCorner = it[0].second
        if (it.size == 1) {
            1L
        } else {
            val secondCorner = it[1].second
            if (firstCorner == BEND_N_E && secondCorner == BEND_N_W
                || firstCorner == BEND_S_E && secondCorner == BEND_S_W
                || firstCorner == START && secondCorner == BEND_S_W
                || firstCorner == BEND_S_E && secondCorner == START
                || firstCorner == START && secondCorner == BEND_N_W
                || firstCorner == BEND_N_E && secondCorner == START
            ) {
                2L
            } else {
                1L
            }
        }
    }

    return (edgeCount + verticalTileCount.toLong()) % 2 == 1L
}

fun rayCastFrom(coordinates: Coordinates): Set<Coordinates> {
    return (0..coordinates.x).map { Coordinates(it, coordinates.y) }.toSet()
}