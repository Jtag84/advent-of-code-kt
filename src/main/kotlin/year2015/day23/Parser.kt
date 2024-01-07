package year2015.day23

import cc.ekblad.konbini.*
import commons.Lines
import commons.parseLines

typealias RegisterA = Long
typealias RegisterB = Long
typealias Registers = Pair<RegisterA, RegisterB>

enum class Register {
    A, B
}

sealed class Instruction {
    data class Half(val register: Register) : Instruction()
    data class Triple(val register: Register) : Instruction()
    data class Increment(val register: Register) : Instruction()
    data class Jump(val offset: Long) : Instruction()
    data class JumpIfEven(val register: Register, val offset: Long) : Instruction()
    data class JumpIfOne(val register: Register, val offset: Long) : Instruction()
}

val inputParser: Parser<List<Instruction>> = parser {
    chain1(instructionParser, whitespace).terms
}

val instructionParser = parser {
    oneOf(halfParser, tripleParser, incParser, jumpParser, jumpIfEvenParser, jumpIfOneParser)
}

val registerAParser = parser {
    char('a')
    Register.A
}
val registerBParser = parser {
    char('b')
    Register.B
}
val registersParser = parser { oneOf(registerAParser, registerBParser) }

val halfParser = parser {
    string("hlf ")
    val register = registersParser()
    Instruction.Half(register)
}

val tripleParser = parser {
    string("tpl ")
    val register = registersParser()
    Instruction.Triple(register)
}

val incParser = parser {
    string("inc ")
    val register = registersParser()
    Instruction.Increment(register)
}

val jumpParser = parser {
    string("jmp ")
    val offset = integer()
    Instruction.Jump(offset)
}

val jumpIfEvenParser = parser {
    string("jie ")
    val register = registersParser()
    string(", ")
    val offset = integer()
    Instruction.JumpIfEven(register, offset)
}

val jumpIfOneParser = parser {
    string("jio ")
    val register = registersParser()
    string(", ")
    val offset = integer()
    Instruction.JumpIfOne(register, offset)
}