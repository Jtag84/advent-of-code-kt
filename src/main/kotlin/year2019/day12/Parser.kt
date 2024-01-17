package year2019.day12

import cc.ekblad.konbini.*
import commons.Coordinates3d
import commons.Lines
import commons.parseLines

typealias Velocity = Coordinates3d
typealias MoonPosition = Pair<Coordinates3d, Velocity>

val inputParser: Parser<Set<MoonPosition>> = parser {
    chain1(moonPositionParser, whitespace).terms.toSet()
}

val moonPositionParser = parser {
    string("<x=")
    val x = integer()
    string(", y=")
    val y = integer()
    string(", z=")
    val z = integer()
    string(">")

    MoonPosition(Coordinates3d(x, y, z), Velocity(0, 0, 0))
}