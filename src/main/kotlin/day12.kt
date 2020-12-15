package day12

import utils.*
import kotlin.math.absoluteValue

operator fun<R> R.rem(transform: R.() -> R) = this.transform()

data class Point(val i: Int, val j: Int)

operator fun Point.plus(p: Point) = Point(i + p.i, j + p.j)
operator fun Int.times(p: Point) = Point(this * p.i, this * p.j)

sealed class Action {
    object F : Action()
}

sealed class Direction(val vector: Point): Action() {
    object N : Direction(Point(0, 1))
    object S : Direction(Point(0, -1))
    object E : Direction(Point(1, 0))
    object W : Direction(Point(-1, 0))
}

sealed class Rotate(val dir: Int): Action() {
    object L : Rotate(-1)
    object R : Rotate(1)
}

fun rotate(rot: Rotate, value: Int, vector: Point): Point {
    assert (value % 90 == 0)

    return when(Math.floorMod(value / 90 * rot.dir, 4)) {
        0 -> vector
        1 -> Point(vector.j, -vector.i)
        2 -> Point(-vector.i, -vector.j)
        3 -> Point(-vector.j, vector.i)
        else -> error("")
    }
}

data class Instruction(val action: Action, val value: Int)

fun getInput() = (aocDataDir / "day12.txt").useLines { lines -> lines.map { line ->
    val action = when(line.first()) {
        'N' -> Direction.N
        'S' -> Direction.S
        'E' -> Direction.E
        'W' -> Direction.W
        'L' -> Rotate.L
        'R' -> Rotate.R
        'F' -> Action.F
        else -> throw Exception("oops")
    }
    Instruction(action, line.substring(1).toInt())
}.toList() }

data class State(val loc: Point, val dir: Point)

fun State.evolve1(instruction: Instruction) = when (instruction.action) {
    is Direction -> State(loc + instruction.value * instruction.action.vector, dir)
    is Action.F -> State(loc + instruction.value * dir, dir)
    is Rotate -> State(loc, rotate(instruction.action, instruction.value, dir))
}

fun State.evolve2(instruction: Instruction) = when (instruction.action) {
    is Direction -> State(loc, dir + instruction.value * instruction.action.vector)
    is Action.F -> State(loc + instruction.value * dir, dir)
    is Rotate -> State(loc, rotate(instruction.action, instruction.value, dir))
}

fun runData(startDir: Point, evolver: State.(Instruction) -> State): Int {
    var s = State(Point(0,0), startDir)
    getInput().forEach { s %= { evolver(it) } }
    return s.loc.i.absoluteValue + s.loc.j.absoluteValue
}

fun part1() = runData(Direction.E.vector, State::evolve1)
fun part2() = runData(Point(10, 1), State::evolve2)

fun bar1(f: Int.() -> Unit) {}
fun bar2(f: (Int) -> Unit) {}

fun f(x: Int): Unit {}

fun main() {
    println(part2())
    val x = { i: Int -> Unit }
    val y: Int.() -> Unit = { }
    bar1(::f)
}
