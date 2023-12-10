package year2023.day09

import commons.Part.Companion.part1

fun main() {
    part1.runTest()
    part1.run()
}

val part1 = part1(inputParser, 114L) { report ->
    report.map(::downToZerosList)
        .sumOf { it.sumOf { l -> l.last() } }
}

fun downToZerosList(list: List<Long>): List<List<Long>> {
    if (list.all { it == 0L }) {
        return listOf(list)
    } else {
        val nextList = list.zip(list.drop(1)) { a, b -> b - a }
        return listOf(list) + downToZerosList(nextList)
    }
}


