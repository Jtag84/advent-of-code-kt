package year2023.day07

import arrow.core.identity
import cc.ekblad.konbini.*

enum class Card(private val cardChar: Char) {
    A('A'),
    K('K'),
    Q('Q'),
    J('J'),
    TEN('T'),
    NINE('9'),
    EIGHT('8'),
    SEVEN('7'),
    SIX('6'),
    FIVE('5'),
    FOUR('4'),
    THREE('3'),
    TWO('2'),
    JOKER('x');

    fun getParser() = parser { char(cardChar) }.map { this }
}

typealias Bid = Long

data class Hand(val cards: List<Card>, val jokerIndices: Set<Int> = emptySet()) : Comparable<Hand> {
    override fun compareTo(other: Hand): Int {
        val handType = HandTYpe.getHandType(this).compareTo(HandTYpe.getHandType(other))

        if (handType == 0) {
            for (i in cards.indices) {
                val thisCard = if (jokerIndices.contains(i)) {
                    Card.JOKER
                } else {
                    cards[i]
                }

                val otherCard = if (other.jokerIndices.contains(i)) {
                    Card.JOKER
                } else {
                    other.cards[i]
                }

                val comparison = thisCard.compareTo(otherCard)
                if (comparison != 0) {
                    return comparison
                }
            }
            return 0
        } else {
            return handType
        }
    }
}

enum class HandTYpe {
    FIVE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    THREE_OF_A_KIND,
    TWO_PAIR,
    ONE_PAIR,
    HIGH_CARD;

    companion object {
        fun getHandType(hand: Hand): HandTYpe {
            val groupedCards = hand.cards.groupBy(::identity).mapValues { it.value.size }

            return when {
                groupedCards.values.any { it == 5 } -> FIVE_OF_A_KIND
                groupedCards.values.any { it == 4 } -> FOUR_OF_A_KIND
                groupedCards.values.any { it == 3 }
                    .and(groupedCards.values.any { it == 2 }) -> FULL_HOUSE

                groupedCards.values.any { it == 3 } -> THREE_OF_A_KIND
                groupedCards.values.count { it == 2 } == 2 -> TWO_PAIR
                groupedCards.values.any { it == 2 } -> ONE_PAIR
                else -> HIGH_CARD
            }
        }
    }
}

val inputParser: Parser<List<Pair<Hand, Bid>>> = parser {
    chain1(handBidParser, whitespace).terms
}

val cardParser = parser {
    oneOf(*Card.entries.map(Card::getParser).toTypedArray())
}

val handParser: Parser<Hand> = parser {
    Hand(listOf(cardParser(), cardParser(), cardParser(), cardParser(), cardParser()))
}

val handBidParser: Parser<Pair<Hand, Bid>> = parser {
    val hand = handParser()
    whitespace()
    val bid = integer()
    Pair(hand, bid)
}