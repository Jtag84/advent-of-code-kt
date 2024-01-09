package year2023.day16

import cc.ekblad.konbini.parser
import commons.*

typealias Grid = Map<Coordinates2d, GridElement>

val inputParser = parser {
    coordinatesParser(enumParser<GridElement>())().toMap()
}

typealias Position = Pair<Coordinates2d, Direction>

enum class GridElement(override val parsingString: String) : EnumParser {
    EMPTY("."),
    LEFT_MIRROR("\\"),
    RIGHT_MIRROR("/"),
    VERTICAL_SPLITTER("|"),
    HORIZONTAL_SPLITTER("-");
}