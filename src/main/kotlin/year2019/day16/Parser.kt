package year2019.day16

import cc.ekblad.konbini.many1
import cc.ekblad.konbini.parser
import cc.ekblad.konbini.regex

val inputParser = parser {
    many1(parser { regex("[0-9]") }).map { it.toInt() }
}