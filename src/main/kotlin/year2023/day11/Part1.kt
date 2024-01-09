package year2023.day11

import com.google.common.collect.Sets
import commons.Coordinates2d
import commons.Part.Companion.part1
import commons.manhattanDistance
import year2023.day11.SpaceData.EMPTY_SPACE
import year2023.day11.SpaceData.GALAXY

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 374L) { galaxyMap ->
    calculateDistanceSumBetweenGalaxies(galaxyMap)
}

fun calculateDistanceSumBetweenGalaxies(galaxyMap: Map<Coordinates2d, SpaceData>, expansionFactor: Long = 1L): Long {
    val galaxyMapByColumn = galaxyMap.entries.groupBy({ it.key.x }, { it.value })
    val galaxyMapByRow = galaxyMap.entries.groupBy({ it.key.y }, { it.value })
    val rowsToExpand = galaxyMapByRow.entries.filter { row -> row.value.all { it == EMPTY_SPACE } }.map { it.key }
    val columnsToExpand =
        galaxyMapByColumn.entries.filter { column -> column.value.all { it == EMPTY_SPACE } }.map { it.key }

    val galaxiesCoordinates = galaxyMap.entries.filter { it.value == GALAXY }.map { it.key }
        .map { coordinates ->
            val x = coordinates.x
            val y = coordinates.y
            val xExpansion = columnsToExpand.count { it < x } * expansionFactor
            val yExpansion = rowsToExpand.count { it < y } * expansionFactor
            Coordinates2d(x + xExpansion, y + yExpansion)
        }.toSet()

    return Sets.combinations(galaxiesCoordinates, 2).sumOf {
        val coordinatesList = it.toList()
        coordinatesList[0].manhattanDistance(coordinatesList[1])
    }
}

