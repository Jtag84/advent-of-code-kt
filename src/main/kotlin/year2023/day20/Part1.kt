package year2023.day20

import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 11687500) { modules ->
    val initializedModules = modules.init()
    generateSequence { pushButton(initializedModules) }.take(1000).flatten()
        .fold(0 to 0) { (lowPulseCount, highPulseCount), pulses ->
            val currentHighCount = pulses.count { it.first }
            val currentLowCount = pulses.size - currentHighCount
            (lowPulseCount + currentLowCount) to (highPulseCount + currentHighCount)
        }
        .let { it.first * it.second }
}

fun Map<ModuleName, Module>.init(): Map<ModuleName, Module> {
    val conjunctions = this.values.filterIsInstance<Module.Conjunction>().associateBy { it.name }
    val flipFlops = this.values.filterIsInstance<Module.FlipFlop>()

    flipFlops.forEach { it.isOn = false }
    this.values.map { module ->
        module.outputs
            .mapNotNull { conjunctions[it] }
            .forEach { it.inputState[module.name] = false }
    }

    return this
}

fun pushButton(modules: Map<ModuleName, Module>): Sequence<List<Pulse>> {
    return generateSequence(listOf(Pulse(false, "button", "broadcaster"))) { pulses ->
        if (pulses.isEmpty()) {
            null
        } else {
            pulses.flatMap { processPulse(modules, it) }
        }
    }
}

fun processPulse(modules: Map<ModuleName, Module>, pulse: Pulse): List<Pulse> {
    val (isHigh, from, to) = pulse
    return when (val toModule = modules[to]) {
        is Module.Broadcaster -> toModule.outputs.map { Pulse(isHigh, to, it) }
        is Module.Conjunction -> {
            toModule.inputState[from] = isHigh
            val pulseStateToSend = toModule.inputState.values.any { it.not() }
            toModule.outputs.map { Pulse(pulseStateToSend, to, it) }
        }

        is Module.FlipFlop -> {
            if (isHigh.not()) {
                toModule.isOn = toModule.isOn.not()
                toModule.outputs.map { Pulse(toModule.isOn, to, it) }
            } else {
                emptyList()
            }
        }

        null -> emptyList()
    }
}