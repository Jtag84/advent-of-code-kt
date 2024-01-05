package year2021.day19

import cc.ekblad.konbini.*
import commons.Coordinates3d
import commons.Lines
import commons.parseLines

val inputParser = parser {
    chain1(scannerParser, whitespace).terms
}

val coordinates3dParser = parser {
    val x = integer()
    char(',')
    val y = integer()
    char(',')
    val z = integer()
    Coordinates3d(x, y, z)
}

val scannerParser = parser {
    string("--- scanner ")
    integer()
    string(" ---")
    whitespace()
    chain1(coordinates3dParser, whitespace).terms.toSet()
}