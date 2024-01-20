package year2019.day18

import com.google.common.collect.Sets
import commons.*
import commons.Part.Companion.part2
import commons.search.AStar
import commons.search.Node
import org.jgrapht.Graph
import org.jgrapht.graph.DefaultUndirectedWeightedGraph
import org.jgrapht.graph.DefaultWeightedEdge

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 72) { tunnelMap ->
    val entrance = tunnelMap.entries.first { it.value == TunnelElement.Entrance }.key
    val allKeys = tunnelMap.values.filterIsInstance<TunnelElement.Key>().map { it.keyName }.toSet()
    val modifiedTunnelMap = tunnelMap.toMutableMap()
    modifiedTunnelMap[entrance] = TunnelElement.Wall
    modifiedTunnelMap[entrance.east()] = TunnelElement.Wall
    modifiedTunnelMap[entrance.west()] = TunnelElement.Wall
    modifiedTunnelMap[entrance.north()] = TunnelElement.Wall
    modifiedTunnelMap[entrance.south()] = TunnelElement.Wall

    val newEntrances = listOf(
            entrance.northWest(),
            entrance.northEast(),
            entrance.southEast(),
            entrance.southWest())
    newEntrances.forEach { modifiedTunnelMap[it] = TunnelElement.Entrance }

    val contractedGraph: Graph<TunnelVertex, DefaultWeightedEdge> = DefaultUndirectedWeightedGraph(DefaultWeightedEdge::class.java)

    val entranceVertices = newEntrances.map { TunnelVertex(TunnelElement.Entrance, it) }
    entranceVertices.forEach {
        contractedGraph.addVertex(it)
        buildContractedGraph(
            it,
            0,
            contractedGraph,
            it.second,
            modifiedTunnelMap,
            mutableSetOf(),
        )
    }

    // uncomment to create a png of the graph
//    if(isTest) {
//        contractedGraph.printGraph("graphPrintTestPart2.png")
//    }else {
//        contractedGraph.printGraph("graphPrintPart2.png")
//    }

    // takes 21s to complete
    AStar.search(
        TunnelNodePart2(contractedGraph, entranceVertices, emptySet()),
        {(allKeys.size - it.keys.size)},
        {node -> node.keys.size == allKeys.size})
        .first
}

data class TunnelNodePart2(
    val tunnelGraph: Graph<TunnelVertex, DefaultWeightedEdge>,
    val tunnelVertices: List<TunnelVertex>,
    val keys: Set<Char>
) : Node<TunnelNodePart2>() {

    override fun getNeighbors(): Collection<TunnelNodePart2> {
        return tunnelVertices.flatMapIndexed { index, vertex ->
            val edges = tunnelGraph.edgesOf(vertex)
            edges
                .map { tunnelGraph.getEdgeTarget(it).takeIf { it != vertex } ?: tunnelGraph.getEdgeSource(it) }
                .mapNotNull {
                    val newVertices = tunnelVertices.toMutableList()
                    newVertices[index] = it
                    TunnelNodePart2(tunnelGraph, newVertices, keys)
                    when (val tunnelElement = it.first) {
                        is TunnelElement.Key -> TunnelNodePart2(tunnelGraph, newVertices, keys + tunnelElement.keyName)
                        is TunnelElement.Door -> TunnelNodePart2(tunnelGraph, newVertices, keys).takeIf { tunnelElement.canBeOpened(keys) }
                        else -> null
                    }
                }
        }
    }

    override fun distanceTo(other: TunnelNodePart2): Int {
        val verticesToConsider = Sets.symmetricDifference(this.tunnelVertices.toSet(), other.tunnelVertices.toSet())
        check(verticesToConsider.size == 2)

        val edge = tunnelGraph.getEdge(verticesToConsider.first(), verticesToConsider.last())
        return tunnelGraph.getEdgeWeight(edge).toInt()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TunnelNodePart2

        if (tunnelVertices != other.tunnelVertices) return false
        if (keys != other.keys) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tunnelVertices.hashCode()
        result = 31 * result + keys.hashCode()
        return result
    }

    override fun toString(): String {
        return "TunnelNodePart2(tunnelVertices=$tunnelVertices, keys=$keys)"
    }
}