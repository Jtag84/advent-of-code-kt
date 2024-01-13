package year2020.day18

import cc.ekblad.konbini.*
import commons.Lines
import commons.newLine
import commons.parseLines

val inputParser: Parser<List<List<Operation>>> = parser {
    chain1(operationLineParser, newLine).terms
}

val operationLineParser = parser {
    chain(parser{ oneOf(numberParser, operationParser) }, parser { regex("[ ]{0,1}") }).terms
}

sealed class Operation {
    data class Number(val number: Long): Operation()
    data object Plus : Operation()
    data object Multiply: Operation()
    data object OpeningParenthesis : Operation()
    data object ClosingParenthesis : Operation()
}

val operationParser = parser {
    val currentChar = regex("[+*()]{1}")
    when(currentChar) {
        "+" -> Operation.Plus
        "*" -> Operation.Multiply
        "(" -> Operation.OpeningParenthesis
        ")" -> Operation.ClosingParenthesis
        else -> throw IllegalStateException()
    }
}

val numberParser = integer.map { Operation.Number(it) }
