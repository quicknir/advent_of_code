package day1

import utils.*

fun getInputs() = mutableMapOf<Int, Int>().apply {
    (aocDataDir / "day1.txt").forEachLine { line ->
        update(line.toInt()) { (it ?: 0) + 1 }
    }
}

fun Map<Int, Int>.checkSumPair(sum: Int): Pair<Int, Int>? {
    if (sum % 2 == 0 && getOrDefault(sum / 2, 0) >= 2) {
        return Pair(sum/2, sum/2)
    }
    return keys.find { (it <= (sum-1) / 2) && (getOrDefault(sum-it, 0) >=1) }?.let {
        Pair(it, sum-it)
    }
}

fun part1() = getInputs().checkSumPair(2020)?.also { (x, y) -> println("${x*y}")}

fun part2() {
    val inputs = getInputs()
    for ((k, v) in inputs) {
        inputs[k] = v - 1
        inputs.checkSumPair(2020-k)?.also {
            println("${k * it.first * it.second}")
            return
        }
        inputs[k] = v
    }
}

fun main() {
    part2()
}

