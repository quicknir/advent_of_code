package day8

import utils.*

enum class InstructionType {
    JMP,
    NOP,
    ACC
}

data class Instruction(val type: InstructionType, val arg: Int)

fun getData() = (aocDataDir / "day8.txt").useLines { lines ->
    lines.map { line ->
        val (typeStr, argStr) = line.split(" ")
        val type = when (typeStr) {
            "jmp" -> InstructionType.JMP
            "nop" -> InstructionType.NOP
            "acc" -> InstructionType.ACC
            else -> throw Exception("Bad data!")
        }
        Instruction(type, argStr.toInt())
    }.toList()
}

data class State(val accumulator: Int, val index: Int)

fun State.execute(program: List<Instruction>): State {
    val instr = program[index]
    return when (instr.type) {
        InstructionType.JMP -> copy(index = index + instr.arg)
        InstructionType.NOP -> copy(index = index + 1)
        InstructionType.ACC -> copy(index = index + 1, accumulator = accumulator + instr.arg)
    }
}

operator fun State.rem(program: List<Instruction>) = execute(program)

fun part1(): Int {
    val program = getData()
    val seen = mutableSetOf<Int>()
    var state = State(0, 0)
    while (true) {
        state %= program
        if (state.index in seen) return state.accumulator
        seen += state.index
    }
}

fun part2(): Int {
    val program = getData()
    var currentState = State(0, 0)
    val visited = mutableSetOf<Int>()
    while (true) {
        val instr = program[currentState.index]
        if (instr.type == InstructionType.ACC) {
            currentState %= program
            continue
        }
        val oldState = currentState

        currentState = when (instr.type) {
            InstructionType.JMP -> currentState.copy(index = currentState.index + 1)
            InstructionType.NOP -> currentState.copy(index = currentState.index + instr.arg)
            else -> throw Exception("oops")
        }
        while (true) {
            if (currentState.index == program.size) {
                return currentState.accumulator
            }
            if (currentState.index in visited) {
                break
            }
            visited += currentState.index
            currentState %= program
        }
        currentState = oldState % program
    }
}

fun main() {
    println(part1())
}