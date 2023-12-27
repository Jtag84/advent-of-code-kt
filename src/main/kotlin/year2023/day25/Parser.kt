package year2023.day25

import cc.ekblad.konbini.*
import commons.graph.*
import commons.parseLines

val inputParser = parser {
    chain1(edgesRowParser, whitespace).terms.flatten().groupBy ({ it.first }, {it.second}).mapValues { it.value.toSet() }
}

val vertexNameParser = parser { regex("[a-z]+") }
val edgesRowParser = parser {
    val leftVertexName = vertexNameParser()
    string(": ")
    val linkedToVertices = chain1(vertexNameParser, parser { char(' ') }).terms
    linkedToVertices.flatMap { listOf( it to leftVertexName, leftVertexName to it) }
}