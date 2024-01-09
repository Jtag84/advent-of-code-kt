package year2023.day11

import cc.ekblad.konbini.Parser
import cc.ekblad.konbini.parser
import commons.Coordinates2d
import commons.EnumParser
import commons.coordinatesParser
import commons.enumParser

val inputParser: Parser<Map<Coordinates2d, SpaceData>> = parser {
    coordinatesParser(enumParser<SpaceData>())().toMap()
}

enum class SpaceData(override val parsingString: String) : EnumParser {
    GALAXY("#"),
    EMPTY_SPACE(".");
}

