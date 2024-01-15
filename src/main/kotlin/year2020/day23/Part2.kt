package year2020.day23

import commons.Part.Companion.part2
import commons.*

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 149245887792L) { cups ->
    val (head, tail, nodeMap) = (cups + (10..1_000_000)).toHeadTailAndNodeMap()
    head.previous = tail
    tail.next = head

    executeMoves(head, nodeMap, 10_000_000, 1_000_000)

    val node1 = nodeMap[1]!!

    node1.next!!.value.toLong() * node1.next!!.next!!.value.toLong()
}