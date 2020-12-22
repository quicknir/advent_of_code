package day20

import utils.*
import java.lang.Math.floorMod
import kotlin.math.sqrt

val seaMonster =
"""
                  # 
#    ##    ##    ###
 #  #  #  #  #  #   
"""

data class Image(val id: Int, val data: List<String>) {
    val edges = data.getEdges()
}

fun List<String>.getEdges() = listOf(
    first(),
    map { it.last() }.joinToString(""),
    last(),
    map { it.first() }.joinToString(""),
)

fun List<String>.flipTopBottom() = asReversed()
fun List<String>.flipLeftRight() = map { it.reversed() }

fun Image.flipTopBottom() = copy(data = data.flipTopBottom())
fun Image.flipLeftRight() = copy(data = data.flipLeftRight())

fun List<String>.rotate(rotation: Int): List<String> {
    val size = size
    val halfSize = (size-1).toDouble() / 2.0
    assert(size == first().length)

    val make = { transform: (Double, Double) -> Pair<Double, Double> ->
        (0 until size).map { row ->
            (0 until size).joinToString("") { col ->
                val coords = transform(row.toDouble() - halfSize, col.toDouble() - halfSize)
                get((coords.first + halfSize).toInt())[(coords.second + halfSize).toInt()].toString()
            }
        }
    }

    return when (floorMod(rotation, 4)) {
        0 -> this
        1 -> make { row, col -> -col to row}
        2 -> make { row, col -> -row to -col}
        3 -> make { row, col -> col to -row}
        else -> throw Exception("")
    }
}

fun Image.rotate(rotation: Int) = copy(data=data.rotate(rotation))

fun getInput() = (aocDataDir / "day20.txt").useChunkedLines { chunks ->
    chunks.filter { !it.isEmpty() }.map { lines ->
        val id = lines.first().popPrefix("Tile ")!!.popSuffix(":")!!.toInt()
        Image(id, lines.subList(1, lines.size))
    }.toList()
}

fun String.canonicalize() = reversed().let { if (it < this) it else this }

typealias EdgeMap = Map<String, List<Pair<Image, Int>>>

fun List<Image>.edgeMap(): EdgeMap = asSequence().map { image ->
    image.edges.mapIndexed { index, edge ->
        Triple(image, edge.canonicalize(), index)
    }
}.flatten().groupBy(keySelector = { it.second }, valueTransform = { it.first to it.third })

fun EdgeMap.findCorners() = values.asSequence().filter { it.size % 2 == 1 }.flatten()
    .groupingBy { it.first }.eachCount().filter { it.value == 2 }.keys.also { assert(it.size == 4) }

fun EdgeMap.getCorrectImage(adjacentImage: Image, adjacentEdgeIndex: Int): Image {
    val matchingEdge = adjacentImage.edges[adjacentEdgeIndex]
    val desiredEdgeIndex = floorMod(adjacentEdgeIndex + 2, 4)

    var (currentImage, currentEdgeIndex) = getValue(matchingEdge.canonicalize()).let { (firstImage, secondImage) ->
        if (adjacentImage.id == firstImage.first.id) secondImage else firstImage
    }

    currentImage %= { rotate(desiredEdgeIndex - currentEdgeIndex) }

    if (matchingEdge != currentImage.edges[desiredEdgeIndex]) {
        when (adjacentEdgeIndex % 2) {
            0 -> currentImage %= { flipLeftRight() }
            1 -> currentImage %= { flipTopBottom() }
        }
    }
    assert(matchingEdge == currentImage.edges[desiredEdgeIndex])
    return currentImage
}

fun part1() = getInput().edgeMap().findCorners().fold(1L) { acc, image -> acc * image.id }

fun List<Image>.solveImage(): List<String> {
    val edgeMap = edgeMap()

    val firstEntry = generateSequence(edgeMap.findCorners().first()) { it.rotate(1) }.find {
        val isBorder = { i: Int -> edgeMap.getValue(it.edges[i].canonicalize()).size == 1 }
        isBorder(0) && isBorder(3)
    }!!

    val numSubImagesSide = sqrt(size.toDouble()).toInt()
    val subImageArray = List(numSubImagesSide) { mutableListOf<Image>() }
    subImageArray[0] += firstEntry

    for ((aboveRow, belowRow) in subImageArray.asSequence().windowed(2)) {
        belowRow += edgeMap.getCorrectImage(aboveRow.first(), 2)
    }

    for (row in subImageArray) {
        repeat(numSubImagesSide - 1) { _ ->
            row += edgeMap.getCorrectImage(row.last(), 1)
        }
    }
    val subImagesSize = subImageArray.first().first().data.size

    return subImageArray.map { subImageRow -> (1 until (subImagesSize -1)).map { subImageRowIndex ->
        subImageRow.joinToString("") { it.data[subImageRowIndex].substring(1, subImagesSize - 1) }
    }}.flatten()
}

fun part2(): Int {
    val solnImage = getInput().solveImage()
    val solnSize = solnImage.size
    val numSharps = solnImage.sumBy { line -> line.count { it == '#' } }
    val seaSharps = seaMonster.count { it == '#' }
    val images = (0..3).map { listOf(solnImage.rotate(it), solnImage.rotate(it).flipTopBottom())}.flatten()

    val seaLines = seaMonster.split("\n").filter { it.isNotEmpty() }
    val seaHeight = seaLines.size
    val seaWidth = seaLines.first().length
    assert(seaLines.all { it.length == seaWidth })

    return numSharps - seaSharps * images.maxOf {  image ->
        (0 until (solnSize - seaHeight)).sumOf { row ->
            (0 until (solnSize - seaWidth)).count { col ->
                (0 until seaHeight).all { r ->
                    (0 until seaWidth).all { c->
                        !(seaLines[r][c] == '#' && image[row + r][col + c] != '#')
                    }
                }
            }
        }
    }
}

fun main() {
    println(part2())
}