package year2023.day19

import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 19114L) { (workflows, partRatings) ->
    val initialRule = workflows["in"]!!
    partRatings
        .filter { isAccepted(workflows, initialRule, it) }
        .sumOf { (x, m, a, s) -> x + m + a + s }
}

fun isAccepted(workflows: Map<String, Rule>, rule: Rule, partRating: Rating): Boolean {
    return when (rule) {
        Rule.Accepted -> true
        is Rule.NextWorkflow -> {
            val nextRule = workflows[rule.workflowName]!!
            isAccepted(workflows, nextRule, partRating)
        }

        Rule.Rejected -> false
        is Rule.RuleCondition -> if (rule.condition(partRating)) {
            isAccepted(workflows, rule.ifTrueRule, partRating)
        } else {
            isAccepted(workflows, rule.ifFalseRule, partRating)
        }

        is Rule.RuleConditionPart2 -> throw IllegalStateException()
    }
}