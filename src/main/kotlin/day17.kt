package day17

import utils.*

interface Dimension {
    val dimension: Int
}

object DimThree : Dimension {
    override val dimension = 3
}

object DimFour : Dimension {
    override val dimension = 4
}

class Point<D : Dimension> private constructor(val data: List<Int>) {
    constructor(d: D, init: (Int) -> Int) : this(List(d.dimension, init))

    operator fun <D : Dimension> plus(p: Point<D>) = Point<D>(data.zip(p.data) { x, y -> x + y })
}

fun <D : Dimension> makeNeighborSteps(d: D): List<Point<D>> {
    val ret = mutableListOf(Point(d) { 0 })
    for (dimIndex in 0 until d.dimension) {
        val curRetSize = ret.size
        for (retIndex in 0 until curRetSize) {
            ret += Point(d) { if (it == dimIndex) 1 else ret[retIndex].data[it] }
            ret += Point(d) { if (it == dimIndex) -1 else ret[retIndex].data[it] }
        }
    }
    return ret.subList(1, ret.size)
}

fun <D : Dimension> Pair<Int, Int>.toPoint(d: D) =
    Point(d) {
        when (it) {
            0 -> first
            1 -> second
            else -> 0
        }
    }

fun <D : Dimension> getData(d: D) = (aocDataDir / "day17.txt").useLines { lines ->
    lines.mapIndexed { x, line ->
        line.mapIndexedNotNull { y, c -> if (c == '#') (x to y).toPoint(d) else null }
    }.flatten().toSet()
}

fun <D : Dimension> Set<Point<D>>.evolve(neighbors: List<Point<D>>): Set<Point<D>> {
    val neighborCounts = mutableMapOf<Point<D>, Int>()
    for (p in this) {
        for (n in neighbors) {
            neighborCounts.update(p + n) { (it ?: 0) + 1 }
        }
    }

    return neighborCounts.asSequence().filter { (p, neighborCount) ->
        (neighborCount == 3) || (neighborCount == 2 && p in this)
    }.map { it.key }.toSet()
}

fun <D : Dimension> run(d: D): Int {
    var state = getData(d)
    val neighbors = makeNeighborSteps(d)
    for (i in 1..6) {
        state %= { evolve(neighbors) }
    }
    return state.size
}

fun part1() = run(DimThree)

fun part2() = run(DimFour)

fun main() {
    println(part2())
}