package year2020.day23

import commons.Part.Companion.part1
import commons.*

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, "67384529") { cups ->
    val (head, tail, nodeMap) = cups.toHeadTailAndNodeMap()
    head.previous = tail
    tail.next = head

    executeMoves(head, nodeMap, 100)

    val node1 = nodeMap[1]!!

    node1.next!!.take(8).map { it.value }.joinToString("")
}

fun executeMoves(head: Head<Int>, nodeMap: NodeMap<Int>, numberOfMoves : Int, max:Int = 9): Head<Int> {
    return generateSequence(head) { currentHead ->
        val currentCup = currentHead.value
        val (pickupCupsHead, pickUpCupTail) = currentHead.removeSectionAfter(3)
        val pickUpCupsValues = pickupCupsHead.map { it.value }.toSet()

        var destinationCup = currentCup - 1
        while (destinationCup in pickUpCupsValues || destinationCup < 1) {
            if (destinationCup < 1) {
                destinationCup = max
            } else {
                destinationCup--
            }
        }

        val destinationCupNode = nodeMap[destinationCup]!!
        destinationCupNode.insertSectionAfter(pickupCupsHead, pickUpCupTail)

        currentHead.next!!
    }.drop(numberOfMoves).first()
}
