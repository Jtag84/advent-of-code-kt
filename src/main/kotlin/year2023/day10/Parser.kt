package year2023.day10

import cc.ekblad.konbini.Parser
import cc.ekblad.konbini.parser
import commons.*
import year2023.day10.Direction.*

val inputParser: Parser<Map<Coordinates, Tile>> = parser {
    coordinatesParser(enumParser<Tile>())().toMap()
}

enum class Tile(private val directionsMap: Map<Direction, Direction>, override val parsingString: String) : EnumParser {
    VERTICAL(mapOf(Pair(SOUTH, SOUTH), Pair(NORTH, NORTH)), "|"),
    HORIZONTAL(mapOf(Pair(EAST, EAST), Pair(WEST, WEST)), "-"),
    BEND_N_E(mapOf(Pair(SOUTH, EAST), Pair(WEST, NORTH)), "L"),
    BEND_N_W(mapOf(Pair(SOUTH, WEST), Pair(EAST, NORTH)), "J"),
    BEND_S_W(mapOf(Pair(EAST, SOUTH), Pair(NORTH, WEST)), "7"),
    BEND_S_E(mapOf(Pair(NORTH, EAST), Pair(WEST, SOUTH)), "F"),
    GROUND(emptyMap(), "."),
    START(emptyMap(), "S");

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