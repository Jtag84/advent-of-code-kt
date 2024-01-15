package year2020.day23

import cc.ekblad.konbini.*

val inputParser = parser {
    many(parser{ regex("[0-9]") }).map { it.toInt() }
}