package year2023.day21

import cc.ekblad.konbini.Parser
import cc.ekblad.konbini.parser
import commons.Coordinates
import commons.EnumParser
import commons.coordinatesParser
import commons.enumParser

typealias MaxX = Long
typealias MaxY = Long
typealias Garden = Pair<Map<Coordinates, GardenElement>, Pair<MaxX, MaxY>>

val inputParser: Parser<Garden> = parser {
    val map = coordinatesParser(enumParser<GardenElement>())().toMap()
    val maxX = map.keys.maxOf { it.x }
    val maxY = map.keys.maxOf { it.y }

    map to (maxX to maxY)
}

enum class GardenElement(override val parsingString: String) : EnumParser {
    PLOT("."),
    ROCK("#"),
    START("S")
}