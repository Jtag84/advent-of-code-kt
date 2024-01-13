package year2020.day19

import cc.ekblad.konbini.*
import commons.newLine
import commons.parseLines

val inputParser: Parser<Pair<Parser<String>, List<String>>> = parser {
    val ruleMap = chain1(ruleParser, newLine).terms.toMap()
    whitespace()
    val messages = parseLines()

    getRuleParsers(ruleMap)["0"]!! to messages
}

val inputParserPart2: Parser<Pair<ParserState.() -> String, List<String>>> = parser {
    val ruleMap = chain1(ruleParser, newLine).terms.toMap()
    whitespace()
    val messages = parseLines()

    val ruleParsers = getRuleParsers(ruleMap).toMutableMap()

    val ruleParser = parser {
            val sequence1 = ruleParsers["42"]!!
            val sequence2 = ruleParsers["31"]!!
            val parsedSequence1 = many1(sequence1)
            val parsedSequence2 = many1(sequence2)
            if(parsedSequence2.size >= parsedSequence1.size ) {
                fail("2nd sequnce should be of a smaller size")
            }
            (parsedSequence1 + parsedSequence2).joinToString("")
        }

    ruleParser to messages
}

private fun getRuleParsers(ruleMap: Map<String, List<List<String>>>): MutableMap<String, Parser<String>> {
    val builtParsers = mutableMapOf<String, Parser<String>>()

    while (ruleMap.keys != builtParsers.keys) {
        val missingRuleParsers = ruleMap.keys - builtParsers.keys
        missingRuleParsers.forEach { ruleName ->
            val rule = ruleMap[ruleName]!!
            when {
                rule.size == 1 && rule[0].size == 1 && (rule[0][0] == "a" || rule[0][0] == "b") -> builtParsers[ruleName] =
                    parser {
                        string(rule[0][0])
                        rule[0][0]
                    }

                rule.all { sequence -> sequence.all { builtParsers.contains(it) } } -> {
                    builtParsers[ruleName] = parser {
                        oneOf(*rule.map { sequence -> parser { sequence.joinToString("") { builtParsers[it]!!() } } }
                            .toTypedArray())
                    }
                }
            }
        }
    }
    return builtParsers
}

val ruleNameParser = parser { regex("[0-9]+") }

val ruleSequenceParser = parser { chain(ruleNameParser, parser { char(' ') }).terms }
val ruleSequencesParser = parser { chain(ruleSequenceParser, parser { string(" | ") }).terms }

val ruleParser = parser {
    val ruleName = ruleNameParser()
    string(": ")
    val rule = oneOf(doubleQuotedString.map { listOf(listOf(it)) }, ruleSequencesParser)
    ruleName to rule
}