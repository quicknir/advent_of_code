package day5

import kotlin.math.pow
import utils.*

fun String.asBinary(one: Char) = reversed()
    .withIndex()
    .map { 2.0.pow(it.index) * if(it.value == one) 1 else 0 }
    .sum().toInt()

fun toId(row: Int, col: Int) = row * 8 + col

fun getInput() = (aocDataDir / "day5.txt").useLines { lines ->
    lines.map { line ->
        toId(line.substring(0,7).asBinary('B'), line.substring(7).asBinary('R'))
    }.toList()
}

fun part1() = getInput().maxOrNull()!!.also { println(it) }

fun part2(): Int {
    val ids = getInput().toSet()
    return (0..toId(127, 7)).asSequence().filter {
        it !in ids && (it+1) in ids && (it-1) in ids
    }.single().also { println(it) }
}

fun main() {
    part2()
}