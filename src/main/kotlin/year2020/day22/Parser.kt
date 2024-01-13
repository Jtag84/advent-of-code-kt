package year2020.day22

import cc.ekblad.konbini.*
import commons.Lines
import commons.parseLines

val inputParser: Parser<Pair<List<Long>, List<Long>>> = parser {
    string("Player 1:")
    whitespace()
    val player1Hand = chain1(integer, whitespace).terms
    whitespace()
    string("Player 2:")
    whitespace()
    val player2Hand = chain1(integer, whitespace).terms

    player1Hand to player2Hand
}