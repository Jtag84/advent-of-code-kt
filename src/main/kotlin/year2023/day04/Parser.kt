package year2023.day04

import cc.ekblad.konbini.*
import com.google.common.collect.Sets

typealias MyWinningNumbers = Set<Long>

val scratchcardsParser: Parser<List<MyWinningNumbers>> = parser {
    chain1(scratchcardParser, whitespace).terms
}

val scratchcardParser: Parser<MyWinningNumbers> = parser {
    string("Card")
    whitespace()
    integer()
    char(':')
    whitespace()
    val leftNumbers = chain(integer, whitespace)
    val winningNumbers = leftNumbers.terms.toSet()
    whitespace()
    char('|')
    whitespace()
    val rightNumbers = chain(integer, whitespace)
    val myNumbers = rightNumbers.terms.toSet()
    whitespace()
    Sets.intersection(winningNumbers, myNumbers)
}