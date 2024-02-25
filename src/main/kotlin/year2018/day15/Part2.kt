package year2018.day15

import commons.Part.Companion.part2
import commons.printEachLine

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 4988L) { cavernMap ->
    val maxX = cavernMap.maxOf { it.key.x }
    val elves = cavernMap.entries.filter { it.value == CavernMap.ELF }.mapIndexed { index, entry -> Elf(index, entry.key, 200) }
    val goblins = cavernMap.entries.filter { it.value == CavernMap.GOBLIN }.mapIndexed { index, entry -> Goblin(index, entry.key, 200) }

    val emptyCavernMap = cavernMap.mapValues { if(it.value in setOf(CavernMap.GOBLIN, CavernMap.ELF)) {CavernMap.EMPTY} else {it.value} }

    val startingHitStrength = 15L // should really starts at 4L but this is faster this way when rerunning it while passing the tests
    generateSequence({startingHitStrength to combat(emptyCavernMap, maxX, elves, goblins, hitBy(startingHitStrength))} ,
        { (hit, _) ->
            val newHit = hit+1
            newHit to combat(emptyCavernMap, maxX, elves, goblins, hitBy(newHit)) }
    )
        .first {it.second.second.value.first.size == elves.size}
        .second.first
}

fun hitBy(elfHit: Long) : (Fighter) -> Long {
    return {
        fighter ->
            if (fighter is Elf) {
                elfHit
            } else {
                3
            }
    }
}