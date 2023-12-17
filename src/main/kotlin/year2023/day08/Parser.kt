package year2023.day08

import cc.ekblad.konbini.*
import commons.EnumParser
import commons.enumParser

enum class Instruction(override val parsingString: String) : EnumParser {
    LEFT("L"),
    RIGHT("R");

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

val instructionsParser: Parser<Instructions> = parser {
    many1(enumParser<Instruction>())
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