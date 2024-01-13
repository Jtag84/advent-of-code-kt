package year2020.day22

import arrow.core.tail
import commons.Part.Companion.part2

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 291L) { playerHands ->
    playRecursiveGame(playerHands).calculateScore()
}

fun playRecursiveGame(playerHands : Pair<List<Long>, List<Long>>) :Pair<List<Long>, List<Long>>{
    if(playerHands.first.isEmpty() || playerHands.second.isEmpty()) {
        return playerHands
    }

    val alreadyPlayedHands = mutableSetOf<Pair<List<Long>, List<Long>>>()
    return generateSequence(playerHands) { currentPlayerHands ->
            val (player1Hand, player2Hand) = currentPlayerHands
            val player1Card = player1Hand.first()
            val remainingPlayer1Hand = player1Hand.tail()
            val player2Card = player2Hand.first()
            val remainingPlayer2Hand = player2Hand.tail()

            val nextHand = when {
                currentPlayerHands in alreadyPlayedHands -> player1Hand to emptyList()
                remainingPlayer1Hand.size >= player1Card && remainingPlayer2Hand.size >= player2Card -> {
                    val (subGamePlayer1Hand, _) = playRecursiveGame(remainingPlayer1Hand.take(player1Card.toInt()) to remainingPlayer2Hand.take(player2Card.toInt()))
                    if(subGamePlayer1Hand.isEmpty()) {
                        remainingPlayer1Hand to remainingPlayer2Hand + player2Card + player1Card
                    } else {
                        remainingPlayer1Hand + player1Card + player2Card to remainingPlayer2Hand
                    }
                }
                player1Card > player2Card -> remainingPlayer1Hand + player1Card + player2Card to remainingPlayer2Hand
                else -> remainingPlayer1Hand to remainingPlayer2Hand + player2Card + player1Card
            }

            alreadyPlayedHands.add(currentPlayerHands)

            nextHand
        }
        .first { it.first.isEmpty() || it.second.isEmpty() }
}