package year2019.day22

import commons.Part.Companion.part2
import java.math.BigInteger

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}
// https://en.wikipedia.org/wiki/Linear_congruential_generator
// this is a linear congruential function, so composing it on itself k times can be done quickly
// 𝑓(𝑥)=𝑎𝑥+𝑏  mod 𝑚
val part2 = part2(inputParser, 9L.toBigInteger()) { techniques ->
    val size = if(isTest) {10L.toBigInteger()} else {119_315_717_514_047L.toBigInteger()}

    val indexFunction = techniques
        .fold(CardShuffleLCF(BigInteger.ONE, BigInteger.ZERO, size))
        { cardShuffleLCF, shuffleTechnique -> cardShuffleLCF.compose(shuffleTechnique.toCardShuffleLCF(size))}

    val ktimesIndexFunction = pow_compose(indexFunction, 101_741_582_076_661L.toBigInteger())
    ktimesIndexFunction.apply(2020L.toBigInteger())
}

// Ax+B
typealias A = BigInteger
typealias B = BigInteger
typealias Modulus = BigInteger
// linear congruential function (Ax+B) mod m
typealias CardShuffleLCF = Triple<A, B, Modulus>

// https://codeforces.com/blog/entry/72527
// https://codeforces.com/blog/entry/72593
// 𝑔(𝑓(𝑥)) = (𝑐(𝑎𝑥+𝑏)+𝑑)  mod 𝑚 =(𝑎𝑐𝑥+𝑏𝑐+𝑑)  mod 𝑚  -> (𝑎𝑐 mod 𝑚,𝑏𝑐+𝑑  mod 𝑚)
fun CardShuffleLCF.compose(cardShuffleLCF: CardShuffleLCF) :CardShuffleLCF {
    val (a, b, modulus) = cardShuffleLCF
    val (c, d, _) = this

    val newA = a.multiply(c).mod(modulus)
    val newB = b.multiply(c).plus(d).mod(modulus)
    return CardShuffleLCF(newA, newB, modulus)
}

fun CardShuffleLCF.apply(index: BigInteger) :BigInteger {
    val (a, b, modulus) = this
    return a.multiply(index).plus(b).remainder(modulus)
}

fun pow_compose(functionToCompose: CardShuffleLCF, kTimes: BigInteger) : CardShuffleLCF {
    var g = CardShuffleLCF(BigInteger.ONE, BigInteger.ZERO, functionToCompose.third)
    var k = kTimes
    var f = functionToCompose
    while (k > BigInteger.ZERO) {
        if(k.remainder(2L.toBigInteger()) == BigInteger.ONE) {
            g = g.compose(f)
        }
        k = k.divide(2L.toBigInteger())
        f = f.compose(f)
    }
    return g
}

fun CardShuffleTechnique.toCardShuffleLCF(cardDeckSize: BigInteger) : CardShuffleLCF {
    return when(this){
        is CardShuffleTechnique.CutNCards -> {
            val effectiveCut = if(this.n < 0) {cardDeckSize + this.n.toBigInteger()} else {this.n.toBigInteger()}
            CardShuffleLCF(BigInteger.ONE, effectiveCut, cardDeckSize)
        }
        CardShuffleTechnique.DealIntoNewStack -> {CardShuffleLCF((-1).toBigInteger(), (cardDeckSize - BigInteger.ONE), cardDeckSize) }
        is CardShuffleTechnique.DealWithIncrementN -> {
            val effectiveModulus = this.n.toBigInteger().modInverse(cardDeckSize)
            return CardShuffleLCF(effectiveModulus, BigInteger.ZERO, cardDeckSize)
        }
    }
}