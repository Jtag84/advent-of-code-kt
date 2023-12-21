package year2015.day16

import cc.ekblad.konbini.*

val inputParser: Parser<List<Sue>> = parser {
    chain1(sueParser, whitespace1).terms
}

data class Sue(
    val id: Long,
    val children: Long?,
    val cats: Long?,
    val samoyeds: Long?,
    val pomeranians: Long?,
    val akitas: Long?,
    val vizslas: Long?,
    val goldfish: Long?,
    val trees: Long?,
    val cars: Long?,
    val perfumes: Long?,
)

fun sueBuilder(id: Long, inputs: Map<String, Long>): Sue {
    return Sue(
        id,
        children = inputs["children"],
        cats = inputs["cats"],
        samoyeds = inputs["samoyeds"],
        pomeranians = inputs["pomeranians"],
        akitas = inputs["akitas"],
        vizslas = inputs["vizslas"],
        goldfish = inputs["goldfish"],
        trees = inputs["trees"],
        cars = inputs["cars"],
        perfumes = inputs["perfumes"],
    )
}

val sueParser = parser {
    string("Sue ")
    val id = integer()
    string(": ")
    val detectedItems = chain1(detectedItemParser, parser { string(", ") }).terms

    sueBuilder(id, detectedItems.toMap())
}

val detectedItemParser = parser {
    val itemName = regex("[a-z]+")
    string(": ")
    val count = integer()
    itemName to count
}