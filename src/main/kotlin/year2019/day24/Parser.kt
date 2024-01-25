package year2019.day24

import cc.ekblad.konbini.*
import commons.*

val inputParser = parser {
    coordinatesParser(enumParser<ErisMapElement>())().toMap()
}

enum class ErisMapElement(override val parsingString: String) : EnumParser {
    BUG("#"),
    EMPTY(".")
}