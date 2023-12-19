package year2023.day19

import commons.Part.Companion.part2
import commons.splitByIntersection

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputPart2Parser, 167409079868000L) { workflows ->
    val initialRule = workflows["in"]!!
    val fullRange = 1..4000L
    getRatingRange(
        workflows,
        initialRule,
        RatingRange(fullRange, fullRange, fullRange, fullRange)
    )
        .sumOf { (x, m, a, s) -> (x.last - x.first + 1) * (m.last - m.first + 1) * (a.last - a.first + 1) * (s.last - s.first + 1) }
}

fun getRatingRange(workflows: Map<String, Rule>, rule: Rule, currentRatingRange: RatingRange): List<RatingRange> {
    return when (rule) {
        Rule.Accepted -> listOf(currentRatingRange)
        Rule.Rejected -> emptyList()
        is Rule.NextWorkflow -> {
            val nextRule = workflows[rule.workflowName]!!
            getRatingRange(workflows, nextRule, currentRatingRange)
        }

        is Rule.RuleConditionPart2 -> {
            val (conditionX, conditionM, conditionA, conditionS) = rule.ratingRange
            val (x, m, a, s) = currentRatingRange
            val newXRange = x.splitByIntersection(conditionX)
            val newMRange = m.splitByIntersection(conditionM)
            val newARange = a.splitByIntersection(conditionA)
            val newSRange = s.splitByIntersection(conditionS)
            if (newXRange.first == null || newMRange.first == null || newARange.first == null || newSRange.first == null) {
                getRatingRange(workflows, rule.ifFalseRule, currentRatingRange)
            } else {
                getRatingRange(
                    workflows,
                    rule.ifTrueRule,
                    RatingRange(newXRange.first!!, newMRange.first!!, newARange.first!!, newSRange.first!!)
                ) + getRatingRange(
                    workflows,
                    rule.ifFalseRule,
                    RatingRange(
                        getFalseRangeOrTrueRange(newXRange),
                        getFalseRangeOrTrueRange(newMRange),
                        getFalseRangeOrTrueRange(newARange),
                        getFalseRangeOrTrueRange(newSRange)
                    )
                )
            }
        }

        is Rule.RuleCondition -> throw IllegalStateException()
    }
}

fun getFalseRangeOrTrueRange(intersectionRest: Pair<LongRange?, List<LongRange>>): LongRange {
    if (intersectionRest.second.isEmpty()) {
        return intersectionRest.first!!
    } else {
        check(intersectionRest.second.size == 1)
        return intersectionRest.second.first()
    }
}