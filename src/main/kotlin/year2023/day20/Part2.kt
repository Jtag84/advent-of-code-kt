package year2023.day20

import commons.Part.Companion.part2
import guru.nidi.graphviz.attribute.Color
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.graph
import guru.nidi.graphviz.toGraphviz
import org.apache.commons.math3.primes.Primes
import java.io.File


fun main() {
    // Uncomment the line below to create a graph of all the modules, you will need graphviz installed:  brew install graphviz
//    createGraph.run()
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val createGraph = part2(inputParser, 1) { modules ->
    val moduleGraph = graph("modules", directed = true) {
        "button" - modules["broadcaster"]!!.name
        modules.values.map { module ->
            module.outputs.map { output ->
                val color = when (module) {
                    is Module.Broadcaster -> Color.TAN
                    is Module.Conjunction -> Color.BLUE
                    is Module.FlipFlop -> Color.RED
                }
                module.name[color] - output
            }
        }
    }

    moduleGraph.toGraphviz().render(Format.PNG).toFile(File("modules.png"))
}

val part2 = part2(inputParser, 1L) { modules ->
    val moduleOutputtingToRx = modules.values.first { it.outputs == setOf("rx") }
    check(moduleOutputtingToRx is Module.Conjunction)

    val inputsFromConjunction = modules.values.filter { it.outputs.contains(moduleOutputtingToRx.name) }.map { it.name }

    // Similar to Day 8 you only need to count the number of button push needed to trigger a high pulse on each input of the conjunction module that
    // outputs to rx. From there you calculate all the prime factors of those counts, remove the duplicates and multiply them all together
    inputsFromConjunction.asSequence()
        .map { buttonPressCountUntilHighPulseTo(modules.init(), it, moduleOutputtingToRx.name) }
        .flatMap {
            if (it == 1) {
                listOf(1)// this is just to pass the test, not needed to solve part2
            } else {
                Primes.primeFactors(it)
            }
        }
        .distinct()
        .map { it.toLong() }
        .reduce(Math::multiplyExact)
}

private fun buttonPressCountUntilHighPulseTo(
    initializedModules: Map<ModuleName, Module>,
    from: ModuleName,
    to: ModuleName
) =
    generateSequence { pushButton(initializedModules) }
        .takeWhile { buttonSequence ->
            buttonSequence.flatten().none { it.first && it.second == from && it.third == to }
        }
        .count() + 1