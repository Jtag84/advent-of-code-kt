package year2023.day05

import cc.ekblad.konbini.*
import newLine
import newLines
import splitByIntersection

data class AlmanacMap(val rangeToOffsets: List<Pair<LongRange, Long>>) {
    fun convert(value: Long): Long {
        val offset = rangeToOffsets
            .firstOrNull { it.first.contains(value) }
            ?.second
            ?: 0
        return value + offset
    }

    private fun convert(from: LongRange, with: Pair<LongRange, Long>): Pair<LongRange?, List<LongRange>> {
        val rangeSplit = from.splitByIntersection(with.first)
        val convertedRange = rangeSplit.first?.let {
            val offset = with.second
            LongRange(it.first + offset, it.last + offset)
        }
        return Pair(convertedRange, rangeSplit.second)
    }

    fun convert(range: LongRange): List<LongRange> {
        val resultPair = rangeToOffsets.fold(Pair(listOf(range), emptyList<LongRange>())) { acc, rangeToOffset ->
            val convertedRangeSplits = acc.first.asSequence()
                .map { convert(it, rangeToOffset) }
                .toList()

            val nonConvertedRanges = convertedRangeSplits.filter { it.first == null }.flatMap { it.second }
            val convertedRangePairs = convertedRangeSplits.filter { it.first != null }
            val convertedRanges = convertedRangePairs.mapNotNull { it.first }
            val outsideConversionRanges = convertedRangePairs.flatMap { it.second }

            Pair(nonConvertedRanges + outsideConversionRanges, acc.second + convertedRanges)
        }
        return resultPair.first + resultPair.second
    }
}

val seedParser = parser {
    string("seeds: ")
    chain1(integer, whitespace1).terms
}

val rangeParser = parser {
    whitespace()
    val destinationRange = integer()
    whitespace1()
    val sourceRange = integer()
    whitespace1()
    val lengthRange = integer()
    Pair(LongRange(sourceRange, sourceRange + lengthRange - 1), destinationRange - sourceRange)
}

val almanacMapParser = parser {
    val mapName = regex("[a-z-]+")
    string(" map:\n")
    val rangesToOffsets = chain1(rangeParser, newLine)
    Pair(mapName, AlmanacMap(rangesToOffsets.terms))
}

val almanacParser = parser {
    val seeds = seedParser()
    newLines()
    val maps = chain1(almanacMapParser, newLines)
    Pair(seeds, maps.terms.toMap())
}