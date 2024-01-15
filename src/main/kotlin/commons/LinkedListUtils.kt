package commons

import arrow.core.tail

class LinkedListNode<T>(val value:T) : Iterable<LinkedListNode<T>>{
    var previous: LinkedListNode<T>? = null
    var next: LinkedListNode<T>? = null

    fun insertNodeAfter(node: LinkedListNode<T>) {
        val nodeNext = next
        node.previous = this
        this.next = node
        node.next = nodeNext
        nodeNext?.previous = node
    }

    fun insertNodeBefore(node: LinkedListNode<T>) {
        val nodePrevious = previous
        node.next = this
        this.previous = node
        node.previous = nodePrevious
        nodePrevious?.next = node
    }

    fun removeNode() {
        this.next?.previous = this.previous
        this.previous?.next = this.next
        this.next = null
        this.previous = null
    }

    fun moveThisAfter(node: LinkedListNode<T>) {
        this.removeNode()
        node.insertNodeAfter(this)
    }

    fun moveThisBefore(node: LinkedListNode<T>) {
        this.removeNode()
        node.insertNodeBefore(this)
    }

    fun removeSectionAfter(sectionSize: Int) : Pair<Head<T>, Tail<T>> {
        val section = this.asSequence().drop(1).take(sectionSize)
        val head = section.first()
        val tail = section.last()
        head.previous?.next = tail.next
        tail.next?.previous = head.previous

        head.previous = null
        tail.next = null

        return head to tail
    }

    fun insertSectionAfter(head: Head<T>, tail: Tail<T>) {
        val nodeNext = next
        head.previous = this
        this.next = head
        tail.next = nodeNext
        nodeNext?.previous = tail
    }

    override fun iterator(): Iterator<LinkedListNode<T>> {
        return LinkedListNodeIterator(this)
    }
}

class LinkedListNodeIterator<T>(private var currentLinkedListNode: LinkedListNode<T>?) : ListIterator<LinkedListNode<T>> {
    override fun hasNext(): Boolean {
        return currentLinkedListNode != null
    }

    override fun hasPrevious(): Boolean {
        return currentLinkedListNode != null
    }

    override fun next(): LinkedListNode<T> {
        val toReturn = currentLinkedListNode!!
        this.currentLinkedListNode = currentLinkedListNode!!.next
        return toReturn
    }

    override fun nextIndex(): Int {
        throw UnsupportedOperationException("")
    }

    override fun previous(): LinkedListNode<T> {
        val toReturn = currentLinkedListNode!!
        this.currentLinkedListNode = currentLinkedListNode!!.previous
        return toReturn
    }

    override fun previousIndex(): Int {
        throw UnsupportedOperationException("")
    }
}

typealias Head<T> = LinkedListNode<T>
typealias Tail<T> = LinkedListNode<T>
typealias NodeMap<T> = MutableMap<T, LinkedListNode<T>>

fun <T> List<T>.toHeadTailAndNodeMap() : Triple<Head<T>, Tail<T>, NodeMap<T>> {
    val nodeMap = newNodeMap<T>()

    val nodes = this.map { nodeMap.createNode(it) }
    val tail = nodes.reduce { previous, current -> previous.insertNodeAfter(current); current }

    return Triple(nodes.first(), tail, nodeMap)
}

fun <T> newNodeMap() = mutableMapOf<T,LinkedListNode<T>>()

fun <T> MutableMap<T,LinkedListNode<T>>.createNode(value: T) : LinkedListNode<T> {
    val newNode = LinkedListNode(value)
    this.put(value, newNode)
    return newNode
}