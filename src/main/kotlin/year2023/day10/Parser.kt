package year2023.day10

import cc.ekblad.konbini.Parser
import cc.ekblad.konbini.parser
import commons.*
import year2023.day10.Direction.*
import year2023.day10.Tile.*

val inputParser: Parser<Map<Coordinates, Tile>> = parser {
    coordinatesParser(tileParser)().toMap()
}

enum class Tile(private val directionsMap: Map<Direction, Direction>) {
    VERTICAL(mapOf(Pair(SOUTH, SOUTH), Pair(NORTH, NORTH))),
    HORIZONTAL(mapOf(Pair(EAST, EAST), Pair(WEST, WEST))),
    BEND_N_E(mapOf(Pair(SOUTH, EAST), Pair(WEST, NORTH))),
    BEND_N_W(mapOf(Pair(SOUTH, WEST), Pair(EAST, NORTH))),
    BEND_S_W(mapOf(Pair(EAST, SOUTH), Pair(NORTH, WEST))),
    BEND_S_E(mapOf(Pair(NORTH, EAST), Pair(WEST, SOUTH))),
    GROUND(emptyMap()),
    START(emptyMap());

    fun nextDirection(currentDirection: Direction): Direction? {
        return directionsMap[currentDirection]
    }

    companion object {
        fun toTile(directions: Set<Direction>): Tile {
            return entries.first { it.directionsMap.keys == directions }
        }
    }
}

enum class Direction(val directionFunction: (Coordinates) -> Coordinates) {
    NORTH(Coordinates::north),
    SOUTH(Coordinates::south),
    EAST(Coordinates::east),
    WEST(Coordinates::west);

    fun opposite(): Direction {
        return when (this) {
            NORTH -> SOUTH
            SOUTH -> NORTH
            EAST -> WEST
            WEST -> EAST
        }
    }

    fun move(coordinates: Coordinates): Coordinates {
        return directionFunction(coordinates)
    }
}

val tileParser = parser {
    when (val tileChar = char()) {
        '.' -> GROUND
        '|' -> VERTICAL
        '-' -> HORIZONTAL
        'L' -> BEND_N_E
        'J' -> BEND_N_W
        '7' -> BEND_S_W
        'F' -> BEND_S_E
        'S' -> START
        else -> throw IllegalStateException("Don't know $tileChar")
    }
}
