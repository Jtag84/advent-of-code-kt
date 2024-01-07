package year2015.day21

import cc.ekblad.konbini.*
import commons.Lines
import commons.parseLines

typealias HitPoint = Long
typealias Damage = Long
typealias Armor = Long

typealias Stats = Triple<HitPoint,Damage,Armor>
val inputParser: Parser<Stats> = parser {
    string("Hit Points: ")
    val hitPoints = integer()
    whitespace()
    string("Damage: ")
    val damage = integer()
    whitespace()
    string("Armor: ")
    val armor = integer()
    Stats(hitPoints, damage, armor)
}