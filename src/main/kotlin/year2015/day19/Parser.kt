package year2015.day19

import cc.ekblad.konbini.*
import commons.Lines
import commons.parseLines

val inputParser = parser {
    val replacements = chain1(replacementParser, whitespace).terms.groupBy ({ it.first }, {it.second})
    whitespace()
    val moleculeChain = many1(moleculeParser)
    replacements to moleculeChain
}

val moleculeParser = parser { regex("[A-Ze][a-z]{0,1}") }

val replacementParser = parser {
    val from = moleculeParser()
    string(" => ")
    val replacementList = many1(moleculeParser)

    from to replacementList
}