package year2020.day21

import cc.ekblad.konbini.*
import commons.Lines
import commons.parseLines

typealias Ingredient = String
typealias Ingredients = Set<Ingredient>
typealias Allergen = String
typealias Allergens = Set<Allergen>
typealias Food = Pair<Ingredients, Allergens>

val inputParser: Parser<List<Food>> = parser {
    chain1(foodParser, whitespace).terms
}

val ingredientsParser = parser {
    chain1(parser { regex("[a-z]+") }, whitespace).terms.toSet()
}

val allergensParser = parser {
    val allergens = bracket(
        parser { string("(contains ") },
        parser { char(')') } ,
        parser { chain1(parser { regex("[a-z]+") }, parser { string(", ") }) }
    )
    allergens.terms.toSet()
}

val foodParser = parser {
    val ingredients = ingredientsParser()
    whitespace()
    val allergens = allergensParser()
    ingredients to allergens
}