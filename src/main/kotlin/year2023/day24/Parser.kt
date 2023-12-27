package year2023.day24

import arrow.core.Tuple6
import cc.ekblad.konbini.*
import commons.Lines
import commons.parseLines

val inputParser: Parser<List<PositionVector>> = parser {
    chain1(positionVectorParser, whitespace).terms
}

typealias X = Long
typealias Y = Long
typealias Z = Long
typealias Vx = Long
typealias Vy = Long
typealias Vz = Long
typealias PositionVector = Tuple6<X, Y, Z, Vx, Vy, Vz>

val positionVectorParser : Parser<PositionVector> = parser {
    val x = integer()
    string(",")
    whitespace1()
    val y = integer()
        string(",")
    whitespace1()
    val z = integer()
    whitespace1()
    string("@")
    whitespace1()
    val vx = integer()
        string(",")
    whitespace1()
    val vy = integer()
        string(",")
    whitespace1()
    val vz = integer()
    PositionVector(x, y, z, vx, vy, vz)
}