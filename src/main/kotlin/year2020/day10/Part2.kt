package year2020.day10

import commons.Part.Companion.part2
import commons.groupNextToSame

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 19208L) { adapters ->
    val groupedOnes = (adapters + 0).sorted().zipWithNext { a, b -> b - a }.groupNextToSame().filter { it[0] != 3L }.groupBy { it.size }.mapValues { it.value.size }
    (Math.pow(2.0, groupedOnes[2]!!.toDouble()) * Math.pow(7.0, groupedOnes[4]!!.toDouble()) * Math.pow(4.0, groupedOnes[3]!!.toDouble())).toLong()
}