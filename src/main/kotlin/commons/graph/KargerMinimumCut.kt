package commons.graph

import commons.graph.Vertex.MergedVertex
import kotlin.random.Random
import kotlin.random.nextUInt

sealed class  Vertex<T> {
    abstract val edges: List<Edge<T>>

    data class SimpleVertex<T> (val id: T, override var edges: List<Edge<T>>) : Vertex<T>() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as SimpleVertex<*>

            return id == other.id
        }

        override fun hashCode(): Int {
            return id?.hashCode() ?: 0
        }

        override fun toString(): String {
            return "SimpleVertex(id=$id)"
        }
    }

    data class MergedVertex<T> (val vertices: Set<Vertex<T>>, override var edges: List<Edge<T>>) : Vertex<T>() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as MergedVertex<*>

            return vertices == other.vertices
        }

        override fun hashCode(): Int {
            return vertices.hashCode()
        }

        override fun toString(): String {
            return "MergedVertex(vertices=$vertices)"
        }
    }
}

data class Edge<T> (var vertex1: Vertex<T>, var vertex2: Vertex<T>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Edge<*>

        return setOf(vertex1, vertex2) == setOf(other.vertex1, other.vertex2)
    }

    override fun hashCode(): Int {
        return setOf(vertex1, vertex2).hashCode()
    }
}

data class Graph<T> (val vertices: Set<Vertex.SimpleVertex<T>>)

fun <T> Graph<T>.kargerMinCut() : Pair<Int, Set<Vertex<T>>> {

    val verticesCopyMap = this.vertices.associateWith { it.copy() }
    val edgesCopyMap = verticesCopyMap.values
        .flatMap { vertex -> vertex.edges.map { it to Edge(verticesCopyMap[it.vertex1]!!, verticesCopyMap[it.vertex2]!!)} }
        .toMap()

    verticesCopyMap.mapValues { vertexEntry -> vertexEntry.value.edges = vertexEntry.value.edges.map { edgesCopyMap[it]!! }.toList() }

    val verticesLeft:MutableSet<Vertex<T>> = verticesCopyMap.values.toMutableSet()

    while(verticesLeft.size > 2) {
        val edgesLeft = verticesLeft
            .flatMap { it.edges }

        val nextIndex = Random.nextInt(edgesLeft.size)
        val edgeToContract = edgesLeft[nextIndex]

        verticesLeft.remove(edgeToContract.vertex1)
        verticesLeft.remove(edgeToContract.vertex2)
        verticesLeft.add(edgeToContract.contractVertices())
    }

    return verticesLeft.first().edges.count { it.vertex1 != it.vertex2 } to verticesLeft
}

fun <T> Edge<T>.contractVertices() : MergedVertex<T>{
    val edges = this.vertex1.edges.toMutableList()
    edges.addAll(this.vertex2.edges)
    edges.removeAll { it == this || it.vertex1 == it.vertex2 }
    val newVertex = MergedVertex(this.vertex1.expandVertices() + this.vertex2.expandVertices(), edges)
    edges.forEach {
        if (it.vertex1 == this.vertex1 || it.vertex1 == this.vertex2) {
            it.vertex1 = newVertex
        }
        if (it.vertex2 == this.vertex1 || it.vertex2 == this.vertex2) {
            it.vertex2 = newVertex
        }
    }
    edges.removeAll { it == this || it.vertex1 == it.vertex2 }
    return newVertex
}

fun <T> Vertex<T>.expandVertices() : Set<Vertex<T>> {
    return when(this) {
        is MergedVertex -> this.vertices
        is Vertex.SimpleVertex -> setOf(this)
    }
}