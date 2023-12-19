package year2023.day18

import cc.ekblad.konbini.*
import commons.Direction
import commons.Direction.*

val inputParser = parser {
    chain1(digDirectionParser, whitespace).terms
}

data class DigDirection(val direction: Direction, val length: Long, val color: String)

val digDirectionParser = parser {
    val directionChar = char()
    whitespace1()
    val length = integer()
    whitespace1()
    string("(#")
    val colorStringHexa = regex("[0-9a-f]+")
    string(")")
    when (directionChar) {
        'R' -> DigDirection(RIGHT, length, colorStringHexa)
        'D' -> DigDirection(DOWN, length, colorStringHexa)
        'L' -> DigDirection(LEFT, length, colorStringHexa)
        'U' -> DigDirection(UP, length, colorStringHexa)
        else -> throw IllegalStateException()
    }
}