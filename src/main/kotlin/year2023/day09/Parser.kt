package year2023.day09

import cc.ekblad.konbini.*

typealias Report = List<List<Long>>

val inputParser: Parser<Report> = parser {
    chain1(
        parser {
            chain1(integer, parser {
                char(' ')
            }).terms
        },
        whitespace
    ).terms
}