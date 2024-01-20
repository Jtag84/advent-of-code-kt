package year2019.day18

import cc.ekblad.konbini.*
import commons.EnumParser
import commons.coordinatesParser

val inputParser = parser {
    coordinatesParser(tunnelElementParser)().toMap()
}

sealed class TunnelElement : EnumParser {

    data object Wall: TunnelElement() {
        override val parsingString: String
            get() = "#"
    }

    data object Entrance : TunnelElement(){
        override val parsingString: String
            get() = "@"
    }
    data object Empty : TunnelElement(){
        override val parsingString: String
            get() = "."
    }

    data class Door(val doorName: Char) : TunnelElement() {
        val matchingKey = doorName.lowercase().first()

        override val parsingString: String
            get() = doorName.toString()

        fun canBeOpened(keys: Set<Char>) : Boolean {
            return keys.contains(matchingKey)
        }
    }
    data class Key(val keyName: Char) : TunnelElement() {
        override val parsingString: String
            get() = keyName.toString()
    }
}

val tunnelElementParser = parser { oneOf(wallParser, emptyParser, doorParser, keyParser, entranceParser) }

val keyParser : Parser<TunnelElement> = parser { regex("[a-z]") }.map { TunnelElement.Key(it.first()) }
val doorParser : Parser<TunnelElement> = parser { regex("[A-Z]") }.map { TunnelElement.Door(it.first()) }
val entranceParser : Parser<TunnelElement> = parser { char('@') }.map { TunnelElement.Entrance }
val wallParser : Parser<TunnelElement> = parser { char('#') }.map { TunnelElement.Wall }
val emptyParser : Parser<TunnelElement> = parser { char('.') }.map { TunnelElement.Empty }