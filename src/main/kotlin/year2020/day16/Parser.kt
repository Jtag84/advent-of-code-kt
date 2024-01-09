package year2020.day16

import cc.ekblad.konbini.*
import commons.Lines
import commons.newLine
import commons.parseLines

typealias FieldName = String
typealias FieldRanges = Set<LongRange>
typealias Ticket = List<Long>
typealias MyTicket = Ticket
typealias NearbyTickets = List<Ticket>
typealias Fields = Map<FieldName, FieldRanges>

val inputParser: Parser<Triple<Fields, MyTicket, NearbyTickets>> = parser {
    val fields = chain1(fieldParser, newLine).terms.toMap()
    whitespace()
    string("your ticket:")
    whitespace()
    val myTicket = ticketParser()
    whitespace()
    string("nearby tickets:")
    whitespace()
    val nearbyTickets = chain1(ticketParser, whitespace).terms

    Triple(fields, myTicket, nearbyTickets)
}

val fieldParser = parser {
    val fieldName = regex("[a-z ]+")
    string(": ")
    val firstRangeLowerBound = integer()
    char('-')
    val firstRangeUpperBound = integer()
    string(" or ")
    val secondRangeLowerBound = integer()
    char('-')
    val secondRangeUpperBound = integer()

    Pair(fieldName, setOf(firstRangeLowerBound..firstRangeUpperBound, secondRangeLowerBound..secondRangeUpperBound))
}

val ticketParser:Parser<Ticket> = parser {
    chain1(integer, parser { char(',') }).terms
}