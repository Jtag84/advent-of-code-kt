package year2020.day24

import cc.ekblad.konbini.*
import commons.*

val inputParser: Parser<List<List<TileDirection>>> = parser {
    chain1(parser{ many1(enumParser<TileDirection>()) }, whitespace).terms
}

enum class TileDirection(override val parsingString: String) : EnumParser {
    NORTH_EAST("ne"),
    EAST("e"),
    SOUTH_EAST("se"),
    SOUTH_WEST("sw"),
    WEST("w"),
    NORTH_WEST("nw");

    fun move(coordinates: HexagonGridCoordinates, n: Long = 1): HexagonGridCoordinates {
        return when (this) {
            NORTH_EAST -> coordinates.northEast()
            EAST -> coordinates.east(n)
            SOUTH_EAST -> coordinates.southEast()
            SOUTH_WEST -> coordinates.southWest()
            WEST -> coordinates.west(n)
            NORTH_WEST -> coordinates.northWest()
        }
    }
}