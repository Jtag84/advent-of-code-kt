package year2020.day19

import cc.ekblad.konbini.Parser
import cc.ekblad.konbini.ParserResult
import cc.ekblad.konbini.ParserState
import cc.ekblad.konbini.parse
import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 2) { (ruleParser, messages) ->
    countValidMessages(ruleParser, messages)
}

fun countValidMessages(ruleParser: Parser<String>,messages: List<String>) =
    messages
        .map { message -> ruleParser.parse(message) }
        .filterIsInstance<ParserResult.Ok<String>>()
        .filter { it.remainingInput.isEmpty() }
        .count()