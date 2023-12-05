package year2023.day04

import Part2Function
import arrow.core.memoize
import com.google.common.collect.Sets
import parseOrThrowException
import run
import runTest

fun main() {
    part2.runTest(30)
    part2.run()
}

val part2 = Part2Function { input ->
    val cards = input.lines()
        .filter(String::isNotBlank)
        .map { scratchcardParser.parseOrThrowException(it) }
        .map { Sets.intersection(it.first, it.second).toSet() }
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