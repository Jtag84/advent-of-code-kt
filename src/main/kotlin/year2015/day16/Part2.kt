package year2015.day16

import commons.Part.Companion.part2

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, null) { sues ->
    sues.first { currentSue ->
        (currentSue.children?.equals(identifiedSue.children) ?: true)
                && (currentSue.cats?.let { it > identifiedSue.cats!! } ?: true)
                && (currentSue.samoyeds?.equals(identifiedSue.samoyeds) ?: true)
                && (currentSue.pomeranians?.let { it < identifiedSue.pomeranians!! } ?: true)
                && (currentSue.akitas?.equals(identifiedSue.akitas) ?: true)
                && (currentSue.vizslas?.equals(identifiedSue.vizslas) ?: true)
                && (currentSue.goldfish?.let { it < identifiedSue.goldfish!! } ?: true)
                && (currentSue.trees?.let { it > identifiedSue.trees!! } ?: true)
                && (currentSue.cars?.equals(identifiedSue.cars) ?: true)
                && (currentSue.perfumes?.equals(identifiedSue.perfumes) ?: true)
    }.id
}