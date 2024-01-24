package year2019.day22

import cc.ekblad.konbini.*
import commons.*
import java.math.BigInteger

val inputParser = parser {
    chain1(cardShuffleTechniqueParser, whitespace).terms
}

sealed class CardShuffleTechnique {
    abstract fun toCardShuffleLCF(cardDeckSize: BigInteger) : CardShuffleLCF

    data object DealIntoNewStack : CardShuffleTechnique() {
        override fun toCardShuffleLCF(cardDeckSize: BigInteger) : CardShuffleLCF {
            return CardShuffleLCF((-1).toBigInteger(), (cardDeckSize - BigInteger.ONE), cardDeckSize)
        }
    }

    data class CutNCards(val n : Long) : CardShuffleTechnique() {
        override fun toCardShuffleLCF(cardDeckSize: BigInteger) : CardShuffleLCF {
            val effectiveCut = if(this.n < 0) {cardDeckSize + this.n.toBigInteger()} else {this.n.toBigInteger()}
            return CardShuffleLCF(BigInteger.ONE, effectiveCut, cardDeckSize)
        }
    }

    data class DealWithIncrementN(val n : Long) : CardShuffleTechnique() {
        override fun toCardShuffleLCF(cardDeckSize: BigInteger) : CardShuffleLCF {
            val effectiveModulus = this.n.toBigInteger().modInverse(cardDeckSize)
            return CardShuffleLCF(effectiveModulus, BigInteger.ZERO, cardDeckSize)
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