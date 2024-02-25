package year2018.day15

import cc.ekblad.konbini.*
import commons.*

val inputParser = parser {
    coordinatesParser(enumParser<CavernMap>())().toMap()
}

enum class CavernMap(override val parsingString: String) : EnumParser {
    PRE_GOBLIN("g"), // for debug only
    PRE_ELF("e"),// for debug only
    GOBLIN("G"),
    ELF("E"),
    WALL("#"),
    EMPTY(".")
}