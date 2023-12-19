package year2023.day07

import com.google.common.collect.Sets
import commons.Part.Companion.part2

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 5905L) { handBidPairs ->
    handBidPairs
        .map { it.generateBestHandFromJokers() }
        .calculateTotalWinnings()
}

fun Pair<Hand, Bid>.generateBestHandFromJokers(): Pair<Hand, Bid> {
    val cards = this.first.cards

    val jokerIndices = cards.withIndex().filter { it.value == Card.J }.map { it.index }.toSet()

    if (jokerIndices.isEmpty()) {
        return this
    }

    val possibleJokerReplacements = cards.toSet().minus(Card.J).plus(Card.JOKER)

    val jokerReplacements = jokerIndices.map { possibleJokerReplacements }
    val allPossibleJokerCombinations = Sets.cartesianProduct(*jokerReplacements.toTypedArray())
    val jokerIndexToReplacements = allPossibleJokerCombinations.map { jokerIndices.zip(it) }

    val bestHand = jokerIndexToReplacements.map { replacements ->
        cards.mapIndexed { index, card ->
            replacements.find { it.first == index }
                ?.second
                ?: card
        }
    }
        .minOf { Hand(it, jokerIndices) }

    return Pair(bestHand, this.second)
}