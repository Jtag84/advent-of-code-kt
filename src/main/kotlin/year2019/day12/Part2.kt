package year2019.day12

import arrow.core.andThen
import commons.Part.Companion.part2
import commons.*
import org.apache.commons.math3.primes.Primes
import org.apache.commons.math3.util.MathUtils

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 4686774924L) { moonPositions ->
    val motionSteps = generateSequence(moonPositions, ::applyGravity.andThen(::caculateNewPosition))
        .withIndex()
        .drop(1)

    val xs = moonPositions.map { it.first.x to it.second.x }.toSet()
    val ys = moonPositions.map { it.first.y to it.second.y }.toSet()
    val zs = moonPositions.map { it.first.z to it.second.z }.toSet()

    val xRepeating = motionSteps.first{ step -> step.value.map { it.first.x to it.second.x }.toSet() == xs}.index
    val yRepeating = motionSteps.first{ step -> step.value.map { it.first.y to it.second.y }.toSet() == ys}.index
    val zRepeating = motionSteps.first{ step -> step.value.map { it.first.z to it.second.z }.toSet() == zs}.index

    listOf(xRepeating.toLong(), yRepeating.toLong(), zRepeating.toLong())
        .reduce{a, b -> lcm(a, b) }
}
