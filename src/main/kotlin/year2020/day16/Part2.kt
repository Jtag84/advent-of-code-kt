package year2020.day16

import com.google.common.collect.Sets
import commons.Part.Companion.part2

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 156L) { (fields, myTicket, nearbyTickets) ->
    val ranges = fields.values.flatten()
    val validNearbyTickets = nearbyTickets.filterNot { ticket -> ticket.any { number -> ranges.none { it.contains(number) } } }

    val possibleFieldPositions = validNearbyTickets.flatMap { ticket ->
            ticket.mapIndexed{index, number ->
                index to fields.filter {
                    fieldEntry -> fieldEntry.value.any {
                    it.contains(number)
                    }
                }.keys
            }
        }
        .groupBy ({ it.first }, {it.second})
        .mapValues { it.value.reduce(Sets::intersection) }
        .toMutableMap()

    val knownFieldPositions = possibleFieldPositions.filter { it.value.size == 1 }.values.flatten().toMutableSet()

    while(possibleFieldPositions.count { it.value.size > 1 } > 0) {
        possibleFieldPositions.replaceAll { _, fieldNames ->
            if(fieldNames.size > 1) {
                val newFieldNames = fieldNames - knownFieldPositions
                if(newFieldNames.size == 1) {
                    knownFieldPositions.addAll(newFieldNames)
                }
                newFieldNames
            }
            else {
                fieldNames
            }
        }
    }

    val fieldPositions = possibleFieldPositions.mapValues {
            check(it.value.size == 1)
            it.value.first()
        }

    val departureFieldPositions = fieldPositions.filter { it.value.startsWith("departure") }.keys
    departureFieldPositions.map { myTicket[it] }.reduce(Math::multiplyExact)
}