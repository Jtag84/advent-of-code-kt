package commons.graph

import commons.graph.Vertex.SimpleVertex
import commons.println
import org.junit.jupiter.api.Test

class KargerMinimumCutKtTest {

    @Test
    fun kargerMinCut() {
    /* Let us create following unweighted graph
        0------1---4
        | \    |  /
        |   \  | /
        |     \|/
        2------3   */

        val v0 = SimpleVertex(0, emptyList())
        val v1 = SimpleVertex(1, emptyList())
        val v2 = SimpleVertex(2, emptyList())
        val v3 = SimpleVertex(3, emptyList())
        val v4 = SimpleVertex(4, emptyList())

        val edge0to1 = Edge(v0, v1)
        val edge0to2 = Edge(v0, v2)
        val edge0to3 = Edge(v0, v3)
        val edge2to3 = Edge(v2, v3)
        val edge1to3 = Edge(v1, v3)
        val edge1to4 = Edge(v1, v4)
        val edge3to4 = Edge(v3, v4)

        v0.edges = listOf(edge0to1, edge0to2, edge0to3)
        v1.edges = listOf(edge0to1, edge1to3, edge1to4)
        v2.edges = listOf(edge0to2, edge2to3)
        v3.edges = listOf(edge2to3, edge1to3, edge0to3, edge3to4)
        v4.edges = listOf(edge1to4, edge3to4)

        val graph = Graph(setOf(v0, v1, v2, v3, v4))

        val (minCut, verticesSets) = generateSequence { graph.kargerMinCut() }.take(10).minBy{it.first}
        verticesSets.println()

        check(minCut == 2) { "min cut was $minCut" }
    }
}