package year2020.day11

import cc.ekblad.konbini.*
import commons.*

val inputParser: Parser<Map<Coordinates2d, LayoutItem>> = parser {
    coordinatesParser(enumParser<LayoutItem>())().toMap()
}

enum class LayoutItem(override val parsingString: String) : EnumParser {
    OCCUPIED_SEAT("#"),
    EMPTY_SEAT("L"),
    FLOOR("."),
}