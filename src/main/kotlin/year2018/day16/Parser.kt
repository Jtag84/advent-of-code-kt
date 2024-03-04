package year2018.day16

import arrow.core.Tuple4
import cc.ekblad.konbini.*
import commons.Lines
import commons.newLine
import commons.newLines
import commons.parseLines

typealias Registers = List<Long>
typealias BeforeRegisters = Registers
typealias AfterRegisters = Registers
typealias Instruction = List<Long>
typealias Sample = Triple<BeforeRegisters, Instruction, AfterRegisters>

val inputParser = parser {
    val samples = chain1(sampleParser, parser { newLine(); newLine() }).terms
    whitespace()
    val program = chain1(instructionParser, whitespace).terms
    samples to program
}

enum class InstructionEnum {
    ADDR,
    ADDI,
    MULR,
    MULI,
    BANR,
    BANI,
    BORR,
    BORI,
    SETR,
    SETI,
    GTIR,
    GTRI,
    GTRR,
    EQIR,
    EQRI,
    EQRR;

    fun execute(registers: Registers, a: Long, b: Long, c: Long) : Registers{
        val newRegisters = registers.toMutableList()
        val registerA = registers[a.toInt()]
        val registerB = registers[b.toInt()]
        val registerCSetter = {value : Long -> newRegisters[c.toInt()] = value}
        when(this) {
            ADDR -> registerCSetter(registerA + registerB)
            ADDI -> registerCSetter(registerA + b)
            MULR -> registerCSetter(registerA * registerB)
            MULI -> registerCSetter(registerA * b)
            BANR -> registerCSetter(registerA.and(registerB))
            BANI -> registerCSetter(registerA.and(b))
            BORR -> registerCSetter(registerA.or(registerB))
            BORI -> registerCSetter(registerA.or(b))
            SETR -> registerCSetter(registerA)
            SETI -> registerCSetter(a)
            GTIR -> if(a > registerB) {registerCSetter(1)} else {registerCSetter(0)}
            GTRI -> if(registerA > b) {registerCSetter(1)} else {registerCSetter(0)}
            GTRR -> if(registerA > registerB) {registerCSetter(1)} else {registerCSetter(0)}
            EQIR -> if(a == registerB) {registerCSetter(1)} else {registerCSetter(0)}
            EQRI -> if(registerA == b) {registerCSetter(1)} else {registerCSetter(0)}
            EQRR -> if(registerA == registerB) {registerCSetter(1)} else {registerCSetter(0)}
        }

        return newRegisters.toList()
    }
}

val registerParser: Parser<Registers> = parser {
    char('[')
    val registers = chain1(integer, parser { string(", ") })
    char(']')
    registers.terms
}

val instructionParser = parser {
    chain1(integer, parser { string(" ") }).terms
}

val sampleParser = parser {
    string("Before: ")
    whitespace()
    val beforeRegisters = registerParser()
    whitespace()
    val instruction = instructionParser()
    whitespace()
    string("After: ")
    whitespace()
    val afterRegisters = registerParser()

    Triple(beforeRegisters, instruction, afterRegisters)
}