package year2023.day19

import arrow.core.Tuple4
import arrow.core.andThen
import arrow.core.partially2
import cc.ekblad.konbini.*

val inputParser = parser {
    val workflows = workflowsParser()
    whitespace1()
    workflows to chain1(ratingParser, whitespace).terms
}

typealias X = Long
typealias M = Long
typealias A = Long
typealias S = Long
typealias Rating = Tuple4<X, M, A, S>
typealias WorkflowName = String
typealias Condition = (Rating) -> Boolean
typealias RatingRange = Tuple4<LongRange, LongRange, LongRange, LongRange>

sealed class Rule {
    data object Accepted : Rule()
    data object Rejected : Rule()
    data class RuleCondition(val condition: Condition, val ifTrueRule: Rule, val ifFalseRule: Rule) : Rule()
    data class NextWorkflow(val workflowName: WorkflowName) : Rule()
    data class RuleConditionPart2(val ratingRange: RatingRange, val ifTrueRule: Rule, val ifFalseRule: Rule) : Rule()
}

val acceptedParser: Parser<Rule> = parser { char('A') }.map { Rule.Accepted }
val rejectedParser: Parser<Rule> = parser { char('R') }.map { Rule.Rejected }
val workflowNameParser = parser { regex("[a-z]{2,3}") }
val nextWorkflowParser: Parser<Rule> = workflowNameParser.map { Rule.NextWorkflow(it) }

fun ruleConditionParser(): Parser<Rule> = parser {
    val getPartRating: (Rating) -> Long = when (char()) {
        'x' -> Rating::first
        'm' -> Rating::second
        'a' -> Rating::third
        's' -> Rating::fourth
        else -> throw IllegalStateException()
    }
    val comparisonFunction: (Long, Long) -> Boolean = when (char()) {
        '>' -> { a, b -> a > b }
        '<' -> { a, b -> a < b }
        else -> throw IllegalStateException()
    }
    val valueComparison = integer()
    val compareToValue: (Long) -> Boolean = comparisonFunction.partially2(valueComparison)
    val ruleCondition = getPartRating andThen compareToValue

    char(':')
    val ifTrueRule: Rule = oneOf(acceptedParser, rejectedParser, nextWorkflowParser, ruleConditionParser())
    char(',')
    val ifFalseRule: Rule = oneOf(acceptedParser, rejectedParser, nextWorkflowParser, ruleConditionParser())

    Rule.RuleCondition(ruleCondition, ifTrueRule, ifFalseRule)
}

val workflowParser = parser {
    val name = workflowNameParser()
    char('{')
    val rules = ruleConditionParser()()
    char('}')
    name to rules
}

val workflowsParser = parser {
    chain1(workflowParser, whitespace).terms.toMap()
}

val ratingParser = parser {
    string("{x=")
    val xRating = integer()
    string(",m=")
    val mRating = integer()
    string(",a=")
    val aRating = integer()
    string(",s=")
    val sRating = integer()
    char('}')
    Rating(xRating, mRating, aRating, sRating)
}

fun part2RuleConditionParser(): Parser<Rule> = parser {
    val part = char()
    val comparisonOperator = char()
    val valueComparison = integer()

    val rangeCondition = when (comparisonOperator) {
        '<' -> LongRange(1, valueComparison - 1)
        '>' -> LongRange(valueComparison + 1, 4000)
        else -> throw IllegalStateException()
    }

    val fullRange = LongRange(1, 4000)

    val ratingRange = when (part) {
        'x' -> RatingRange(rangeCondition, fullRange, fullRange, fullRange)
        'm' -> RatingRange(fullRange, rangeCondition, fullRange, fullRange)
        'a' -> RatingRange(fullRange, fullRange, rangeCondition, fullRange)
        's' -> RatingRange(fullRange, fullRange, fullRange, rangeCondition)
        else -> throw IllegalStateException()
    }

    char(':')
    val ifTrueRule: Rule = oneOf(acceptedParser, rejectedParser, nextWorkflowParser, part2RuleConditionParser())
    char(',')
    val ifFalseRule: Rule = oneOf(acceptedParser, rejectedParser, nextWorkflowParser, part2RuleConditionParser())

    Rule.RuleConditionPart2(ratingRange, ifTrueRule, ifFalseRule)
}

val workflowPart2Parser = parser {
    val name = workflowNameParser()
    char('{')
    val rules = part2RuleConditionParser()()
    char('}')
    name to rules
}

val inputPart2Parser = parser {
    chain1(workflowPart2Parser, whitespace1).terms.toMap()
}