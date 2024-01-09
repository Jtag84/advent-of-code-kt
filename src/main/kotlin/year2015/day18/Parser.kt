package year2015.day18

import cc.ekblad.konbini.*
import commons.*

typealias LightMap = Map<Coordinates2d, Light>

val inputParser: Parser<LightMap> = parser {
    coordinatesParser(enumParser<Light>())().toMap()
}

enum class Light(override val parsingString: String) : EnumParser {
    ON("#"),
    OFF(".")
}