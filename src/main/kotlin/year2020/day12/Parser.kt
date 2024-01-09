package year2020.day12

import cc.ekblad.konbini.*
import commons.*

val inputParser: Parser<List<Instructon>> = parser {
    chain1(instructionParser, whitespace).terms
}

typealias ShipPosition = Pair<Coordinates, CardinalDirection>

sealed class Instructon {
    data class North(val steps: Long) : Instructon()
    data class South(val steps: Long) : Instructon()
    data class East(val steps: Long) : Instructon()
    data class West(val steps: Long) : Instructon()
    data class Forward(val steps: Long) : Instructon()
    data class Right(val degrees: Long) : Instructon()
    data class Left(val degrees: Long) : Instructon()

    fun move(shipPosition: ShipPosition) : ShipPosition {
        val (coordinates, direction) = shipPosition
        return when(this) {
            is North    -> ShipPosition(coordinates.north(steps), direction)
            is South    -> ShipPosition(coordinates.south(steps), direction)
            is East     -> ShipPosition(coordinates.east(steps), direction)
            is West     -> ShipPosition(coordinates.west(steps), direction)
            is Forward  -> ShipPosition(direction.move(coordinates, steps), direction)
            is Left     -> ShipPosition(coordinates, direction.rotateCounterClockwise(degrees))
            is Right    -> ShipPosition(coordinates, direction.rotateClockwise(degrees))
        }
    }
}

val instructionParser = parser {
    val instruction = char()
    val stepsOrDegrees = integer()
    when(instruction) {
        'N' -> Instructon.North(stepsOrDegrees)
        'E' -> Instructon.East(stepsOrDegrees)
        'W' -> Instructon.West(stepsOrDegrees)
        'S' -> Instructon.South(stepsOrDegrees)
        'F' -> Instructon.Forward(stepsOrDegrees)
        'L' -> Instructon.Left(stepsOrDegrees)
        'R' -> Instructon.Right(stepsOrDegrees)
        else -> throw IllegalStateException()
    }
}