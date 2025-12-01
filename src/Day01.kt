import Day01.DialRotation.Direction

// https://adventofcode.com/2025/day/1
fun main() {
    val input = readInput(day = 1)
    val dialRotations = Day01.parseDialRotations(input)
    println("Part 1: ${Day01.Part1.calculateNumberRotationsResultingInZero(dialRotations)}")
    println("Part 2: ${Day01.Part2.calculateNumberRotationsHitZero(dialRotations)}")
}

private object Day01 {

    const val DialSize = 100

    object Part1 {
        fun calculateNumberRotationsResultingInZero(dialRotations: List<DialRotation>): Int {
            var currentRotation = 50
            var numZeroHits = 0

            dialRotations.forEach { rotation ->
                currentRotation = rotation.rotate(
                    start = currentRotation,
                    dialSize = DialSize,
                )
                if (currentRotation == 0) {
                    numZeroHits++
                }
            }
            return numZeroHits
        }
    }

    object Part2 {
        fun calculateNumberRotationsHitZero(dialRotations: List<DialRotation>): Int {
            var currentRotation = 50
            var numZeroHits = 0

            dialRotations.forEach { rotation ->
                repeat(rotation.distance) {
                    currentRotation = when (rotation.direction) {
                        Direction.Left -> (currentRotation - 1).mod(DialSize)
                        Direction.Right -> (currentRotation + 1).mod(DialSize)
                    }
                    if (currentRotation == 0) {
                        numZeroHits++
                    }
                }
            }
            return numZeroHits
        }
    }

    fun parseDialRotations(input: List<String>): List<DialRotation> = input.map(::parseDialRotation)

    fun parseDialRotation(line: String): DialRotation {
        val direction = when (val directionChar = line[0]) {
            'L' -> Direction.Left
            'R' -> Direction.Right
            else -> error("Unknown direction: $directionChar")
        }
        val distance = line.substring(startIndex = 1).toInt()
        return DialRotation(direction, distance)
    }

    data class DialRotation(
        val direction: Direction,
        val distance: Int,
    ) {

        fun rotate(start: Int, dialSize: Int): Int =
            when (direction) {
                Direction.Left -> (start - distance).mod(dialSize)
                Direction.Right -> (start + distance).mod(dialSize)
            }

        override fun toString(): String {
            val directionChar = when (direction) {
                Direction.Left -> 'L'
                Direction.Right -> 'R'
            }
            return "${directionChar}${distance}"
        }

        enum class Direction {
            Left,
            Right
        }
    }
}