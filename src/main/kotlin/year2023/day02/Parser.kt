package year2023.day02

import cc.ekblad.konbini.*
import year2023.day02.CubeColor.*

typealias Handful = Set<CubeColor>

data class Game(val gameId: Long, val handfuls: List<Handful>) {
    fun getMaxRed(): Long =
        handfuls.flatMap { handful -> handful.filterIsInstance<Red>() }.maxOf { it.numberOfRedCubes }

    fun getMaxGreen(): Long =
        handfuls.flatMap { handful -> handful.filterIsInstance<Green>() }.maxOf { it.numberOfGreenCubes }

    fun getMaxBlue(): Long =
        handfuls.flatMap { handful -> handful.filterIsInstance<Blue>() }.maxOf { it.numberOfBlueCubes }

}

sealed class CubeColor {
    data class Red(val numberOfRedCubes: Long) : CubeColor()
    data class Green(val numberOfGreenCubes: Long) : CubeColor()
    data class Blue(val numberOfBlueCubes: Long) : CubeColor()
}

val cubeColorParser = parser {
    whitespace()
    val numberOfCubes = integer()
    whitespace1()
    val color = regex("[a-z]*")
    when (color) {
        "red" -> Red(numberOfCubes)
        "blue" -> Blue(numberOfCubes)
        "green" -> Green(numberOfCubes)
        else -> {
            throw IllegalStateException("don't know $color")
        }
    }
}

val handfulParser: Parser<Handful> = parser {
    val handful = chain1(cubeColorParser, parser { string(", ") })
    handful.terms.toSet()
}

val gameParser: Parser<Game> = parser {
    string("Game ")
    val gameId = integer()
    char(':')
    val handfuls = chain1(handfulParser, parser { string("; ") })
    Game(gameId, handfuls.terms)
}

val gamesParser: Parser<Games> = parser {
    val games = chain1(gameParser, parser { char('\n') })
    games.terms
}

typealias Games = List<Game>