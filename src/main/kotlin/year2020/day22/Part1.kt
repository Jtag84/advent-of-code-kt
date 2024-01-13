package year2020.day22

import arrow.core.tail
import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 306L) { playerHands ->
    generateSequence(playerHands) { (player1Hand, player2Hand) ->
            val player1Card = player1Hand.first()
            val player2Card = player2Hand.first()

            if(player1Card > player2Card) {
                player1Hand.tail() + player1Card + player2Card to player2Hand.tail()
            } else {
                 player1Hand.tail() to player2Hand.tail() + player2Card + player1Card
            }
        }
        .first { it.first.isEmpty() || it.second.isEmpty() }
        .calculateScore()
}

fun Pair<List<Long>, List<Long>>.calculateScore() =
    (this.first + this.second).reversed().mapIndexed { index, card -> (index + 1) * card }.sum()