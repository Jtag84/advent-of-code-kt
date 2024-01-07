package year2015.day22

import cc.ekblad.konbini.*
import commons.Lines
import commons.parseLines
import year2015.day21.Stats

val inputParser: Parser<Stats> = parser {
    string("Hit Points: ")
    val hitPoints = integer()
    whitespace()
    string("Damage: ")
    val damage = integer()
    Stats(hitPoints, damage, 0)
}