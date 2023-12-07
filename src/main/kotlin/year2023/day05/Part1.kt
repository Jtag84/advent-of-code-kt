package year2023.day05

import Part

fun main() {
    part1.runTest(35L)
    part1.run()
}

val part1 = Part.part1(almanacParser) { (seeds, maps) ->
    getLocations(seeds, maps).min()
}

fun getLocations(
    seeds: List<Long>,
    maps: Map<String, AlmanacMap>
) = seeds
    .asSequence()
    .map { maps["seed-to-soil"]!!.convert(it) }
    .map { maps["soil-to-fertilizer"]!!.convert(it) }
    .map { maps["fertilizer-to-water"]!!.convert(it) }
    .map { maps["water-to-light"]!!.convert(it) }
    .map { maps["light-to-temperature"]!!.convert(it) }
    .map { maps["temperature-to-humidity"]!!.convert(it) }
    .map { maps["humidity-to-location"]!!.convert(it) }
