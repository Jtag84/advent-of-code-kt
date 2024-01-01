package year2015.day17

import cc.ekblad.konbini.*
import commons.Lines
import commons.parseLines

val inputParser: Parser<List<Int>> = parser {
    parseLines().map { it.toInt() }
}