package <PACKAGE_NAME>

import cc.ekblad.konbini.*
import commons.Lines
import commons.parseLines

val inputParser: Parser<Lines> = parser {
    parseLines()
}