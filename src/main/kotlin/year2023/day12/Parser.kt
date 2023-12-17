package year2023.day12

import cc.ekblad.konbini.*
import commons.EnumParser
import commons.enumParser

typealias GroupSize = Int
typealias GroupSizes = List<GroupSize>
typealias SpringConditions = List<SpringCondition>

typealias Row = Pair<SpringConditions, GroupSizes>

val inputParser: Parser<List<Row>> = parser {
    chain1(rowParser, whitespace).terms
}

enum class SpringCondition(override val parsingString: String) : EnumParser {
    OPERATIONAL("."),
    DAMAGED("#"),
    UNKNOWN("?");
}

val springConditionsParser = parser {
    many1(enumParser<SpringCondition>())
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