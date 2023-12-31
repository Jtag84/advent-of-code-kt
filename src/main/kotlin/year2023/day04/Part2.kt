package year2023.day04

import arrow.core.memoize
import commons.Part.Companion.part2

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(scratchcardsParser, 30) { myWinningNumbers ->
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