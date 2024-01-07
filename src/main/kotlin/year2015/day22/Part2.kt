package year2015.day22

import arrow.core.tail
import commons.Part.Companion.part2
import commons.repeat
import year2015.day21.Damage

fun main() {
    part2.runAndPrint()
}

val part2 = part2(inputParser, null) {(bossHitpoints, bossDamages, _) ->
    val wizardState = WizardState(50, 500, 0)
    val spellsCasted = Spell.entries.map { it to 0L }.toMap()
    findMinimumManaSpent(bossDamages, GameState(wizardState, bossHitpoints), spellsCasted, Long.MAX_VALUE, 1)
}