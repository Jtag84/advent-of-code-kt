package year2015.day16

import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val identifiedSue = Sue(
    0,
    children = 3L,
    cats = 7L,
    samoyeds = 2L,
    pomeranians = 3L,
    akitas = 0L,
    vizslas = 0L,
    goldfish = 5L,
    trees = 3L,
    cars = 2L,
    perfumes = 1L,
)

val part1 = part1(inputParser, null) { sues ->
    sues.first {
        (it.children?.equals(identifiedSue.children) ?: true)
                && (it.cats?.equals(identifiedSue.cats) ?: true)
                && (it.samoyeds?.equals(identifiedSue.samoyeds) ?: true)
                && (it.pomeranians?.equals(identifiedSue.pomeranians) ?: true)
                && (it.akitas?.equals(identifiedSue.akitas) ?: true)
                && (it.vizslas?.equals(identifiedSue.vizslas) ?: true)
                && (it.goldfish?.equals(identifiedSue.goldfish) ?: true)
                && (it.trees?.equals(identifiedSue.trees) ?: true)
                && (it.cars?.equals(identifiedSue.cars) ?: true)
                && (it.perfumes?.equals(identifiedSue.perfumes) ?: true)
    }.id
}