package year2020.day15

import cc.ekblad.konbini.*
import commons.Lines
import commons.parseLines

val inputParser: Parser<List<Long>> = parser {
    chain1(integer, parser { char(',') }).terms
}