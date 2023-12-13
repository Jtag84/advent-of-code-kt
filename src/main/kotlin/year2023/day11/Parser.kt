package year2023.day11

import cc.ekblad.konbini.char
import cc.ekblad.konbini.map
import cc.ekblad.konbini.oneOf
import cc.ekblad.konbini.parser
import commons.coordinatesParser

val inputParser = parser {
    coordinatesParser(spaceDataParser)().toMap()
}

val spaceDataParser = parser { oneOf(*SpaceData.entries.map { it.getParser() }.toTypedArray()) }

enum class SpaceData(private val charData: Char) {
    GALAXY('#'),
    EMPTY_SPACE('.');

    fun getParser() = parser { char(charData) }.map { this }
}

