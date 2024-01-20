package year2019.day18

import commons.Coordinates2d
import commons.Part.Companion.part1
import commons.cardinals
import commons.search.AStar
import commons.search.JgraphtNode
import commons.search.Node
import org.jgrapht.Graph
import org.jgrapht.graph.DefaultUndirectedWeightedGraph
import org.jgrapht.graph.DefaultWeightedEdge


fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

typealias TunnelVertex = Pair<TunnelElement, Coordinates2d>

val part1 = part1(inputParser, 136) { tunnelMap ->
    val entrance = tunnelMap.entries.first { it.value == TunnelElement.Entrance }.key
    val allKeys = tunnelMap.values.filterIsInstance<TunnelElement.Key>().map { it.keyName }.toSet()

    val contractedGraph: Graph<TunnelVertex, DefaultWeightedEdge> = DefaultUndirectedWeightedGraph(DefaultWeightedEdge::class.java)

    val entranceVertex = TunnelVertex(TunnelElement.Entrance, entrance)
    contractedGraph.addVertex(entranceVertex)

    buildContractedGraph(
        entranceVertex,
        0,
        contractedGraph,
        entrance,
        tunnelMap,
        mutableSetOf(),
        )

    // uncomment to create a png of the graph
//    if(isTest) {
//        contractedGraph.printGraph("graphPrintTest.png")
//    }else {
//        contractedGraph.printGraph("graphPrint.png")
//    }

    val entranceNodeFirstSolution = TunnelNodeFirstSolution(entrance, emptySet(), tunnelMap)
    AStar.search(
        entranceNodeFirstSolution,
        {allKeys.size - it.keys.size},
        {it.keys.size == allKeys.size}
    )
        .first

    // It's fast but the answer is 4 off on the actual inputs while correct on all the test cases.
    // My previous solution was much slower but got the right answer
//    val entranceNode = TunnelNode(contractedGraph, entranceVertex, emptySet())
//    AStar.search(
//        entranceNode,
//        {allKeys.size - (it as TunnelNode).keys.size},
//        {(it as TunnelNode).keys.size == allKeys.size}
//    )
//        .first
}

data class TunnelNode(val tunnelGraph: Graph<TunnelVertex, DefaultWeightedEdge>, val tunnelVertex: TunnelVertex, val keys: Set<Char>) :
    JgraphtNode<TunnelVertex, DefaultWeightedEdge>(tunnelGraph, tunnelVertex) {
    override fun getNeighbors(): Collection<JgraphtNode<TunnelVertex, DefaultWeightedEdge>> {
        return super.getNeighbors()
            .filterNot { it.vertex.first is TunnelElement.Entrance }
            .mapNotNull {
                when(val tunnelElement = it.vertex.first) {
                    is TunnelElement.Key -> TunnelNode(tunnelGraph, it.vertex, keys + tunnelElement.keyName)
                    is TunnelElement.Door -> TunnelNode(tunnelGraph, it.vertex, keys).takeIf { tunnelElement.canBeOpened(keys) }
                    else -> null
                }
            }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TunnelNode

        if (tunnelVertex != other.tunnelVertex) return false
        if (keys != other.keys) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tunnelVertex.hashCode()
        result = 31 * result + keys.hashCode()
        return result
    }

    override fun toString(): String {
        return "TunnelNode(tunnelVertex=$tunnelVertex, keys=$keys)"
    }
}

fun buildContractedGraph(currentVertex: TunnelVertex,
                         currentStepCount: Int,
                         graph: Graph<TunnelVertex, DefaultWeightedEdge>,
                         currentCoordinates: Coordinates2d,
                         tunnelMap: Map<Coordinates2d, TunnelElement>,
                         alreadyVisitedCoordinates: MutableSet<Pair<Coordinates2d, TunnelVertex>>) {
    val currentTunnelElement = tunnelMap[currentCoordinates]!!
    alreadyVisitedCoordinates.add(currentCoordinates to currentVertex)

    when(currentTunnelElement) {
        TunnelElement.Entrance, TunnelElement.Empty -> {
            getNonVisitedNeighbors(currentCoordinates, currentVertex, alreadyVisitedCoordinates)
                .forEach { buildContractedGraph(currentVertex, currentStepCount + 1, graph, it, tunnelMap, alreadyVisitedCoordinates) }
        }
        is TunnelElement.Door, is TunnelElement.Key -> {
            val newTunnelVertex = TunnelVertex(currentTunnelElement, currentCoordinates)
            graph.addVertex(newTunnelVertex)
            val existingEdges = graph.getAllEdges(currentVertex, newTunnelVertex)
            if(existingEdges.isNullOrEmpty().not()) {
                check(existingEdges.size == 1)
                val existingEdge = existingEdges.first()
                if(currentStepCount < graph.getEdgeWeight(existingEdge)) {
                    graph.setEdgeWeight(existingEdge, currentStepCount.toDouble())
                }
            }
            val edge: DefaultWeightedEdge? = graph.addEdge(currentVertex, newTunnelVertex)
            edge?.let { graph.setEdgeWeight(it, currentStepCount.toDouble())}

            getNonVisitedNeighbors(currentCoordinates, newTunnelVertex, alreadyVisitedCoordinates)
                .forEach { buildContractedGraph(newTunnelVertex, 1, graph, it, tunnelMap, alreadyVisitedCoordinates) }
        }
        TunnelElement.Wall -> {} // Do nothing
    }
}

fun getNonVisitedNeighbors(currentCoordinates: Coordinates2d, currentVertex: TunnelVertex, alreadyVisitedCoordinates: Set<Pair<Coordinates2d, TunnelVertex>>) : List<Coordinates2d> {
    return currentCoordinates.cardinals()
        .filterNot { alreadyVisitedCoordinates.contains(it to currentVertex) }
}

// previous solution that gives a correct answer but takes 4s
data class TunnelNodeFirstSolution(
    val coordinates: Coordinates2d,
    val keys: Set<Char>,
    val tunnelMap : Map<Coordinates2d, TunnelElement>
) : Node<TunnelNodeFirstSolution>() {
    override fun getNeighbors(): Collection<TunnelNodeFirstSolution> {
        return coordinates.cardinals()
            .mapNotNull { cardinalCoordinates ->
                when(val element = tunnelMap[cardinalCoordinates]!!) {
                    is TunnelElement.Door -> TunnelNodeFirstSolution(cardinalCoordinates, keys, tunnelMap).takeIf {element.canBeOpened(keys)}
                    TunnelElement.Empty -> TunnelNodeFirstSolution(cardinalCoordinates, keys, tunnelMap)
                    TunnelElement.Entrance -> TunnelNodeFirstSolution(cardinalCoordinates, keys, tunnelMap)
                    is TunnelElement.Key -> TunnelNodeFirstSolution(cardinalCoordinates, keys + element.keyName, tunnelMap)
                    TunnelElement.Wall -> null
                }
            }
    }

    override fun distanceTo(other: TunnelNodeFirstSolution): Int {
        return 1
    }

    override fun toString(): String {
        return "TunnelNode(coordinates=$coordinates, keys=$keys)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TunnelNodeFirstSolution

        if (coordinates != other.coordinates) return false
        if (keys != other.keys) return false

        return true
    }


    var hashcode : Int? = null
    override fun hashCode(): Int {
        if(hashcode == null) {
            hashcode = keys.sorted().fold(coordinates.x.toInt() * 3671 + coordinates.y.toInt()) {acc, c -> acc * 3671 + c.code }
        }
        return hashcode!!
    }
}