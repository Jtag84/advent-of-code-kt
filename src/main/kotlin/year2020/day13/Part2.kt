package year2020.day13

import commons.Part.Companion.part2
import commons.chineseRemainder

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 1068781L) { (_, busIds) ->
    busIds
        .mapIndexedNotNull { index, busId -> busId?.let { (busId - index) to busId }}
        .let { chineseRemainder(it.toSet()) }
}