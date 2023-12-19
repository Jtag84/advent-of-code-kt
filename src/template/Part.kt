package <PACKAGE_NAME>

import commons.Part.Companion.part<PART_NUMBER>

fun main() {
    part<PART_NUMBER>.runAndPrintTest()
    part<PART_NUMBER>.runAndPrint()
}

val part<PART_NUMBER> = part<PART_NUMBER>(inputParser, 1) { input ->
    input
}

