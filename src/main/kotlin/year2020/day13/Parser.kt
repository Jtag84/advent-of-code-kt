package year2020.day13

import cc.ekblad.konbini.*
import commons.Lines
import commons.parseLines

typealias DepartureTime = Long
typealias BusId = Long
typealias DepartureTimeSchedule = Pair<DepartureTime, List<BusId?>>

val inputParser: Parser<DepartureTimeSchedule> = parser {
    val lines = parseLines()
    val departureTime: DepartureTime = lines[0].toLong()
    val busIds = lines[1].split(",").map { if(it == "x") {null} else {it.toLong()} }

    departureTime to busIds
}