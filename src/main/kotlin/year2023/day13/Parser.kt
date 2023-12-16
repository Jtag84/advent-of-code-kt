package year2023.day13

import cc.ekblad.konbini.chain1
import cc.ekblad.konbini.parser
import cc.ekblad.konbini.regex
import cc.ekblad.konbini.whitespace
import commons.newLine

val inputParser = parser {
    chain1(mapParser, whitespace).terms
}

val mapParser = parser {
    chain1(parser { regex("[.#]+") }, newLine).terms
}