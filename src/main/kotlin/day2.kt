package day2

import utils.*

data class RuledPassword(val range: IntRange, val char: Char, val password: String)

fun String.toRuledPassword(): RuledPassword {
    val (rangeStart, rangeEnd, char, password) = split(": ", " ", "-")
    assert(char.length == 1)
    return RuledPassword(IntRange(rangeStart.toInt(), rangeEnd.toInt()), char.first(), password)
}

fun RuledPassword.isValidOne() = password.count { it == char } in range

fun RuledPassword.isValidTwo(): Boolean{
    val charPresent = { i: Int -> password.elementAtOrNull(i-1)?.let { it == char} ?: false }
    return charPresent(range.first) xor charPresent(range.endInclusive)
}

fun countValid(validator: RuledPassword.() -> Boolean) = (aocDataDir / "day2.txt").useLines { lines ->
    lines.count { it.toRuledPassword().validator() }
    lines.map {

    }
}

fun part1() = println(countValid(RuledPassword::isValidOne))
fun part2() = println(countValid(RuledPassword::isValidTwo))

fun main() {
    part2()
}

