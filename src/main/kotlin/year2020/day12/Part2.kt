package year2020.day12

import commons.*
import commons.Part.Companion.part2

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

typealias ShipCoordinates = Coordinates2d
typealias WaypointCoordinates = Coordinates2d
typealias ShipWaypoint = Pair<ShipCoordinates, WaypointCoordinates>

val part2 = part2(inputParser, 286L) { instructions ->
    val waypointStart = WaypointCoordinates(10, -1)
    val shipCoordinatesStart = ShipCoordinates(0,0)
    val shipWaypointStart = ShipWaypoint(shipCoordinatesStart, waypointStart)

    instructions.fold(shipWaypointStart) { shipWaypoint, instruction -> movePart2(shipWaypoint, instruction) }.first.manhattanDistance(shipCoordinatesStart)
}

fun movePart2(shipWaypoint: ShipWaypoint, instructon: Instructon) : ShipWaypoint {
    val (shipCoordinates, waypointCoordinates) = shipWaypoint
    return when(instructon) {
        is Instructon.North     -> ShipWaypoint(shipCoordinates, waypointCoordinates.north(instructon.steps))
        is Instructon.East      -> ShipWaypoint(shipCoordinates, waypointCoordinates.east(instructon.steps))
        is Instructon.West      -> ShipWaypoint(shipCoordinates, waypointCoordinates.west(instructon.steps))
        is Instructon.South     -> ShipWaypoint(shipCoordinates, waypointCoordinates.south(instructon.steps))
        is Instructon.Forward   -> ShipWaypoint(ShipCoordinates(shipCoordinates.x + waypointCoordinates.x * instructon.steps, shipCoordinates.y + waypointCoordinates.y * instructon.steps), waypointCoordinates)
        is Instructon.Left      -> ShipWaypoint(shipCoordinates, generateSequence(waypointCoordinates) {it.rotateCounterClockwise()}.drop(1).take(instructon.degrees.toInt() / 90).last() )
        is Instructon.Right     -> ShipWaypoint(shipCoordinates, generateSequence(waypointCoordinates) {it.rotateClockwise()}.drop(1).take(instructon.degrees.toInt() / 90).last() )
    }
}