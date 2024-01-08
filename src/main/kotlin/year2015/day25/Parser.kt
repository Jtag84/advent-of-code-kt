package year2015.day25

import cc.ekblad.konbini.*
import commons.Lines
import commons.parseLines

val inputParser: Parser<Pair<Long, Long>> = parser {
    string("To continue, please consult the code grid in the manual.  Enter the code at row ")
    val row = integer()
    string(", column ")
    val column = integer()
    Pair(row, column)
}