package year2023.day03

import cc.ekblad.konbini.*
import commons.Coordinates2d
import commons.right

sealed class SchematicItem {
    data class PartNumber(val number: Long, val coordinates: Coordinates2d) : SchematicItem()
    data class Symbol(val symbol: Char, val coordinates: Coordinates2d) : SchematicItem()

    data class Empty(val coordinates: Coordinates2d) : SchematicItem()
}

fun SchematicItem.PartNumber.allNumberCoordinates(): Sequence<Coordinates2d> {
    val numberOfDigits = number.toString().length

    return generateSequence(coordinates, Coordinates2d::right).take(numberOfDigits)
}

val partNumberParser = { lineNumber: Long, xOffset: Long ->
    parser {
        val currentPosition = position - xOffset
        val partNumber = regex("[0-9]+").toLong()
        SchematicItem.PartNumber(partNumber, Coordinates2d(currentPosition, lineNumber))
    }
}

val symbolParser = { lineNumber: Long, xOffset: Long ->
    parser {
        val currentPosition = position - xOffset
        val symbol = regex("[^\\.\\d\\n]").let {
            check(it.length == 1)
            it[0]
        }
        SchematicItem.Symbol(symbol, Coordinates2d(currentPosition, lineNumber))
    }
}

val emptyParser = { lineNumber: Long, xOffset: Long ->
    parser {
        val currentPosition = position - xOffset
        char('.')
        SchematicItem.Empty(Coordinates2d(currentPosition, lineNumber))
    }
}

val schematicItemParser = { lineNumber: Long, xOffset: Long ->
    parser {
        oneOf(
            emptyParser(lineNumber, xOffset),
            partNumberParser(lineNumber, xOffset),
            symbolParser(lineNumber, xOffset)
        )
    }
}

val lineParser = { lineNumber: Long ->
    parser {
        many1(schematicItemParser(lineNumber, position.toLong()))
    }
}

val schematicsItemParser = parser {
    whitespace()
    var lineNumber = 0L

    val schematicItems = mutableSetOf<SchematicItem>()
    var parsedLine = tryParse(lineParser(lineNumber))

    while (parsedLine != null) {
        schematicItems.addAll(parsedLine)
        whitespace()
        lineNumber++
        parsedLine = tryParse(lineParser(lineNumber))
    }
    schematicItems.toSet()
}