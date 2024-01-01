package year2021.day24

import commons.Part.Companion.part1

fun main() {
    part1.runAndPrint()
}

/*
For each input the code if of the form:
inp w
mul x 0
add x z
mod x 26
div z D
add x P
eql x w
eql x 0
mul y 0
add y 25
mul y x
add y 1
mul z y
mul y 0
add y w
add y L
mul y x
add z y

Which is:
    if w != (z%26 + P)
    then
           z = z/D * 26 + (w+L)
    else
           z = z/D

From anaylizing the inputs we get:
D	P	L		            condition
1	1	11	8	w1 + 8		                (w1+8)
2	1	14	13	w2 + 13		                (w1+8)	(w2+13)
3	1	10	2	w3 + 2		                (w1+8)	(w2+13)	(w3 + 2)
4	26	0	7	w4 + 7	    w4 == w3 + 2	(w1+8)	(w2+13)
5	1	12	11	w5 + 11		                (w1+8)	(w2+13)	(w5 + 11)
6	1	12	4	w6 + 4		                (w1+8)	(w2+13)	(w5 + 11)	(w6 + 4)
7	1	12	13	w7 + 13		                (w1+8)	(w2+13)	(w5 + 11)	(w6 + 4)	(w7 + 13)
8	26	-8	13	w8 + 13	    w8==w7+13-8	    (w1+8)	(w2+13)	(w5 + 11)	(w6 + 4)
9	26	-9	10	w9 + 10	    w9==w6+4-9	    (w1+8)	(w2+13)	(w5 + 11)
10	1	11	1	w10 + 1		                (w1+8)	(w2+13)	(w5 + 11)	(w10+1)
11	26	0	2	w11 + 2	    w11==w10+1	    (w1+8)	(w2+13)	(w5 + 11)
12	26	-5	14	w12 + 14    w12==w5+11-5	(w1+8)	(w2+13)
13	26	-6	6	w13 + 6	    w13==w2+13-6	(w1+8)
14	26	-12	14	w14 + 14	w14==w1+8-12

From which we deduce:
	            max
w4 == w3 + 2	w3 = 7	    w4 = 9
w8 == w7 + 5	w7 = 4	    w8 = 9
w9 == w6 - 5	w6 = 9	    w9 = 4
w11 == w10 + 1	w10 = 8	    w11 = 9
w12 == w5 + 6	w5 = 3	    w12 = 9
w13 == w2 + 7	w2 = 2	    w13 = 9
w14 == w1 - 4	w1 = 9	    w14 = 5

	w1	w2	w3	w4	w5	w6	w7	w8	w9	w10	w11	w12	w13	w14
max	9	2	7	9	3	9	4	9	4	8	9	9	9	5
 */

val part1 = part1(inputParser, null) { _ ->
    9273949489995
}