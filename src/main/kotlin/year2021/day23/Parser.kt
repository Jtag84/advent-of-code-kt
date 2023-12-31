package year2021.day23

import cc.ekblad.konbini.*
import commons.*
import year2021.day23.BurrowMapElement.*

typealias BurrowMap = Map<Coordinates, BurrowMapElement>
typealias Amphipods = Set<Pair<Coordinates, BurrowMapElement>>

val extraLines =
    """
    |  #D#C#B#A#
    |  #D#B#A#C#
    """.trimMargin()

val inputParserPart2: Parser<Pair<BurrowMap, Amphipods>> = parser {
    val lines = parseLines()
    (lines.take(3) + extraLines + lines.drop(3)).joinToString("\n")
}.map { inputParser.parseOrThrowException(it) }

val inputParser: Parser<Pair<BurrowMap, Amphipods>> = parser {
    val map = coordinatesParser(enumParser<BurrowMapElement>())().toMap()
    val amphipods = map.entries.filter { it.value in amphipodTypes }.map { it.key to it.value }.toSet()
    val mapWithoutAmphipods = map.mapValues { if(it.value in amphipodTypes) {
            EMPTY_SPACE
        }
        else {
            it.value
        }
    }

    mapWithoutAmphipods to amphipods
}

val amphipodTypes = setOf(AMBER_AMPHIPOD, BRONZE_AMPHIPOD, COPPER_AMPHIPOD, DESERT_AMPHIPOD)

enum class BurrowMapElement(override val parsingString: String, private val cost : Int) : EnumParser {
    OUTSIDE(" ", -1),
    WALL("#", -1),
    EMPTY_SPACE(".", -1),
    AMBER_AMPHIPOD("A", 1),
    BRONZE_AMPHIPOD("B", 10),
    COPPER_AMPHIPOD("C", 100),
    DESERT_AMPHIPOD("D", 1000);

    fun getCost() : Int {
        check(cost > 0) { "can't use cost for $this" }

        return cost
    }
}
