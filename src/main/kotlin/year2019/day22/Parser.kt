package year2019.day22

import cc.ekblad.konbini.*
import commons.*

val inputParser = parser {
    chain1(cardShuffleTechniqueParser, whitespace).terms
}

sealed class CardShuffleTechnique {
    abstract fun apply(cards : Pair<Head<Long>, Tail<Long>>, size: Long): Pair<Head<Long>, Tail<Long>>

    data object DealIntoNewStack : CardShuffleTechnique() {
        override fun apply(cards: Pair<Head<Long>, Tail<Long>>, size: Long) : Pair<Head<Long>, Tail<Long>> {
            return cards.second.reverse() to cards.first.reverse()
        }
    }

    data class CutNCards(val n : Long) : CardShuffleTechnique() {
        override fun apply(cards: Pair<Head<Long>, Tail<Long>>, size: Long): Pair<Head<Long>, Tail<Long>> {
            val (head, tail) = cards
            return if(n > 0) {
                val node= head.next(n - 1)!!
                val afterNode = node.next()!!
                node.setNext(null)
                afterNode.setPrevious(null)
                tail.setNext(head)
                head.setPrevious(tail)
                afterNode to node
            } else {
                val node= tail.previous(-n - 1)!!
                val beforeNode = node.previous()!!
                beforeNode.setNext(null)
                node.setPrevious(null)
                tail.setNext(head)
                head.setPrevious(tail)
                node to beforeNode
            }
        }
    }

    data class DealWithIncrementN(val n : Long) : CardShuffleTechnique() {
        override fun apply(cards: Pair<Head<Long>, Tail<Long>>, size: Long): Pair<Head<Long>, Tail<Long>> {
            val newArray = arrayOfNulls<Long>(size.toInt())

            var node: LinkedListNode<Long>? = cards.first
            var index = 0L
            while(node != null) {
                newArray.set(index.toInt(), node.value)

                index = (index + n) % size
                node = node.next()
            }

            return newArray.filterNotNull().toLinkedList()!!
        }
    }
}

val cardShuffleTechniqueParser = parser {
    oneOf(dealIntoNewStackParser, cutNCardsParser, dealWithIncrementNParser)
}

val dealIntoNewStackParser = parser {
    string("deal into new stack")
    CardShuffleTechnique.DealIntoNewStack
}

val cutNCardsParser = parser {
    string("cut ")
    val n = integer()
    CardShuffleTechnique.CutNCards(n)
}

val dealWithIncrementNParser = parser {
    string("deal with increment ")
    val n = integer()
    CardShuffleTechnique.DealWithIncrementN(n)
}