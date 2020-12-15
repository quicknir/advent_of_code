package day11

import utils.*

enum class Seat {
    EMPTY,
    OCCUPIED,
    FLOOR
}

data class Point(val i: Int, val j: Int)

operator fun Point.plus(p: Point) = Point(i + p.i, j + p.j)

fun directionPoints() = (-1..1).asSequence().map { i ->
    (-1..1).asSequence().map { j -> Point(i, j) }
}.flatten().filter { it.i != 0 || it.j != 0 }

typealias SeatMap = List<List<Seat>>

fun SeatMap.getSeat(p: Point) = getOrNull(p.i)?.getOrNull(p.j)

operator fun SeatMap.contains(p: Point) = p.i in indices && p.j in first().indices

fun SeatMap.withPoints() = withIndex().asSequence().map { (i, row) ->
    withIndex().asSequence().map { (j, seat) ->
        Point(i, j) to seat
    }
}.flatten()

fun getData() = (aocDataDir / "day11.txt").readLines().map { line ->
    line.map {
        when (it) {
            '.' -> Seat.FLOOR
            'L' -> Seat.EMPTY
            '#' -> Seat.OCCUPIED
            else -> throw Exception("oops")
        }
    }
}


fun SeatMap.adjacentNeighbors(p: Point) = directionPoints().map {
    if (getSeat(p + it) == Seat.OCCUPIED) 1 else 0
}.sum()

fun SeatMap.visibleNeighbors(p: Point) = directionPoints().map { dirPoint ->

    val firstSeen = generateSequence(p + dirPoint) { it + dirPoint }
        .filter { foo ->
            getSeat(foo) != Seat.FLOOR
        }.first()

    when (getSeat(firstSeen)) {
        null, Seat.EMPTY -> 0
        Seat.OCCUPIED -> 1
        else -> throw Exception("")
    }
}.sum()

fun SeatMap.evolve(occupiedThreshold: Int, neighborCounter: SeatMap.(Point) -> Int): SeatMap {
    return withIndex().map { (i, row) ->
        row.withIndex().map { (j, seat) ->
            val neighbors = neighborCounter(Point(i, j))
            if (seat == Seat.EMPTY && neighbors == 0) Seat.OCCUPIED
            else if (seat == Seat.OCCUPIED && neighbors >= occupiedThreshold) Seat.EMPTY
            else seat
        }
    }
}

fun runToStable(occupiedThreshold: Int, neighborCounter: SeatMap.(Point) -> Int): Int {
    var seats = getData()
    while (true) {
        val nextSeats = seats.evolve(occupiedThreshold, neighborCounter)
        if (seats == nextSeats) {
            return seats.flatten().count { it == Seat.OCCUPIED }
        }
        seats = nextSeats
    }
}

fun part1(): Int = runToStable(4, SeatMap::adjacentNeighbors)

fun part2(): Int = runToStable(5, SeatMap::visibleNeighbors)

fun main() {
    println(part2())
}

