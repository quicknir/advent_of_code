package day23

import java.lang.Math.floorMod

fun getInput() = listOf(2,5,3,1,4,9,8,6,7)

fun play(input: List<Int>, turns: Int): Map<Int, Int> {
    val cups = input.indices.associateTo(mutableMapOf()) { input[it] to input[floorMod(it+1, input.size)]}
    var first = input.first()

    val min = 1
    val max = input.size

    repeat(turns) {
        var insertValue = (first - 1).let { if (it < min) max else it }
        val nextThree = generateSequence(cups[first]) { cups[it] }.take(3).toList()
        while (insertValue in nextThree) {
            insertValue = (insertValue - 1).let { if (it < min) max else it }
        }
        val afterInsert = cups.getValue(insertValue)

        cups[first] = cups.getValue(nextThree.last())
        cups[insertValue] = nextThree.first()
        cups[nextThree.last()] = afterInsert
        first = cups.getValue(first)
    }
    return cups
}

fun part1() = play(getInput(), 100).let { cups ->
    generateSequence(cups[1]) { cups[it] }.take(cups.size - 1).joinToString("")
}

fun part2() = play(getInput() + (10..1000000), 10000000).let { cups ->
    generateSequence(cups[1]) { cups[it] }.take(2).fold(1L) { acc: Long, i: Int -> acc * i }
}

fun main() {
    println(part2())
}