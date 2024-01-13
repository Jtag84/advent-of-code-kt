package year2020.day19

import cc.ekblad.konbini.ParserResult
import cc.ekblad.konbini.parse
import commons.Part.Companion.part2

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParserPart2, 12) { (ruleParser, messages) ->
    countValidMessages(ruleParser, messages)
}