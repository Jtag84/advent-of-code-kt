package year2021.day19

import arrow.core.memoize
import arrow.core.tail
import commons.*
import commons.Part.Companion.part1

fun main() {
    part1.runAndPrintTest()
    part1.runAndPrint()
}

val oneYRotationSequence = sequenceOf({ c:Coordinates3d -> c.yRotate90CounterClockwise()})
val oneZRotationSequence = sequenceOf({ c:Coordinates3d -> c.zRotate90CounterClockwise()})
val threeXRotationSequence = sequenceOf({ c:Coordinates3d -> c.xRotate90CounterClockwise()}).repeat().take(3)
val threeYRotationSequence = oneYRotationSequence.repeat().take(3)
val threeZRotationSequence = oneZRotationSequence.repeat().take(3)
val rotationSequence = threeZRotationSequence + oneYRotationSequence +
        threeXRotationSequence + oneYRotationSequence +
        threeZRotationSequence + oneYRotationSequence +
        threeXRotationSequence + oneZRotationSequence +
        threeYRotationSequence + sequenceOf({ c:Coordinates3d -> c.zRotate90CounterClockwise().zRotate90CounterClockwise()}) +
        threeYRotationSequence

val part1 = part1(inputParser, 79) { scanners ->
    val (_, adjustedBeaconCoordinates) = memoizedGetAdjustedCoordinates(scanners)

    adjustedBeaconCoordinates.size
}

// memoizing just to speed up part 2 since it's the same calculations as part1
val memoizedGetAdjustedCoordinates = ::getAdjustedCoordinates.memoize()

fun getAdjustedCoordinates(scanners: List<Set<Coordinates3d>>): Pair<MutableSet<Coordinates3d>, MutableSet<Coordinates3d>> {
    val scannersToAdjust = scanners.tail().map { it to rotationSequence.repeat().iterator() }.toMutableList()
    val adjustedBeaconCoordinates = mutableSetOf(*scanners.get(0).toTypedArray())
    val scannerCoordinates = mutableSetOf(Coordinates3d(0, 0, 0))

    while (scannersToAdjust.isNotEmpty()) {
        val (beacons, rotationSequence) = scannersToAdjust.removeFirst()

        val overlappingBeacons = beacons.asSequence()
            .flatMap { beacon ->
                adjustedBeaconCoordinates.map { adjustedBeacon ->
                    Coordinates3d(
                        beacon.x - adjustedBeacon.x,
                        beacon.y - adjustedBeacon.y,
                        beacon.z - adjustedBeacon.z
                    )
                }
            }
            .map { scanner ->
                scanner to beacons.asSequence()
                    .map { Coordinates3d(it.x - scanner.x, it.y - scanner.y, it.z - scanner.z) }
            }
            .firstOrNull {
                it.second.filter { adjustedBeaconCoordinates.contains(it) }.take(12).toList().size == 12
            }

        if (overlappingBeacons != null) {
            scannerCoordinates.add(overlappingBeacons.first)
            adjustedBeaconCoordinates.addAll(overlappingBeacons.second)
        } else {
            val nextRotation = rotationSequence.next()
            val rotatedBeacons = beacons.map(nextRotation).toSet()
            scannersToAdjust.add(rotatedBeacons to rotationSequence)
        }
    }

    return scannerCoordinates to adjustedBeaconCoordinates
}