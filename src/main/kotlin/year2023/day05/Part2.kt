package year2023.day05

import Part.Companion.part2

fun main() {
    part2.runTest()
    part2.run()
}

val part2 = part2(almanacParser, 46L) { (seeds, maps) ->
    val seedRanges = seeds.chunked(2).map { LongRange(it[0], it[0] + it[1] - 1) }

    getLocationRanges(seedRanges, maps)
        .minOfOrNull { it.first() }!!
}

fun getLocationRanges(
    seeds: List<LongRange>,
    maps: Map<String, AlmanacMap>
) = seeds
    .asSequence()
    .flatMap { maps["seed-to-soil"]!!.convert(it) }
    .flatMap { maps["soil-to-fertilizer"]!!.convert(it) }
    .flatMap { maps["fertilizer-to-water"]!!.convert(it) }
    .flatMap { maps["water-to-light"]!!.convert(it) }
    .flatMap { maps["light-to-temperature"]!!.convert(it) }
    .flatMap { maps["temperature-to-humidity"]!!.convert(it) }
    .flatMap { maps["humidity-to-location"]!!.convert(it) }

