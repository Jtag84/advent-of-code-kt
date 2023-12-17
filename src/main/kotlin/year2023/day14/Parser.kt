package year2023.day14

import cc.ekblad.konbini.char
import cc.ekblad.konbini.map
import cc.ekblad.konbini.oneOf
import cc.ekblad.konbini.parser
import commons.coordinatesParser

val inputParser = parser {
    coordinatesParser(rockParser)().toMap()
}

val rockParser = parser { oneOf(*Rock.entries.map { it.getParser() }.toTypedArray()) }

enum class Rock(private val parsingChar: Char) {
    ROUND_ROCK('O'),
    CUBE_ROCK('#'),
    EMPTY('.');

    fun getParser() = parser { char(parsingChar) }.map { this }
}