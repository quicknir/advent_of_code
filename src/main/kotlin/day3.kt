package day3

import utils.*

fun getData() = (aocDataDir / "day3.txt").useLines { lines ->
    lines.map { line -> line.map { it == '#'} }.toList()
}

fun List<List<Boolean>>.countTrees(down: Int, right: Int) = asSequence()
    .filterIndexed { i, _ -> i % down == 0 }
    .withIndex()
    .count { (index, row) -> row[right * index % row.size] }


fun part1() = getData().countTrees(1, 3).also { println(it) }

fun part2() = getData().run { sequenceOf(1 to 1, 3 to 1, 5 to 1, 7 to 1, 1 to 2)
    .map { (right, down) -> countTrees(down, right).also { println(it) } }
    .fold(1L) { x, y -> x * y }
    .also { println("Product: $it") }
}

fun main() {
    part2()
}


