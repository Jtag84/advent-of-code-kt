package year2018.day16

import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 1) { (samples, _) ->
    samples.map { findOpcodes(it) }.count { it.size >= 3 }
}

fun findOpcodes(sample: Sample) : Set<InstructionEnum> {
    val (beforeRegisters, instruction, afterRegisters) = sample
    val (_, a, b, c) = instruction
    return InstructionEnum.entries.filter {
        it.execute(beforeRegisters, a, b, c) == afterRegisters
    }.toSet()
}