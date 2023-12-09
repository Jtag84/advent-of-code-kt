package year2023.day08

import cc.ekblad.konbini.*

enum class Instruction {
    LEFT,
    RIGHT;

    fun apply(pair: Pair<NodeName, NodeName>): NodeName {
        return when (this) {
            LEFT -> pair.first
            RIGHT -> pair.second
        }
    }
}

typealias Instructions = List<Instruction>
typealias Node = Pair<NodeName, NodeName>
typealias NodeMap = Map<NodeName, Node>
typealias NodeName = String

val inputParser: Parser<Pair<Instructions, NodeMap>> = parser {
    val instructions = instructionsParser()
    whitespace()
    val nodeMap = nodesParser()
    Pair(instructions, nodeMap)
}

val instructionParser = parser {
    val instruction = regex("[LR]")
    when (instruction) {
        "L" -> Instruction.LEFT
        "R" -> Instruction.RIGHT
        else -> throw IllegalStateException("Don't know $instruction")
    }
}

val instructionsParser: Parser<Instructions> = parser {
    many1(instructionParser)
}

val nodeNameParser = parser { regex("[A-Z1-9]{3}") }

val nodeParser: Parser<Pair<NodeName, Node>> = parser {
    val nodeName = nodeNameParser()
    string(" = (")
    val leftNode = nodeNameParser()
    string(", ")
    val rightNode = nodeNameParser()
    string(")")
    Pair(nodeName, Pair(leftNode, rightNode))
}

val nodesParser: Parser<NodeMap> = parser {
    chain1(nodeParser, whitespace).terms.toMap()
}