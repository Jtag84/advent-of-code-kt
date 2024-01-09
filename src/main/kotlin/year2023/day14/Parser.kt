package year2023.day14

import cc.ekblad.konbini.Parser
import cc.ekblad.konbini.parser
import commons.Coordinates2d
import commons.EnumParser
import commons.coordinatesParser
import commons.enumParser

val inputParser: Parser<Map<Coordinates2d, Rock>> = parser {
    coordinatesParser(enumParser<Rock>())().toMap()
}

enum class Rock(override val parsingString: String) : EnumParser {
    ROUND_ROCK("O"),
    CUBE_ROCK("#"),
    EMPTY(".");
}