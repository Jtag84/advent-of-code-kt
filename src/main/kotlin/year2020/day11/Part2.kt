package year2020.day11

import commons.*
import commons.Part.Companion.part2

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 26) { seatLayout ->
    val maxX = seatLayout.keys.maxOf { it.x }
    val maxY = seatLayout.keys.maxOf { it.y }
    val emptySeats = seatLayout.entries.filter { it.value == LayoutItem.EMPTY_SEAT }.map { it.key }.toSet()
    val neighbors = emptySeats.map {seat ->
            seat to CardinalDirection.entries.mapNotNull { seat.findFirstInDirection(emptySeats, it, maxX, maxY) }.toSet()
        }
        .toMap()

    generateSequence(Seats(emptySeats, emptySet())) { applyRound(it, neighbors, 5) }
        .zipWithNext()
        .takeWhile { (previous, current) -> previous != current }
        .last().second.second.count()
}