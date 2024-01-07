package year2015.day24

import cc.ekblad.konbini.*
import commons.Lines
import commons.parseLines

typealias Weight = Long
val inputParser: Parser<List<Weight>> = parser {
    chain1(integer, whitespace).terms
}