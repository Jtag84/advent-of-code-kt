package year2023.day24

import arrow.core.Tuple4
import com.google.common.collect.Sets
import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(inputParser, 2) { positionVectors ->
    val validRange = if(isTest) {
        7.0..27.0
    }
    else {
        200000000000000.0..400000000000000.0
    }

    val positionVectorSet = positionVectors.toSet()
    Sets.combinations(positionVectorSet, 2).asSequence()
        .mapNotNull { positionVectorCombination ->
            check(positionVectorCombination.size == 2)
            calculateIntersection(positionVectorCombination.first(), positionVectorCombination.last())
        }
        .count { (x, y, t1, t2) -> x in validRange && y in validRange && t1 >= 0.0 && t2 >= 0.0}
}

//Ox + Vxt = x
//t = (x - Ox) / Vx
//
//y = Oy + Vyt
//y = Oy + Vy(x - Ox) / Vx
//y = Oy + x * Vy / Vx - VyOx / Vx
//y = x * Vy / Vx + (Oy - Vy * Ox / Vx)

// a1 * x + b1 = a2 * x + b2
// a1 * x - a2 * x = b2 - b1
// x(a1 -a2) = b2 - b1
// x = (b2 -b1) / (a1 - a2)

//19 -2t = x
//(19 - x)/2 = t
//y = 13 + t
//y = 13 + 19/2 - x/2
//y = 22.5 - x/2
//
//x = 18 -t
//t = 18 - x
//y = 19 -t
//y = 19 - 18 + x
//y = 1+x
//
//1 + x = 22.5 -x/2
//3x/2 = 21.5
//x = 14.333
//y = 15.333

fun calculateIntersection(first: PositionVector, second: PositionVector) : Tuple4<Double, Double, Double, Double>?{
    val (x1, y1, _ , vx1, vy1, _) = first
    val (x2, y2, _ , vx2, vy2, _) = second

    if(vx1 == 0L || vx2 == 0L) {
        return null
    }

    val a1 : Double = vy1 / vx1.toDouble()
    val b1 : Double = y1 - vy1 * x1 / vx1.toDouble()
    val a2 : Double = vy2 / vx2.toDouble()
    val b2 : Double = y2 - vy2 * x2 / vx2.toDouble()

    if((a1 - a2) == 0.0) {
        return null
    }

    val x:Double = (b2 - b1) / (a1 - a2)
    val y: Double = a1 * x + b1

    val t1 = (x - x1) / vx1
    val t2 = (x - x2) / vx2
    return Tuple4(x, y, t1, t2)
}