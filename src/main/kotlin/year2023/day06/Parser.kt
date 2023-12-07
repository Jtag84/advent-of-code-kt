package year2023.day06

import cc.ekblad.konbini.*
import newLine
import parseAll

typealias Time = Long
typealias Record = Long
typealias RacePairs = List<Pair<Time, Record>>

val raceParser: Parser<RacePairs> = parser {
    parseAll
    string("Time:")
    whitespace()
    val times = chain1(integer, whitespace)
    newLine()
    string("Distance:")
    whitespace()
    val distances = chain1(integer, whitespace)
    times.terms.zip(distances.terms)
}