package year2018.day15

import cc.ekblad.konbini.*
import commons.*

val inputParser = parser {
    coordinatesParser(enumParser<CavernMap>())().toMap()
}

enum class CavernMap(override val parsingString: String) : EnumParser {
    GOBLIN("G"),
    ELF("E"),
    WALL("#"),
    EMPTY(".")
}