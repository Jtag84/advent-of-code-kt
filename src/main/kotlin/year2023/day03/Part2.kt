package year2023.day03

import arrow.core.partially1
import com.google.common.collect.Sets
import commons.Coordinates
import commons.Part.Companion.part2
import commons.aroundWithDiagonals

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(schematicsItemParser, 467835L) { schematicItems ->
    val gears = schematicItems.filterIsInstance<SchematicItem.Symbol>()
        .filter { it.symbol == '*' }.toSet()
    val gearCoordinates = gears.map(SchematicItem.Symbol::coordinates).toSet()

    val partNumbers = schematicItems.filterIsInstance<SchematicItem.PartNumber>().toSet()

    partNumbers
        .asSequence()
        .filter(::isNextToSymbol.partially1(gears))
        .groupBy {
            val aroundCoordinates = it.allNumberCoordinates().flatMap(Coordinates::aroundWithDiagonals).minus(it).toSet()
            Sets.intersection(gearCoordinates, aroundCoordinates).let { gearSet ->
                check(gearSet.size==1)
                gearSet.first()
            }
        }
        .filter { it.value.size == 2 }
        .map { it.value[0].number * it.value[1].number }
        .sum()
}