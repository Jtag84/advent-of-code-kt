package year2015.day23

import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 2L) { instructions ->
    instructions.execute(ProgramState(Registers(0, 0), 0)).first.second
}

typealias InstructionPointer = Long
typealias ProgramState = Pair<Registers, InstructionPointer>

tailrec fun List<Instruction>.execute(programState: ProgramState) : ProgramState{
    val (_, instructionPointer) = programState

    if(instructionPointer >= this.size) {
        return programState
    }

    return this.execute(this[instructionPointer.toInt()].execute(programState))
}

fun Instruction.execute(programState: ProgramState) : ProgramState {
    val (registers, instructionPointer) = programState
    return when(this) {
        is Instruction.Half -> registers.map(register) {a -> a/2} to instructionPointer + 1
        is Instruction.Increment -> registers.map(register) {a -> a + 1} to instructionPointer + 1
        is Instruction.Jump -> registers to (instructionPointer + offset)
        is Instruction.JumpIfEven -> {
                if(registers.getRegisterValue(register) % 2 == 0L) {
                    registers to instructionPointer + offset
                } else {
                    registers to instructionPointer + 1
                }
            }
        is Instruction.JumpIfOne -> {
            if(registers.getRegisterValue(register) == 1L) {
                registers to instructionPointer + offset
            } else {
                registers to instructionPointer + 1
            }
        }
        is Instruction.Triple -> registers.map(register) {a -> a * 3} to instructionPointer + 1
    }
}

fun Registers.map(register: Register, mappingFunction: (Long) -> Long) : Registers {
    return this.setRegisterValue(register, mappingFunction(this.getRegisterValue(register)))
}

fun Registers.getRegisterValue(register: Register) : Long {
    return when(register) {
        Register.A -> first
        Register.B -> second
    }
}

fun Registers.setRegisterValue(register: Register, value: Long) : Registers{
    return when(register) {
        Register.A -> Registers(value, this.second)
        Register.B -> Registers(this.first, value)
    }
}