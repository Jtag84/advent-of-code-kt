package year2023.day04

import Part.Companion.part2
import arrow.core.memoize

fun main() {
    part2.runTest(30)
    part2.run()
}

val part2 = part2(scratchcardsParser) { myWinningNumbers ->
    val cards = myWinningNumbers
        .map { it.size }
        .mapIndexed { index, numberOfCards -> (index + 1) to numberOfCards }
        .toMap()

    cards.keys.sumOf { memoizedGenerateCards(cards, it) }
}

typealias CardNumber = Int
typealias Score = Int
typealias NumberOfCopies = Int

val memoizedGenerateCards = ::generateCards.memoize()

fun generateCards(cards: Map<CardNumber, Score>, processingCardId: CardNumber): NumberOfCopies {
    val score = cards.get(processingCardId) ?: throw IllegalStateException("No card $processingCardId")

    return if (score > 0) {
        1 + ((processingCardId + 1)..(processingCardId + score))
            .sumOf { memoizedGenerateCards(cards, it) }
    } else {
        1
    }
}