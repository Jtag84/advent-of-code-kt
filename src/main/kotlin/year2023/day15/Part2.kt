package year2023.day15

import commons.Part.Companion.part2

fun main() {
    part2.runTest()
    part2.run()
}

val part2 = part2(stepsParser, 145) { input ->
    input.fold(mutableMapOf<Int, LinkedHashMap<String, Int>>()) { boxes, step: Step ->
        val box = boxes.computeIfAbsent(hash(step.label)) { LinkedHashMap() }
        when (step) {
            is Step.Add -> box[step.label] = step.focalLens
            is Step.Remove -> box.remove(step.label)
        }
        boxes
    }
        .map { (it.key + 1) * it.value.entries.mapIndexed { index, entry -> (index + 1) * entry.value }.sum() }
        .sum()
}