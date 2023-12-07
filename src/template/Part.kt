package <PACKAGE_NAME>

import Part.Companion.part

<PART_NUMBER>

fun main() {
    part<PART_NUMBER>.runTest(1)
    part<PART_NUMBER>.run()
}

val part<PART_NUMBER> = part<PART_NUMBER>(inputParser)
{ input -> 1 }

