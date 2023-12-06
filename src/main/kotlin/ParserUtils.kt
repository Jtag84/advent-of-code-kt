import cc.ekblad.konbini.many
import cc.ekblad.konbini.parser
import cc.ekblad.konbini.regex

val newLine = parser { regex("(\\r\\n|\\r|\\n)") }
val newLines = parser { many(newLine) }

val parseAll = parser { regex("[\\s\\S]*") }

