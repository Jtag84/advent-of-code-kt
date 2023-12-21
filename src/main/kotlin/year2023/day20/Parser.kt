package year2023.day20

import cc.ekblad.konbini.*

val inputParser: Parser<Map<ModuleName, Module>> = parser {
    chain1(moduleParser, whitespace1).terms.associateBy { it.name }
}

typealias ModuleName = String
typealias FromModuleName = ModuleName
typealias ToModuleName = ModuleName

typealias Pulse = Triple<Boolean, FromModuleName, ToModuleName>

sealed class Module {
    abstract val name: ModuleName
    abstract val outputs: Set<ModuleName>

    data class Broadcaster(override val outputs: Set<ModuleName>, override val name: ModuleName = "broadcaster") :
        Module()

    data class Conjunction(
        override val name: ModuleName,
        override val outputs: Set<ModuleName>,
        val inputState: MutableMap<ModuleName, Boolean> = mutableMapOf()
    ) : Module()

    data class FlipFlop(
        override val name: ModuleName,
        override val outputs: Set<ModuleName>,
        var isOn: Boolean = false
    ) : Module()
}

val moduleParser = parser {
    oneOf(broadcasterParser, conjunctionParser, flipFlopParser)
}

val outputArrowParser = parser { string(" -> ") }

val moduleNameParser = parser { regex("[a-z]+") }

val outputsParser = parser {
    outputArrowParser()
    chain1(moduleNameParser, parser { string(", ") }).terms.toSet()
}

val broadcasterParser = parser {
    string("broadcaster")
    val outputs = outputsParser()

    Module.Broadcaster(outputs)
}

val conjunctionParser = parser {
    char('&')
    val name = moduleNameParser()
    val outputs = outputsParser()

    Module.Conjunction(name, outputs)
}

val flipFlopParser = parser {
    char('%')
    val name = moduleNameParser()
    val outputs = outputsParser()

    Module.FlipFlop(name, outputs)
}