package year2023.day23

import cc.ekblad.konbini.Parser
import cc.ekblad.konbini.parser
import commons.Coordinates
import commons.EnumParser
import commons.coordinatesParser
import commons.enumParser

typealias TrailMap = Map<Coordinates, TrailMapElement>

val inputParser: Parser<TrailMap> = parser {
    coordinatesParser(enumParser<TrailMapElement>())().toMap()
}

enum class TrailMapElement(override val parsingString: String) : EnumParser {
    FOREST("#"),
    PATH("."),
    SLOPE_NORTH("^"),
    SLOPE_SOUTH("v"),
    SLOPE_EAST(">"),
    SLOPE_WEST("<"),
}