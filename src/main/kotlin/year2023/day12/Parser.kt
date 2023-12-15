package year2023.day12

import cc.ekblad.konbini.*

typealias GroupSize = Int
typealias GroupSizes = List<GroupSize>
typealias SpringConditions = List<SpringCondition>

typealias Row = Pair<SpringConditions, GroupSizes>

val inputParser: Parser<List<Row>> = parser {
    chain1(rowParser, whitespace).terms
}

enum class SpringCondition(private val char: Char) {
    OPERATIONAL('.'),
    DAMAGED('#'),
    UNKNOWN('?');

    fun getParser() = parser { char(char) }.map { this }
}

val springConditionParser = parser {
    oneOf(*SpringCondition.entries.map { it.getParser() }.toTypedArray())
}
val springConditionsParser = parser {
    many1(springConditionParser)
}

val groupSizesParser = parser {
    chain1(integer, parser { char(',') }).terms.map { it.toInt() }
}

val rowParser = parser {
    val springConditions = springConditionsParser()
    whitespace1()
    val groupSizes = groupSizesParser()
    Row(springConditions, groupSizes)
}