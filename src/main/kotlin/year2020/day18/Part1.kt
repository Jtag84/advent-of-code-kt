package year2020.day18

import arrow.core.tail
import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 26406L) { operationLines ->
    operationLines.sumOf { it.calculate() }
}

fun List<Operation>.calculate(total: Long = 0L) : Long {
    if(this.isEmpty()) {
        return total
    }

    return when(val operation = this.last()) {
        Operation.ClosingParenthesis -> {
                var closingParenthesisCount = 0
                this.dropLast(1)
                    .dropLastWhile {
                        when {
                            it == Operation.ClosingParenthesis -> {
                                    closingParenthesisCount++
                                    true
                                }
                            it == Operation.OpeningParenthesis && closingParenthesisCount == 0 -> false
                            it == Operation.OpeningParenthesis && closingParenthesisCount > 0 -> {
                                    closingParenthesisCount--
                                    true
                                }
                            else -> true
                        }
                    }.dropLast(1).calculate(this.dropLast(1).calculate())
            }
        Operation.Multiply -> total * this.dropLast(1).calculate()
        is Operation.Number -> this.dropLast(1).calculate(operation.number)
        Operation.OpeningParenthesis -> total
        Operation.Plus -> total + this.dropLast(1).calculate()
    }
}