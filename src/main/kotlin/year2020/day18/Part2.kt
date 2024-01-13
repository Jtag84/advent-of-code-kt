package year2020.day18

import arrow.core.tail
import commons.Part.Companion.part2
import commons.*

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 694_173L) { operationLines ->
    operationLines.sumOf { it.calculatePart2().number }
}

fun List<Operation>.calculatePart2() : Operation.Number {
    val reducedParenthesis = generateSequence(this) {it.reduceParenthesis()}.dropWhile { it.contains(Operation.OpeningParenthesis) }.first()

    return reducedParenthesis
        .splitBy{it == Operation.Multiply}
        .map { plusOperations ->
            plusOperations.filterIsInstance<Operation.Number>()
                .sumOf{it.number} }
        .reduce(Math::multiplyExact)
        .let { Operation.Number(it) }
}

fun List<Operation>.reduceParenthesis() : List<Operation> {
    val indexOfFirstOpenedParenthesis = this.indexOfFirst { it == Operation.OpeningParenthesis }

    if(indexOfFirstOpenedParenthesis < 0) {
        return this
    }

    var openParenthesisCount = 0
    val indexOfMatchingClosedParenthesis = indexOfFirstOpenedParenthesis + 1 + this.subList(indexOfFirstOpenedParenthesis + 1, this.size).indexOfFirst {
            when {
                it == Operation.ClosingParenthesis && openParenthesisCount == 0 -> true
                it == Operation.ClosingParenthesis && openParenthesisCount > 0 -> {openParenthesisCount--; false}
                it == Operation.OpeningParenthesis -> {openParenthesisCount++; false}
                else -> false
            }
        }

    return this.subList(0, indexOfFirstOpenedParenthesis) + this.subList(indexOfFirstOpenedParenthesis + 1, indexOfMatchingClosedParenthesis).calculatePart2() + this.subList(indexOfMatchingClosedParenthesis + 1, this.size)
}