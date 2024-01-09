package year2020.day14

import cc.ekblad.konbini.*
import commons.Lines
import commons.parseLines

val inputParser: Parser<List<Instruction>> = parser {
    chain1(instructionParser, whitespace).terms
}

sealed class Instruction {
    data class Mask(val andMask: Long, val orMask: Long, val floatingBits: Set<Long> = emptySet(), val floatingBitMask: Long = Long.MAX_VALUE) : Instruction()
    data class Mem(val address: Long, val value: Long) : Instruction()
}

val maskParser = parser {
    string("mask = ")
    val mask = regex("[01X]{36}").reversed()
    val andMask = mask.withIndex().filter { it.value == '0' }.sumOf { Math.pow(2.0, it.index.toDouble()) }.toLong().xor(Long.MAX_VALUE)
    val orMask = mask.withIndex().filter { it.value == '1' }.sumOf { Math.pow(2.0, it.index.toDouble()) }.toLong()
    val floatingBits = mask.withIndex().filter { it.value == 'X' }.map { it.index.toLong() }.toSet()
    val floatingBitMask = floatingBits.sumOf { Math.pow(2.0, it.toDouble()) }.toLong().xor(Long.MAX_VALUE)
    Instruction.Mask(andMask, orMask, floatingBits, floatingBitMask)
}

val memParser = parser {
    string("mem[")
    val address = integer()
    string("] = ")
    val value = integer()

    Instruction.Mem(address, value)
}

val instructionParser = parser { oneOf(maskParser, memParser) }