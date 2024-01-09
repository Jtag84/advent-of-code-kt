package year2020.day14

import commons.Part.Companion.part2
import commons.combinations
import commons.println

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 208L) { instructions ->
    val startMemory = mutableMapOf<Long, Long>()
    val startMask = Instruction.Mask(Long.MAX_VALUE, 0)

    instructions.fold(startMask to startMemory){ (currentMask, currentMemory), instruction ->
        when(instruction) {
            is Instruction.Mask -> instruction to currentMemory
            is Instruction.Mem -> {
                val address = instruction.address.and(currentMask.floatingBitMask).or(currentMask.orMask)
                (currentMask.floatingBits.toList().combinations()
                    .map { bits -> bits.sumOf { Math.pow(2.0, it.toDouble()).toLong() } } + listOf(0L))
                    .map { it + address }
                    .forEach {
                        currentMemory[it] = instruction.value
                    }
                currentMask to currentMemory
            }
        }
    }
        .second.values.sum()
}
