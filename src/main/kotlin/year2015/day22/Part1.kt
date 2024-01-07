package year2015.day22

import arrow.core.Tuple4
import arrow.core.tail
import commons.Part.Companion.part1
import commons.repeat
import year2015.day21.Armor
import year2015.day21.Damage
import year2015.day21.HitPoint

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

typealias Mana = Long
typealias ManaSpent = Long
typealias WizardState = Triple<HitPoint,Mana, ManaSpent>
typealias BossState = HitPoint
typealias GameState = Pair<WizardState, BossState>

enum class Spell(val manaCost: Mana, val turns: Long, val damage: Damage, val armor: Armor, val healingPoint: HitPoint, val manaRegeneration: Mana) {
    MISSILE (53,    1, 4, 0, 0, 0),
    DRAIN   (73,    1, 2, 0, 2, 0),
    SHIELD  (113,   6, 0, 7, 0, 0),
    POISON  (173,   6, 3, 0, 0, 0),
    RECHARGE(229,    5, 0, 0, 0, 101)
}

val part1 = part1(inputParser, 641L) { (bossHitpoints, bossDamages, _) ->
    val spellsCasted = Spell.entries.map { it to 0L }.toMap()
    val wizardState = if(isTest) {
            WizardState(10, 250, 0)
        }
        else {
            WizardState(50, 500, 0)
        }
    findMinimumManaSpent(bossDamages, GameState(wizardState, bossHitpoints), spellsCasted, Long.MAX_VALUE)
}

fun findMinimumManaSpent (bossDamage: Damage, gameState: GameState, currentSpellsCasted:Map<Spell, Long>, currentMinimumManaSpent: Long, playerTurnPenalty: Int = 0) : ManaSpent {
    if(gameState.first.first <= playerTurnPenalty || gameState.first.third >= currentMinimumManaSpent) {
        return currentMinimumManaSpent
    }

    val (wizardState, bossState) = gameState
    val wizardRoundSpell = currentSpellsCasted.entries.filter { it.value > 0 }.map { it.key }.toSet()

    val validSpellsToCast = Spell.entries.toMutableSet()
        .let {
            it.removeAll(wizardRoundSpell)
            it.filter { it.manaCost <= wizardState.second }.toSet()
        }

    if(validSpellsToCast.isEmpty()) {
        return currentMinimumManaSpent
    }

    val (damage, _, healingPoints, manaRegeneration) = wizardRoundSpell.aggregate()

    var newMimimumManaSpent = currentMinimumManaSpent
    validSpellsToCast.forEach {
        val newBossState = bossState - damage - it.damage
        var newHitPoints = wizardState.first - playerTurnPenalty + it.healingPoint + healingPoints
        var newMana = wizardState.second + manaRegeneration + it.manaRegeneration - it.manaCost
        var newManaSpent = wizardState.third + it.manaCost

        val wizardRoundSpellCasted = currentSpellsCasted.mapValues{spellEntry ->
            if(spellEntry.key == it) {
                it.turns - 1
            }
            else {
                spellEntry.value - 1
            }
        }

        val (bossRoundDamage, bossRoundArmor, bossRoundHealingPoints, bossRoundManaRegeneration) = wizardRoundSpellCasted.entries.filter { it.value > 0 }.map { it.key }.toSet().aggregate()

        if(newBossState <= 0 && newManaSpent < newMimimumManaSpent) {
            newMimimumManaSpent = newManaSpent
        }
        else {
            val currentDamage = if(bossDamage - bossRoundArmor < 1) {1} else {bossDamage - bossRoundArmor}
            val newWizardHitPoints = newHitPoints + bossRoundHealingPoints - currentDamage

            val bossRoundSpellCasted = wizardRoundSpellCasted.mapValues{spellEntry -> spellEntry.value - 1}

            val minManaSpent = findMinimumManaSpent(bossDamage, GameState(WizardState(newWizardHitPoints, newMana + bossRoundManaRegeneration, newManaSpent), newBossState - bossRoundDamage), bossRoundSpellCasted, newMimimumManaSpent, playerTurnPenalty)
            if(minManaSpent < newMimimumManaSpent) {
                newMimimumManaSpent = minManaSpent
            }
        }
    }

    return newMimimumManaSpent
}

fun Set<Spell>.aggregate(): Tuple4<Long, Long, Long, Long> {
    return this.fold(Tuple4(0L,0L,0L, 0L)) {(damage, armor, healingPoints, manaRegeneration), spell ->
        Tuple4(damage + spell.damage, armor + spell.armor, healingPoints + spell.healingPoint, manaRegeneration + spell.manaRegeneration)
    }
}