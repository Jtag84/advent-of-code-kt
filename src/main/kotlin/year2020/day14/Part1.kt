package year2020.day14

import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 165L) { instructions ->
    val startMemory = mutableMapOf<Long, Long>()
    val startMask = Instruction.Mask(Long.MAX_VALUE, 0)

    instructions.fold(startMask to startMemory){ (currentMask, currentMemory), instruction ->
            when(instruction) {
                is Instruction.Mask -> instruction to currentMemory
                is Instruction.Mem -> {
                        currentMemory[instruction.address] = instruction.value.and(currentMask.andMask).or(currentMask.orMask)
                        currentMask to currentMemory
                    }
            }
        }
        .second.values.sum()
}