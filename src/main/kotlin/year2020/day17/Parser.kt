package year2020.day17

import cc.ekblad.konbini.*
import commons.*

val inputParser: Parser<Map<Coordinates, CubeState>> = parser {
   coordinatesParser(enumParser<CubeState>())().toMap()
}

enum class CubeState(override val parsingString: String) : EnumParser{
    ACTIVE("#"),
    INACTIVE("."),
}