package year2020.day11

import commons.*
import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 37) { seatLayout ->
    val emptySeats = seatLayout.entries.filter { it.value == LayoutItem.EMPTY_SEAT }.map { it.key }.toSet()
    val neighbors = emptySeats.map { it to it.allAround().filter { emptySeats.contains(it) }.toSet() }.toMap()
    generateSequence(Seats(emptySeats, emptySet())) { applyRound(it, neighbors, 4) }.zipWithNext().takeWhile { (previous, current) -> previous != current }.last().second.second.count()
}

typealias EmptySeats = Set<Coordinates2d>
typealias OccupiedSeats = Set<Coordinates2d>
typealias Seats = Pair<EmptySeats, OccupiedSeats>

fun applyRound(seats: Seats, neighbors: Map<Coordinates2d, Set<Coordinates2d>>, numberOfOccupiedSeatsToBecomeEmpty: Int) : Seats {
    val (emptySeats, occupiedSeats) = seats

    val newlyOccupiedSeats = emptySeats.mapNotNull { emptySeat ->
            if(neighbors[emptySeat]!!.any { occupiedSeats.contains(it) } ) {
                null
            }
            else {
                emptySeat
            }
        }.toSet()

    val newlyEmptySeats = occupiedSeats.mapNotNull { occupiedSeat ->
            if(neighbors[occupiedSeat]!!.count { occupiedSeats.contains(it) } >= numberOfOccupiedSeatsToBecomeEmpty) {
                occupiedSeat
            }
            else {
                null
            }
        }

    return Seats(emptySeats - newlyOccupiedSeats + newlyEmptySeats, occupiedSeats - newlyEmptySeats + newlyOccupiedSeats)
}