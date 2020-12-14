package day10

import utils.*

fun getData() = (aocDataDir / "day10.txt").readLines().map { it.toLong() }

fun part1() = (sequenceOf(0L) + getData().sorted().asSequence())
    .zipWithNext { a, b -> b - a}
    .groupingBy { it }.eachCount()
    .run {
        getValue(1) * (getValue(3) + 1)
    }

fun part2(): Long {
    val data = getData().sorted()
    val result = mutableMapOf(0L to 1L)

    for (e in data) {
        result[e] = (1..3).map { result.getOrDefault(e-it, 0) }.sum()
    }

    return result.getValue(data.last())
}

fun MutableMap<Long, Long?>.part2Helper(key: Long): Long {
    get(key)?.let { return it }
    if (key !in this) {
        return 0L
    }
    return (1..3).map { part2Helper(key - it) }.sum().also { set(key, it) }
}

fun part2Fast(): Long {
    val cache = mutableMapOf<Long, Long?>().apply {
        getData().associateTo(this) { it to null }
    }
    cache[0L] = 1L
    return cache.part2Helper(cache.keys.maxOrNull()!!)
}

fun main() {
    println(part2())
}