package year2018.day15

import commons.Part.Companion.part2
import commons.printEachLine

fun main() {
//    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 4988L) { cavernMap ->
    val maxX = cavernMap.maxOf { it.key.x }
    val elves = cavernMap.entries.filter { it.value == CavernMap.ELF }.mapIndexed { index, entry -> Elf(index, entry.key, 200) }
    val goblins = cavernMap.entries.filter { it.value == CavernMap.GOBLIN }.mapIndexed { index, entry -> Goblin(index, entry.key, 200) }

    generateSequence({4L to combat(cavernMap, maxX, elves, goblins, hitBy(4L))} ,
        { (hit, _) ->
            val newHit = hit+1
            newHit to combat(cavernMap, maxX, elves, goblins, hitBy(newHit)) }
    )
//        .first {it.second.second.value.first.size == elves.size}
//        .second.first
//        .take(15).map { it.first to it.second.first }.toList().printEachLine()
        .take(16).toList().printEachLine()
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