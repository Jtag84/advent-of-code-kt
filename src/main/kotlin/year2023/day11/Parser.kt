package year2023.day11

import cc.ekblad.konbini.parser
import commons.EnumParser
import commons.coordinatesParser
import commons.enumParser

val inputParser = parser {
    coordinatesParser(enumParser<SpaceData>())().toMap()
}

enum class SpaceData(override val parsingString: String) : EnumParser {
    GALAXY("#"),
    EMPTY_SPACE(".");
}

