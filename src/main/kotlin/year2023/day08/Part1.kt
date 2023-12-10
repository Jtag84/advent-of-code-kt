package year2023.day08

import commons.Part.Companion.part1
import commons.foldUntil
import commons.repeat

fun main() {
    part1.runTest()
    part1.run()
}

val part1 = part1(inputParser, 2) { (instructions, nodeMap) ->
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

