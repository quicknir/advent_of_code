package day6

import utils.*

fun getData() = (aocDataDir / "day6.txt").readText()
    .split("\n\n")
    .map { it.split("\n").filter { it.isNotEmpty() }.map { it.toSet() }}

fun part1() = getData().sumBy { group ->
    group.reduce { acc, set -> acc union set }.size
}

fun part2() = getData().sumBy { group ->
    group.reduce { acc, set -> acc intersect set }.size
}

fun main() {
    println(part2())
}