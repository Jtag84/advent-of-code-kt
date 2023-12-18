package commons.search


abstract class Node<T : Node<T>> : Comparable<Node<*>> {
    private var fScore: Int = Int.MAX_VALUE

    abstract fun getNeighbors(cameFrom: Map<T, T>): Collection<T>

    abstract fun distanceTo(other: T, cameFrom: Map<T, T> = emptyMap()): Int

    fun withFScore(fScore: Int): Node<T> {
        this.fScore = fScore
        return this
    }

    override fun compareTo(other: Node<*>): Int {
        return this.fScore - other.fScore
    }
}
