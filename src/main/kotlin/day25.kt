package day25

import utils.*

val first = 2959251L
val second = 4542595L

fun transform(value: Long, subject: Long) = (value * subject) % 20201227

fun part1(): Long {
    val numLoops = generateSequence(1L) { transform(it, 7) }.indexOf(first)
    return generateSequence(1L) { transform(it, second) }.drop(numLoops).first()
}

fun main() {
    println(part1())
}