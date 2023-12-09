package <PACKAGE_NAME>

import Part.Companion.part<PART_NUMBER>

fun main() {
    part<PART_NUMBER>.runTest()
    part<PART_NUMBER>.run()
}

val part<PART_NUMBER> = part<PART_NUMBER>(inputParser, 1) { input ->
    input
}

