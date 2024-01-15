package year2020.day24

import commons.HexagonGridCoordinates
import commons.Part.Companion.part2

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 2208) { tileDirections ->
    val blackTiles = getBlackTiles(tileDirections)
    generateSequence(blackTiles) { currentBlackTiles ->
        val allTiles = currentBlackTiles + currentBlackTiles.flatMap { it.allAround() }.toSet()
        allTiles.filter { tile ->
            val isBlack = currentBlackTiles.contains(tile)
            val countAdjacentBlacks = tile.allAround().count { currentBlackTiles.contains(it) }
            when {
                isBlack && (countAdjacentBlacks == 0 || countAdjacentBlacks > 2) -> false
                isBlack -> true
                isBlack.not() && countAdjacentBlacks == 2 -> true
                else -> false
            }
        }.toSet()
    }
        .drop(100)
        .first()
        .size
}