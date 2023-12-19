package year2023.day17

import cc.ekblad.konbini.integer
import cc.ekblad.konbini.parser
import commons.Coordinates
import commons.coordinatesParser

typealias HeatLossMap = Map<Coordinates, Long>

val inputParser = parser {
    coordinatesParser(integer)().toMap()
}