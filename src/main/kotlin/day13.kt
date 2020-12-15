package day13

import utils.*
import kotlin.math.ceil

fun departureDelay(time: Long, bus: Long) = ceil(time.toDouble() / bus).toLong() * bus - time

fun part1(): Long {
    val lines = (aocDataDir / "day13.txt").readLines()
    val departureTime = lines[0].toLong()
    return lines[1].split(",").asSequence()
        .mapNotNull { str -> str.toLongOrNull()?.let { it to departureDelay(departureTime, it)} }
        .minBy { it.second }!!
        .let { it.first * it.second }
}

fun combine(p: Pair<Long, Long>, q: Pair<Long, Long>): Pair<Long, Long> {
    var current = p.first
    while (departureDelay(current, q.second) != (q.first % q.second)) {
        current += p.second
    }

    return current to p.second * q.second
}

fun part2() = (aocDataDir / "day13.txt").readLines()[1].split(",").withIndex()
    .mapNotNull {  (index, string) -> string.toLongOrNull()?.let { index.toLong() to it }  }
    .fold(0L to 1L, ::combine)

fun main () {
    println(part2())
    println(listOf(2L to 6L).fold(0L to 1L, ::combine))
}
