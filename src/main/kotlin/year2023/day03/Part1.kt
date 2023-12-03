package year2023.day03

import Coordinates
import Part1Function
import aroundWithDiagonals
import arrow.core.partially1
import cc.ekblad.konbini.*
import parseOrThrowException
import right
import run
import runTest

fun main() {
    part1.runTest(4361L)
    part1.run()
}

sealed class SchematicItem {
    data class PartNumber(val number:Long, val coordinates: Coordinates) : SchematicItem()
    data class Symbol(val symbol:Char, val coordinates: Coordinates): SchematicItem()

    data class Empty(val coordinates: Coordinates) : SchematicItem()
}

fun SchematicItem.PartNumber.allNumberCoordinates(): Sequence<Coordinates> {
    val numberOfDigits = number.toString().length
    return generateSequence(coordinates, Coordinates::right).take(numberOfDigits)
}

val partNumberParser = { lineNumber:Long ->
    parser {
        val currentPosition = position
        val partNumber = regex("[0-9]+").toLong()
        SchematicItem.PartNumber(partNumber, Coordinates(currentPosition.toLong(), lineNumber))
    }
}

val symbolParser = { lineNumber:Long ->
    parser {
        val currentPosition = position
        val symbol = regex("[^\\.\\d]").let {
            check(it.length == 1)
            it[0]
        }
        SchematicItem.Symbol(symbol, Coordinates(currentPosition.toLong(), lineNumber))
    }
}

val emptyParser = { lineNumber:Long ->
    parser {
        val currentPosition = position
        char('.')
        SchematicItem.Empty(Coordinates(currentPosition.toLong(), lineNumber))
    }
}

val schematicItemParser = { lineNumber:Long ->
    parser {
        oneOf(emptyParser(lineNumber), partNumberParser(lineNumber), symbolParser(lineNumber))
    }
}

val lineParser = { lineNumber:Long ->
    parser {
        many1(schematicItemParser(lineNumber))
    }
}

val part1 = Part1Function { input ->
    val schematicItems = input.lines()
        .filter(String::isNotBlank)
        .flatMapIndexed { lineNumber, line -> lineParser(lineNumber.toLong()).parseOrThrowException(line).toSet() }
    val symbols = schematicItems.filterIsInstance<SchematicItem.Symbol>().toSet()
    val partNumbers = schematicItems.filterIsInstance<SchematicItem.PartNumber>().toSet()

    partNumbers
        .filter(::isNextToSymbol.partially1(symbols))
        .sumOf { it.number }
}

fun isNextToSymbol(symbols: Set<SchematicItem.Symbol>, partNumber: SchematicItem.PartNumber) : Boolean{
    val allNumberCoordinates = partNumber.allNumberCoordinates()
    val aroundNumberCoordinates = allNumberCoordinates.flatMap { it.aroundWithDiagonals()}.minus(allNumberCoordinates.toSet())
    val symbolsCoordinates = symbols.map { it.coordinates }

    return aroundNumberCoordinates.any {it in symbolsCoordinates}
}

