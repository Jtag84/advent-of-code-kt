package year2021.day25

import cc.ekblad.konbini.*
import commons.*

val inputParser: Parser<Map<Coordinates2d, SeaCucumber>> = parser {
    coordinatesParser(enumParser<SeaCucumber>())().toMap()
}

enum class SeaCucumber(override val parsingString: String) : EnumParser {
    SOUTH("v"),
    EAST(">"),
    EMPTY(".")
}