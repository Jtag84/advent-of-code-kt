package year2023.day24

import commons.Part.Companion.part2
import org.ejml.simple.SimpleMatrix
import kotlin.math.roundToLong

fun main() {
    part2.runAndPrintTest()
    part2.runAndPrint()
}

val part2 = part2(inputParser, 47L) { positionVectors ->

    val matrixXYAndXZRows = positionVectors.zipWithNext()
        .map {(positionVector1, positionVector2) ->
            val (fx1, fy1, fz1, vx1, vy1, vz1) = positionVector1
            val (fx2, fy2, fz2, vx2, vy2, vz2) = positionVector2

            // vy fx - vx fy + vx' fy' - vy' fx'
            val constantXY = vy1 * fx1 - vx1 * fy1 + vx2 * fy2 - vy2 * fx2
            val constantXZ = vz1 * fx1 - vx1 * fz1 + vx2 * fz2 - vz2 * fx2

            (toMatrixRow(vx1, fx1, vx2, fx2, vy1, fy1, vy2, fy2) to doubleArrayOf(constantXY.toDouble())) to (toMatrixRow(vx1, fx1, vx2, fx2, vz1, fz1, vz2, fz2) to doubleArrayOf(constantXZ.toDouble()))
        }
        .take(4)

    val matrixXYCoefficients = SimpleMatrix(arrayOf(*matrixXYAndXZRows.map{it.first.first}.toTypedArray()))
    val matrixXYConstants = SimpleMatrix(arrayOf(*matrixXYAndXZRows.map{it.first.second}.toTypedArray()))

    val matrixXZCoefficients = SimpleMatrix(arrayOf(*matrixXYAndXZRows.map{it.second.first}.toTypedArray()))
    val matrixXZConstants = SimpleMatrix(arrayOf(*matrixXYAndXZRows.map{it.second.second}.toTypedArray()))

    val resultXYMatrix = matrixXYCoefficients.solve(matrixXYConstants)
    val resultXZMatrix = matrixXZCoefficients.solve(matrixXZConstants)

//    val ax = resultXYMatrix[0]
    val bx = resultXYMatrix[1]
//    val ay = resultXYMatrix[2]
    val by = resultXYMatrix[3]
//    val az = resultXZMatrix[2]
    val bz = resultXZMatrix[3]

    bx.roundToLong() + by.roundToLong() + bz.roundToLong()
}

// x = at1 + b
// x = v1t1 + f1
// at1 + b = v1t1 + f1
// at1 + b - v1t1 - f1 = 0
// t1(a - v1) + b - f1
// b = v1t1 + f1 - at1
// v2t2 + f2 = at2 + b
// v2t2 + f2 = at2 + v1t1 + f1 - at1
// v2t2 + f2 - v1t1 - f1 = a(t2 - t1)
// a = (v2t2 + f2 -v1t1 - f1) / (t2 - t1)
//
// at + b = vt + f
// t(a - v) = f - b
// t = (f-b) / (a-v)

// t = (fx - bx) / (ax-vx)
// t = (fy - by) / (ay-vy)
// (fx - bx) / (ax-vx) = (fy - by) / (ay-vy)
// (fx - bx) (ay-vy) = (fy - by) (ax-vx)
// ay fx - vy fx - bx ay + bx vy = ax fy - vx fy - ax by + by vx
// ax by - bx ay = ax fy - vx fy + by vx - ay fx + vy fx - bx vy
// ax fy' - vx' fy' + by vx' - ay fx' + vy' fx' - bx vy' = ax fy - vx fy + by vx - ay fx + vy fx - bx vy
// ax (fy' - fy) - vx' fy' + by (vx' - vx) + ay ( fx - fx') + vy' fx' + bx (vy - vy') = vy fx - vx fy
// ax (fy' - fy) + bx (vy - vy') + ay ( fx - fx') + by (vx' - vx) = vy fx - vx fy + vx' fy' - vy' fx'

fun toMatrixRow(vx1: Long, fx1: Long, vx2:Long, fx2: Long, vy1: Long, fy1: Long, vy2:Long, fy2: Long) : DoubleArray {
    return doubleArrayOf(fy2 - fy1.toDouble(), vy1 - vy2.toDouble(), fx1 - fx2.toDouble(), vx2 - vx1.toDouble())
}