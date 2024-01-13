package year2020.day20

import arrow.core.tail
import cc.ekblad.konbini.*
import commons.Lines
import commons.newLine
import commons.parseLines
import commons.rotateClockwise

data class Tile(val id: Long, private val fullImage : List<String>,
                var top: String, var right:String, var bottom:String, var left: String,
                var topConnectedTo: Tile? = null, var rightConnectedTo:Tile? = null, var bottomConnectedTo:Tile? = null, var leftConnectedTo: Tile? = null,
    ) {

    private var orientationCount = 1

    fun getFullImageWithoutBorders() : List<String> {
        var finalImage = fullImage.tail().dropLast(1).map { it.substring(1, it.length - 1) }

        var appliedOrientationCount = 1

        while(appliedOrientationCount != orientationCount) {
            if(appliedOrientationCount % 4 == 0) {
                finalImage = finalImage.map { it.reversed() }
            } else {
                finalImage = finalImage.rotateClockwise()
            }

            appliedOrientationCount++
        }

        return finalImage
    }

    fun nextFlipOrRotation() {
        if(orientationCount % 4 == 0) {
            // flip horizontal
            top = top.reversed()
            bottom = bottom.reversed()
            val previousRight = right
            right = left.reversed()
            left = previousRight.reversed()
        }
        else {
            // rotate clockwise
            val previousTop = top
            top = left
            left = bottom
            bottom = right
            right = previousTop
        }
        orientationCount++
    }

    override fun toString(): String {
        return "Tile(id=$id, top='$top', right='$right', bottom='$bottom', left='$left', topConnectedTo=${topConnectedTo?.id}, rightConnectedTo=${rightConnectedTo?.id}, bottomConnectedTo=${bottomConnectedTo?.id}, leftConnectedTo=${leftConnectedTo?.id})"
    }

    companion object {
        fun toTile(lines :List<String>) : Tile {
            val id = lines.first().drop(5).dropLast(1).toLong()
            val top = lines[1]
            val bottom = lines[10].reversed()
            val fullImage = lines.tail()
            val left = fullImage.joinToString("") { it.first().toString() }.reversed()
            val right = fullImage.joinToString("") { it.last().toString() }
            return Tile(id, fullImage, top, right, bottom, left)
        }
    }
}

val inputParser: Parser<List<Tile>> = parser {
    val lines= parseLines()
    lines.chunked(12){ Tile.toTile(it.filterNot { it.isBlank() })}
}