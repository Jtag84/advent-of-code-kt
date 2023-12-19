# advent-of-code-kt

Welcome to the Advent of Code[^aoc] Kotlin project created by [jtag84][github] using the [Advent of Code Kotlin Template][template] delivered by JetBrains.

In this repository, jtag84 is about to provide solutions for the puzzles using [Kotlin][kotlin] language.

If you're stuck with Kotlin-specific questions or anything related to this template, check out the following resources:

- [Kotlin docs][docs]
- [Kotlin Slack][slack]
- Template [issue tracker][issues]

## How to create a new Day

A script `addDayFromTemplate.sh` can be used to create the skeleton for a new day, it will:

* Create a new dayXX package under the yearXXXX package
* Create a `Part1.kt`, `Part2.kt`, and `Parser.kt` from the templates in `src/template`
* Retrieve the inputs and test inputs from the [Advent of Code] [aoc] website and place them in the corresponding
  resources folder
* Update `AllParts.kt` with the newly added parts (this is used by the main app to run everything)

### `addDayFromTemplate.sh` usage

You must save your logged in [Advent of Code] [aoc] cookie in `cookieSession.txt` before running the script.

```shell
./addDayFromTemplate.sh YEAR DAY
```

#### Example:

```shell
./addDayFromTemplate.sh 2023 1
```

## How to run the solutions for each part

Each part comes with a main method, allowing you to run them individually directly from your IDE.

```kotlin
fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val part1 = part1(142) { lines ->
    lines
        .filter(String::isNotBlank)
        .sumOf {
            val leftDigit = getFirstDigitFromTheLeft(it)
            val rightDigit = getFirstDigitFromTheRight(it)
            leftDigit * 10 + rightDigit
        }
}

fun getFirstDigitFromTheLeft(line: String): Int {
    return line.first { it.isDigit() }.digitToInt()
}

fun getFirstDigitFromTheRight(line: String): Int {
    return line.last { it.isDigit() }.digitToInt()
}
```

Running this will give you some profiling time and memory estimates:

```shell

Part 1 test: Succeeded 
Parsing duration: 15.973959ms
Running duration: 884.292us
Total duration:  16.858251ms
Memory usage: 0 bytes

Part 1: 55108
Parsing duration: 4.145250ms
Running duration: 2.376416ms
Total duration:  6.521666ms
Memory usage: 0 bytes
```

You can also choose to run everything from the main method in `src/main/kotlin/App.kt` or with

```shell
./gradlew run
```

This will give you a summary of the passing tests and the profiling information for each part:

```shell
Year 2023:

 Day | Part | Test Result | Test Runtime | Test Memory | Parsing Time | Runtime Duration | Memory Used 
  01 |    1 |      Passed |    147.584us |     0 bytes |   3.935667ms |       1.997458ms |      491 KB
  01 |    2 |      Passed |   2.576250ms |     0 bytes |   1.358625ms |      20.413375ms |        3 MB
  02 |    1 |      Passed |    267.625us |     0 bytes |  10.561917ms |       1.058667ms |     0 bytes
  02 |    2 |      Passed |     79.083us |     0 bytes |   3.016542ms |        726.417us |     0 bytes
  03 |    1 |      Passed |   4.445959ms |        1 MB |  14.727833ms |      30.028750ms |        5 MB
  03 |    2 |      Passed |   3.096083ms |      394 KB |   7.918250ms |      25.625041ms |        8 MB
  04 |    1 |      Passed |      62.25us |     0 bytes |      4.799ms |         205.75us |     0 bytes
  04 |    2 |      Passed |   1.046250ms |     0 bytes |   2.418125ms |      19.774334ms |        2 MB
  05 |    1 |      Passed |     733.75us |     0 bytes |   1.469792ms |        248.667us |     0 bytes
  05 |    2 |      Passed |   1.689167ms |     0 bytes |    744.833us |      10.837583ms |        9 MB
  06 |    1 |      Passed |    609.375us |     0 bytes |     54.792us |        118.792us |     0 bytes
  06 |    2 |      Passed |   7.309125ms |        2 MB |     71.875us |     699.712375ms |      315 MB
  07 |    1 |      Passed |    964.875us |     0 bytes |  21.087791ms |      15.828916ms |       17 MB
  07 |    2 |      Passed |   4.027708ms |     0 bytes |  15.119708ms |      25.549417ms |       21 MB
  08 |    1 |      Passed |    876.208us |     0 bytes |   3.164709ms |       4.616041ms |        1 MB
  08 |    2 |      Passed |   1.993458ms |     0 bytes |   1.312375ms |         12.681ms |        4 MB
  09 |    1 |      Passed |    134.292us |     0 bytes |   2.782375ms |       4.177542ms |        1 MB
  09 |    2 |      Passed |    312.792us |     0 bytes |   1.484917ms |       2.039625ms |        1 MB
  10 |    1 |      Passed |   1.289750ms |     0 bytes |  39.084709ms |       6.799625ms |        1 MB
  10 |    2 |      Passed |    744.625us |     0 bytes |  32.025834ms |      64.260666ms |       65 MB
  11 |    1 |      Passed |   5.038833ms |     0 bytes |  30.857250ms |      35.489584ms |       20 MB
  11 |    2 |      Passed |    766.541us |     0 bytes |  20.869375ms |         14.603ms |       19 MB
  12 |    1 |      Passed |      2.762ms |     0 bytes |  16.687875ms |      12.910500ms |        5 MB
  12 |    2 |      Passed |    715.958us |     0 bytes |   8.704709ms |     232.357125ms |      160 MB
  13 |    1 |      Passed |    395.625us |     0 bytes |   1.567958ms |       4.149208ms |     0 bytes
  13 |    2 |      Passed |   2.020292ms |     0 bytes |   1.576334ms |      70.039375ms |       64 MB
  14 |    1 |      Passed |    970.334us |     0 bytes |  19.184042ms |       8.619208ms |        3 MB
  14 |    2 |      Passed |   7.110750ms |        2 MB |     11.211ms |        1.484088s |      848 MB
  15 |    1 |      Passed |     44.292us |     0 bytes |   3.995042ms |       1.296583ms |     0 bytes
  15 |    2 |      Passed |    570.667us |     0 bytes |   4.814417ms |       1.967333ms |     0 bytes
  16 |    1 |      Passed |    442.417us |     0 bytes |  15.537834ms |      15.762042ms |      129 MB
  16 |    2 |      Passed |    725.917us |     0 bytes |     11.457ms |     1.920097625s |        1 GB
  17 |    1 |      Passed |  11.661208ms |        4 MB |  17.000375ms |     516.829083ms |      607 MB
  17 |    2 |      Passed |   1.769292ms |       84 KB |  17.268833ms |     2.587369042s |      564 MB
  18 |    1 |      Passed |     62.083us |     0 bytes |     600.25us |        339.666us |     0 bytes
  18 |    2 |      Passed |    137.916us |     0 bytes |    391.708us |        834.042us |     0 bytes
  19 |    1 |      Passed |     70.291us |     0 bytes |   8.015750ms |       1.684875ms |        3 KB
  19 |    2 |      Passed |     101.75us |     0 bytes |   5.268083ms |       2.058917ms |      343 KB
```

[^aoc]:
    [Advent of Code][aoc] – An annual event of Christmas-oriented programming challenges started December 2015.
    Every year since then, beginning on the first day of December, a programming puzzle is published every day for twenty-five days.
    You can solve the puzzle and provide an answer using the language of your choice.

[aoc]: https://adventofcode.com
[docs]: https://kotlinlang.org/docs/home.html
[github]: https://github.com/jtag84
[issues]: https://github.com/kotlin-hands-on/advent-of-code-kotlin-template/issues
[kotlin]: https://kotlinlang.org
[slack]: https://surveys.jetbrains.com/s3/kotlin-slack-sign-up
[template]: https://github.com/kotlin-hands-on/advent-of-code-kotlin-template
