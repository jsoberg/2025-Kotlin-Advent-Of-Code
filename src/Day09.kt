import kotlin.math.abs

// https://adventofcode.com/2025/day/9
fun main() {
    val input = readInput(day = 9)
    val points = Day09.parsePoints(input)
    println("Part 1: ${Day09.Part1.largestRectangleArea(points)}")
}

private object Day09 {

    object Part1 {
        fun largestRectangleArea(points: List<Point2D>): Long {
            var largestArea = Long.MIN_VALUE
            for (i in 0 until points.size - 1) {
                for (j in i + 1 until points.size) {
                    val area = points[i].rectangleArea(points[j])
                    if (area > largestArea) {
                        largestArea = area
                    }
                }
            }
            return largestArea
        }
    }

    fun parsePoints(input: List<String>) = input.map(::parsePoint)

    private fun parsePoint(line: String): Point2D {
        val split = line.split(",")
        return Point2D(split[0].toInt(), split[1].toInt())
    }

    data class Point2D(
        val x: Int,
        val y: Int,
    ) {
        fun rectangleArea(other: Point2D): Long {
            // Same values (e.g. x == other.x) would still count an area of 1, so add 1
            val xDistance = (abs(x - other.x) + 1).toLong()
            val yDistance = (abs(y - other.y) + 1).toLong()
            return xDistance * yDistance
        }
    }
}