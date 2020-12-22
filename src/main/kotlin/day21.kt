package day21

import utils.*

data class Food(val ingredients: List<String>, val allergens: List<String>)

fun getInput() = (aocDataDir / "day21.txt").useLines { lines ->
    lines.map { line ->
        val (ingrStr, allStr) = line.split(" (contains ")
        Food(ingrStr.split(" "), allStr.popSuffix(")")!!.split(", "))
    }.toList()
}

fun List<Food>.toAllergenPossibilities() = mutableMapOf<String, MutableSet<String>>().also {
    for ((ingredients, allergens) in this) {
        for (allergen in allergens) {
            it.update(allergen) { set -> set?.let { it.apply { retainAll(ingredients) } } ?: ingredients.toMutableSet() }
        }
    }
}

fun part1(): Int {
    val input = getInput()
    val allergensToIngredients = input.toAllergenPossibilities()
    val nonAllergenIngrs = input.map { it.ingredients }.flatten().toMutableSet()
    allergensToIngredients.values.forEach { nonAllergenIngrs -= it }
    return input.sumOf { food ->
        food.ingredients.count { it in nonAllergenIngrs }
    }
}

fun part2(): String {
    val input = getInput()
    val allergenPossibilities = input.toAllergenPossibilities()
    val allergenMap = mutableMapOf<String, String>()
    while (allergenPossibilities.isNotEmpty()) {
        val (key, value) = allergenPossibilities.asSequence().filter { it.value.size == 1 }.first()
        allergenPossibilities -= key
        allergenPossibilities.values.forEach{ it -= value }
        allergenMap[key] = value.first()
    }
    return allergenMap.toList().sortedBy { it.first }.joinToString(",") { it.second }
}

fun main() {
    println(part2())
}
