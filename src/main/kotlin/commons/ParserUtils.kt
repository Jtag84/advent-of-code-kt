package commons

import cc.ekblad.konbini.*

typealias Lines = List<String>

val newLine = parser { regex("(\\r\\n|\\r|\\n)") }
val newLines = parser { many(newLine) }

val parseText = parser { regex("[\\s\\S]*") }

val parseLines: Parser<Lines> = parser {
    this.input.lines()
}

fun <T> Parser<T>.parseOrThrowException(input: String): T {
    when (val parseResult = this.parse(input)) {
        is ParserResult.Ok -> return parseResult.result
        else -> throw IllegalStateException(parseResult.toString())
    }
}


