package year2021.day24

import commons.Part.Companion.part2

fun main() {
    part2.runAndPrint()
}
 /*
 Similar from part 1, we deduce the min with:
                min
w4 == w3 + 2    w3=1	w4=3
w8 == w7 + 5    w7=1	w8=6
w9 == w6 - 5    w6=6	w9=1
w11 == w10 + 1  w10=1	w11=2
w12 == w5 + 6   w5=1	w12=7
w13 == w2 + 7   w2=1	w13=8
w14 == w1 - 4   w1=5	w14=1

	w1	w2	w3	w4	w5	w6	w7	w8	w9	w10	w11	w12	w13	w14
min	5	1	1	3	1	6	1	6	1	1	2	7	8	1
  */
val part2 = part2(inputParser, null) { _ ->
     51131616112781
}