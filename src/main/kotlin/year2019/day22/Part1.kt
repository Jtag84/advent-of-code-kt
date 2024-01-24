package year2019.day22

import commons.Part.Companion.part1
import commons.*

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 8) { techniques ->

    val range = if(isTest) {
        (0..9L)
    } else {
        (0..10_006L)
    }

    val valueToFind = if(isTest) {3L} else {2019L}

    val cards = range.toLinkedList()!!

    val size = range.endInclusive + 1
    val shuffledCards = techniques.fold(cards) { currentCards, shuffleTechnique -> shuffleTechnique.apply(currentCards, size)}.first.toList().map { it.value }

    shuffledCards.withIndex().first{it.value == valueToFind}.index
}