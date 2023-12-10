package year2023.day03

import arrow.core.partially1
import commons.Part.Companion.part1
import commons.aroundWithDiagonals

fun main() {
    part1.runTest()
    part1.run()
}

val part1 = part1(schematicsItemParser, 4361L) { schematicItems ->
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

