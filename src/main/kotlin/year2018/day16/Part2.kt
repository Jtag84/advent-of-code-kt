package year2018.day16

import commons.Part.Companion.part2

fun main() {
    part2.runAndPrint()
}

val part2 = part2(inputParser, null) { (samples, program) ->
    val opcodePossibleInstructionMap = samples
        .map { it.second.first() to findOpcodes(it) }
        .groupBy ({ it.first }, {it.second}).mapValues { it.value.flatten().toSet() }

    val opcodeToInstructionMap = mutableMapOf<Long, InstructionEnum>()

    while(opcodeToInstructionMap.size < 16) {
        opcodePossibleInstructionMap
            .filterNot { it.key in opcodeToInstructionMap.keys }
            .map { it.key to (it.value - opcodeToInstructionMap.values.toSet())}
            .filter { it.second.size == 1 }
            .forEach { (opcode, instruction) -> opcodeToInstructionMap.put(opcode, instruction.first()) }
    }

    val initialRegisters: Registers = listOf(0L,0L,0L,0L)
    program
        .fold(initialRegisters)
        { registers: Registers, instruction ->
            val (opcode, a, b, c) = instruction
            opcodeToInstructionMap[opcode]!!.execute(registers, a, b, c) }
        .first()
}