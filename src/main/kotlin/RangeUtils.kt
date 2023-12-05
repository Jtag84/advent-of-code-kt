fun LongRange.splitByIntersection(splitter: LongRange): Pair<LongRange?, List<LongRange>> {
    if (this.last < splitter.first || splitter.last < this.first) {
        return Pair(null, listOf(this))
    }

    val intersection = LongRange(maxOf(this.first, splitter.first), minOf(this.last, splitter.last))
    val remainingRanges = mutableListOf<LongRange>()

    if (this.first < intersection.first) {
        remainingRanges.add(this.first until intersection.first)
    }

    if (intersection.last < this.last) {
        remainingRanges.add((intersection.last + 1)..this.last)
    }

    return Pair(intersection, remainingRanges.toList())
}
