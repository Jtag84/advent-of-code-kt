package commons.search

import org.jgrapht.Graph


abstract class Node<T : Node<T>> : Comparable<Node<*>> {
    private var fScore: Int = Int.MAX_VALUE

    abstract fun getNeighbors(): Collection<T>

    abstract fun distanceTo(other: T): Int

    fun withFScore(fScore: Int): Node<T> {
        this.fScore = fScore
        return this
    }

    override fun compareTo(other: Node<*>): Int {
        return this.fScore.compareTo(other.fScore)
    }
}

open class JgraphtNode<V,E>(val graph: Graph<V,E>, val vertex: V) : Node<JgraphtNode<V,E>>() {
    override fun getNeighbors(): Collection<JgraphtNode<V,E>> {
        val edges = graph.edgesOf(vertex)
        return edges
            .map { graph.getEdgeTarget(it).takeIf { it != vertex }?:graph.getEdgeSource(it) }
            .map { JgraphtNode(graph, it) }
    }

    override fun distanceTo(other: JgraphtNode<V,E>): Int {
        val edge = graph.getEdge(this.vertex, other.vertex)
        return graph.getEdgeWeight(edge).toInt()
    }
}