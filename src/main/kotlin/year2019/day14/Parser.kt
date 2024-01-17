package year2019.day14

import cc.ekblad.konbini.*
import commons.Lines
import commons.parseLines

val inputParser: Parser<Map<Chemical, Pair<Long, Set<Pair<Long, Chemical>>>>> = parser {
    chain1(reactionParser, whitespace).terms.toMap()
}

typealias Chemical = String

val chemicalParser = parser {
    val quantity = integer()
    whitespace()
    val chemical = regex("[A-Z]+")
    quantity to chemical
}

val reactionParser = parser {
    val chemicals = chain1(chemicalParser, parser { string(", ") }).terms.toSet()
    string(" => ")
    val (quantity, toChemical) = chemicalParser()

    toChemical to (quantity to chemicals)
}