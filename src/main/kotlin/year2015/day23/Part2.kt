package year2015.day23

import commons.Part.Companion.part2

fun main() {
    part2.runAndPrint()
}

val part2 = part2(inputParser, null) { instructions ->
    instructions.execute(ProgramState(Registers(1, 0), 0)).first.second
}