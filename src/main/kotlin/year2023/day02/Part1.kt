package year2023.day02

import Part1Function
import cc.ekblad.konbini.*
import parseOrThrowException
import run
import runTest
import year2023.day02.CubeColor.*

fun main() {
    part1.runTest(8L)
    part1.run()
}

typealias Handful = Set<CubeColor>

data class Game(val gameId:Long, val handfuls: List<Handful>) {
    fun getMaxRed() : Long = handfuls.flatMap { handful -> handful.filterIsInstance<Red>() }.maxOf { it.numberOfRedCubes }
    fun getMaxGreen() : Long = handfuls.flatMap { handful -> handful.filterIsInstance<Green>() }.maxOf { it.numberOfGreenCubes }
    fun getMaxBlue() : Long = handfuls.flatMap { handful -> handful.filterIsInstance<Blue>() }.maxOf { it.numberOfBlueCubes }

}

sealed class CubeColor {
    data class Red(val numberOfRedCubes: Long): CubeColor()
    data class Green(val numberOfGreenCubes: Long): CubeColor()
    data class Blue(val numberOfBlueCubes: Long): CubeColor()
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
        else -> {throw IllegalStateException("don't know $color")}
    }
}

val handfulParser: Parser<Handful> = parser {
    val handful = chain1(cubeColorParser, parser {string(", ")})
    handful.terms.toSet()
}

val gameParser: Parser<Game> = parser {
    string("Game ")
    val gameId = integer()
    char(':')
    val handfuls = chain1(handfulParser, parser{string("; ")})
    Game(gameId, handfuls.terms)
}

val gamesParser: Parser<List<Game>> = parser {
    val games = chain1(gameParser, parser{char('\n')})
    games.terms
}

val part1 = Part1Function { inputs ->
    gamesParser.parseOrThrowException(inputs)
        .filter {
            it.getMaxRed() <= 12 && it.getMaxGreen() <= 13 && it.getMaxBlue() <= 14
        }
        .sumOf { it.gameId }
    }
