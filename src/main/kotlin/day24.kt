package day24

import utils.*

enum class Direction(val north: Int, val east: Int) {
    NE(1, 1),
    E(0, 2),
    SE(-1, 1),
    SW(-1, -1),
    W(0, -2),
    NW(1, -1),
}

operator fun Direction.plus(p: Point) = Point(p.i + north, p.j + east)

fun getInput() = (aocDataDir / "day24.txt").useLines { lines ->
    lines.map { line ->
        generateSequence(Direction.NE to line) { (_, string) ->
            if (string.isBlank()) {
                return@generateSequence null
            }
            run {
                if (string.length < 2) {
                    return@run null
                }
                when (string.substring(0, 2)) {
                    "ne" -> Direction.NE
                    "se" -> Direction.SE
                    "sw" -> Direction.SW
                    "nw" -> Direction.NW
                    else -> null
                }
            }?.let { return@generateSequence it to string.substring(2) }

            when (string.first()) {
                'e' -> Direction.E
                'w' -> Direction.W
                else -> throw Exception("oops")
            } to string.substring(1)
        }.drop(1).map { it.first }.toList()
    }.toList()
}

fun startingBlackTiles() = getInput().groupingBy { directions ->
    directions.fold(Point(0, 0)) { point, direction -> direction + point }
}.eachCount().filter { it.value % 2 == 1 }.keys

fun part1() = startingBlackTiles().size

fun part2(): Int {
    val blackTiles = startingBlackTiles().toMutableSet()

    repeat(100) {
        val blackNeighborCounts = blackTiles.asSequence()
            .flatMap { point -> Direction.values().map { it + point } }
            .groupingBy { it }
            .eachCount()

        for (point in blackTiles.toList()) {
            blackNeighborCounts.getOrDefault(point, 0).let { neighbors ->
                if (neighbors == 0 || neighbors > 2) {
                    blackTiles -= point
                }
            }
        }

        for ((point, count) in blackNeighborCounts) {
            if (point !in blackTiles && count == 2) {
                blackTiles += point
            }
        }
    }

    return blackTiles.size
}

fun main() {
    println(part2())
}
