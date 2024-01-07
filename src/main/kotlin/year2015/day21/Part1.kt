package year2015.day21

import com.google.common.collect.Sets
import commons.Part.Companion.part1

fun main() {
    part1.runAndPrint()
}

typealias Cost = Long
typealias Item = Triple<Cost, Damage, Armor>
val weapons: Set<Item> = setOf(
    Triple(8, 4, 0), // Dagger
    Triple(10, 5, 0), // Shortsword
    Triple(25, 6, 0), // Warhammer
    Triple(40, 7, 0), // Longsword
    Triple(74, 8, 0), // Greataxe
)

val armors: Set<Item> = setOf(
    Triple(0, 0, 0), // No Armor
    Triple(13, 0, 1), // Leather
    Triple(31, 0, 2), // Chainmail
    Triple(53, 0, 3), // Splintmail
    Triple(75, 0, 4), // Bandedmail
    Triple(102, 0, 5), // Platemail
)

val rings: Set<Item> = setOf(
    Triple(0, 0, 0), // no ring
    Triple(25, 1, 0), // Damage +1
    Triple(50, 2, 0), // Damage +2
    Triple(100, 3, 0), // Damage +3
    Triple(20, 0, 1), // Defense +1
    Triple(40, 0, 2), // Defense +2
    Triple(80, 0, 3), // Defense +3
)

val part1 = part1(inputParser, null) { bossStats ->
    Sets.cartesianProduct(weapons, armors, rings, rings).map { it.toSet() }.filter { isWinningAgainstBoss(bossStats, it) }.minOf { it.sumOf { it.first } }
}

fun isWinningAgainstBoss(bossStats: Stats, items: Set<Item>) : Boolean {
    val (bossHitPoint, bossDamage, bossArmor) = bossStats

    val (myHitpoint, myDamages, myArmor) = items
        .fold(Stats(100, 0, 0)) {(hitPoint, totalDamage, totalArmor), (_, damage, armor) ->
            Stats(hitPoint, totalDamage + damage, totalArmor + armor)}

    when {
        myDamages <= bossArmor -> return false
        bossDamage <= myArmor -> return true
    }

    val roundsToWinAgainstBoss = bossHitPoint / (myDamages - bossArmor)
    val roundsToLoseAgainstBoss = myHitpoint / (bossDamage - myArmor)
    return roundsToWinAgainstBoss <= roundsToLoseAgainstBoss
}