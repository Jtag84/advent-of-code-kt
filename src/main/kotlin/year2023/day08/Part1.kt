package year2023.day08

import Part.Companion.part1
import foldUntil
import repeat

fun main() {
    part1.runTest(2)
    part1.run()
}

val part1 = part1(inputParser) { (instructions, nodeMap) ->
    countToNextNodeEndingInZ(instructions, nodeMap, "AAA")
}

typealias Count = Int

fun countToNextNodeEndingInZ(
    instructions: Instructions,
    nodeMap: NodeMap,
    node: NodeName,
    endingNameCondition: String = "ZZZ"
): Count {
    return instructions.asSequence().repeat().foldUntil(
        Pair(node, 0),
        { (currentNode, _) -> currentNode.endsWith(endingNameCondition) },
        { (currentNode, count), instruction -> Pair(instruction.apply(nodeMap[currentNode]!!), count + 1) })
        .second
}

