package day14

import utils.*
import kotlin.math.pow

sealed class Instruction

data class Mask(val forceOne: ULong, val forceZero: ULong, val xVals: List<ULong>) : Instruction()
data class MemoryWrite(val address: ULong, val value: ULong) : Instruction()

fun getInput() = (aocDataDir / "day14.txt").useLines { lines -> lines.map { line ->
    if (line.startsWith("mask")) {
        val maskString = line.split(" = ")[1].reversed()
        val onesSum = maskString.withIndex().map { (index, char) ->
            if (char == '1') 2.0.pow(index).toULong() else 0UL
        }.sum()
        val zerosSum = maskString.withIndex().map { (index, char) ->
            if (char == '0') 2.0.pow(index).toULong() else 0UL
        }.sum()
        val xIndices = maskString.withIndex().mapNotNull { (index, char) ->
            if (char == 'X') 2.0.pow(index).toULong() else null
        }
        Mask(onesSum, ULong.MAX_VALUE - zerosSum, xIndices)
    }
    else {
        val (first, second) = line.split(" = ")
        MemoryWrite(first.split("[")[1].removeSuffix("]").toULong(), second.toULong())
    }
    }.toList()
}

fun runProgram(applyInstruction: MutableMap<ULong, ULong>.(Mask, MemoryWrite) -> Unit): ULong {
    val input = getInput()
    var mask = Mask(0UL, ULong.MAX_VALUE, emptyList())
    val memory = mutableMapOf<ULong, ULong>()
    for (instruction in input) {
        when (instruction) {
            is Mask -> mask = instruction
            is MemoryWrite -> memory.applyInstruction(mask, instruction)
        }
    }
    return memory.values.sum()
}

fun Mask.mask(ulong: ULong) = (ulong or forceOne) and forceZero

fun part1() = runProgram { mask, instruction -> set(instruction.address, mask.mask(instruction.value))}

fun Mask.maskV2(ulong: ULong): List<ULong> {
    val outputs = mutableListOf(ulong or forceOne)
    for (xVal in xVals) {
        for (i in outputs.indices) {
            outputs += outputs[i] xor xVal
        }
    }
    return outputs
}

fun part2() = runProgram { mask, instruction ->
    mask.maskV2(instruction.address).forEach { set(it, instruction.value) }
}

fun main() {
    println(part1())
}

