package year2019.day22

import commons.Part.Companion.part1
import commons.*
import java.math.BigInteger

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 8) { techniques ->
    val size = if(isTest) {10L.toBigInteger()} else {10_007L.toBigInteger()}

    val valueToFind = if(isTest) {3L} else {2019L}

    val indexFunction = getIndexFunction(techniques, size)

    generateSequence(BigInteger.ZERO) {it + BigInteger.ONE}
        .map { indexFunction.apply(it) }
        .withIndex()
        .first{it.value == valueToFind.toBigInteger()}
        .index
}

fun getIndexFunction(
    techniques: List<CardShuffleTechnique>,
    size: BigInteger
) = techniques
    .fold(CardShuffleLCF(BigInteger.ONE, BigInteger.ZERO, size))
    { cardShuffleLCF, shuffleTechnique -> cardShuffleLCF.compose(shuffleTechnique.toCardShuffleLCF(size)) }