package year2023.day15

import cc.ekblad.konbini.*
import year2023.day15.Step.Add
import year2023.day15.Step.Remove

val inputParser = parser {
    chain1(parser { regex("[a-z-=0-9]+") }, parser { char(',') }).terms
}

val stepsParser = parser {
    chain1(stepParser, parser { char(',') }).terms
}

val stepParser = parser {
    oneOf(removeStepParser, addStepParser)
}

val labelParser = parser { regex("[a-z]+") }

val removeStepParser = parser {
    val label = labelParser()
    char('-')
    Remove(label)
}

val addStepParser = parser {
    val label = labelParser()
    char('=')
    val focalLens = integer()
    Add(label, focalLens.toInt())
}

sealed class Step {
    abstract val label: String

    data class Remove(override val label: String) : Step()

    data class Add(override val label: String, val focalLens: Int) : Step()
}