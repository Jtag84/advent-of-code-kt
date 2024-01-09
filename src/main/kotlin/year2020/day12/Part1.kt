package year2020.day12

import commons.CardinalDirection
import commons.CardinalDirection.EAST
import commons.Coordinates
import commons.Part.Companion.part1
import commons.manhattanDistance

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 25L) { instructions ->
    val startShipPosition = ShipPosition(Coordinates(0, 0), EAST)
    instructions.fold(startShipPosition) { shipPosition, instructon ->  instructon.move(shipPosition)}
        .first.manhattanDistance(startShipPosition.first)
}