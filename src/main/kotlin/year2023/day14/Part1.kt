package year2023.day14

import arrow.core.fold
import arrow.core.partially1
import commons.Coordinates
import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 136L) { platformMap ->
    val rocksByRow = platformMap.entries.groupBy({ it.key.y }, { it.key to it.value })
    tiltNorth(rocksByRow)
        .sumOf(calculateRockLoad.partially1(rocksByRow.size.toLong()))
}

fun tiltNorth(rocksByRow: Map<Long, List<Pair<Coordinates, Rock>>>) =
    rocksByRow.toSortedMap()
        .fold(mutableMapOf<Long, Long>() to emptySet<Coordinates>()) { (offsetCount, roundRockPositions), (_, row) ->
            val roundRockNewPositions = mutableSetOf<Coordinates>()
            for ((coordinates, rock) in row) {
                when (rock) {
                    Rock.EMPTY -> offsetCount.compute(coordinates.x) { _, currentCount -> (currentCount ?: 0) + 1 }
                    Rock.CUBE_ROCK -> offsetCount[coordinates.x] = 0
                    Rock.ROUND_ROCK -> roundRockNewPositions.add(
                        (Coordinates(
                            coordinates.x,
                            coordinates.y - (offsetCount[coordinates.x] ?: 0)
                        ))
                    )
                }
            }
            offsetCount to roundRockPositions + roundRockNewPositions
        }
        .second

val calculateRockLoad = { platformSize: Long, (_, y): Coordinates ->
    platformSize - y
}



