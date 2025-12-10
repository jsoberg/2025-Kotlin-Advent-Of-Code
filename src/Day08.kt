import kotlin.math.sqrt

// https://adventofcode.com/2025/day/8
fun main() {
    val input = readInput(day = 8)
    val points = Day08.parsePoints(input)
    println("Part 1: ${Day08.Part1.threeLargestCircuitsMultiplied(points)}")
    println("Part 2: ${Day08.Part2.xProductLastTwoJunctionBoxes(points)}")
}

private object Day08 {

    private fun findDistances(points: List<Point3D>): Map<Pair<Point3D, Point3D>, Double> {
        val pointPairToDistanceMap = mutableMapOf<Pair<Point3D, Point3D>, Double>()
        for (i in 0 until points.size - 1) {
            for (j in i + 1 until points.size) {
                val first = points[i]
                val second = points[j]
                pointPairToDistanceMap[first to second] = first.distanceTo(second)
            }
        }
        return pointPairToDistanceMap
    }

    private fun <K, V> swapMap(map: Map<K, V>): Map<V, K> {
        val valueToKeyMap = mutableMapOf<V, K>()
        map.forEach { (key, value) ->
            valueToKeyMap[value] = key
        }
        return valueToKeyMap
    }

    object Part1 {
        fun threeLargestCircuitsMultiplied(points: List<Point3D>): Int {
            val pointPairToDistanceMap = findDistances(points)
            val circuits = findCircuits(pointPairToDistanceMap, 1000)
            val threeLargestCircuits = circuits
                .sortedByDescending { it.size }
                .take(3)
            return threeLargestCircuits[0].size * threeLargestCircuits[1].size * threeLargestCircuits[2].size
        }

        private fun findCircuits(
            connectionToDistanceMap: Map<Pair<Point3D, Point3D>, Double>,
            maxConnections: Int,
        ): List<Set<Point3D>> {
            val connectablePairs = swapMap(connectionToDistanceMap)
                .toSortedMap()
                .values
                .take(maxConnections)
                .toMutableList()

            val circuits = mutableListOf<MutableSet<Point3D>>()
            while (connectablePairs.isNotEmpty()) {
                val (first, second) = connectablePairs.removeFirst()
                val firstCircuit = circuits.firstOrNull { first in it }
                val secondCircuit = circuits.firstOrNull { second in it }

                when {
                    // Brand new circuit
                    firstCircuit == null && secondCircuit == null -> {
                        circuits.add(mutableSetOf(first, second))
                    }
                    // Same circuit, these two points are already connected
                    firstCircuit == secondCircuit -> Unit
                    // We have connections in both circuits and need to combine them
                    firstCircuit != null && secondCircuit != null -> {
                        circuits.remove(secondCircuit)
                        firstCircuit.addAll(secondCircuit)
                    }

                    firstCircuit != null -> firstCircuit += second
                    secondCircuit != null -> secondCircuit += first
                }
            }
            return circuits
        }
    }

    object Part2 {
        fun xProductLastTwoJunctionBoxes(points: List<Point3D>): Int {
            val pointPairToDistanceMap = findDistances(points)
            val (penultimate, last) = findLastPair(points, pointPairToDistanceMap)
            return penultimate.x * last.x
        }

        private fun findLastPair(
            points: List<Point3D>,
            connectionToDistanceMap: Map<Pair<Point3D, Point3D>, Double>
        ): Pair<Point3D, Point3D> {
            val connectablePairs = swapMap(connectionToDistanceMap)
                .toSortedMap()
                .values
                .toMutableList()

            var lastPair = connectablePairs.first()
            // Pre-fill all one-point circuits so that we can react when this becomes a single circuit.
            val circuits = points.map { mutableSetOf(it) }.toMutableList()
            while (connectablePairs.isNotEmpty() && circuits.size > 1) {
                lastPair = connectablePairs.removeFirst()
                val (first, second) = lastPair
                val firstCircuit = circuits.firstOrNull { first in it }
                val secondCircuit = circuits.firstOrNull { second in it }

                when {
                    // Brand new circuit
                    firstCircuit == null && secondCircuit == null -> {
                        circuits.add(mutableSetOf(first, second))
                    }
                    // Same circuit, these two points are already connected
                    firstCircuit == secondCircuit -> Unit
                    // We have connections in both circuits and need to combine them
                    firstCircuit != null && secondCircuit != null -> {
                        circuits.remove(secondCircuit)
                        firstCircuit.addAll(secondCircuit)
                    }

                    firstCircuit != null -> firstCircuit += second
                    secondCircuit != null -> secondCircuit += first
                }
            }
            return lastPair
        }
    }

    fun parsePoints(input: List<String>) = input.map(::parsePoint)

    private fun parsePoint(line: String): Point3D {
        val pointValues = line.split(',')
        if (pointValues.size != 3) {
            error("Invalid point parsed: $line")
        }
        return Point3D(
            x = pointValues[0].toInt(),
            y = pointValues[1].toInt(),
            z = pointValues[2].toInt(),
        )
    }

    data class Point3D(
        val x: Int,
        val y: Int,
        val z: Int,
    ) {
        fun distanceTo(other: Point3D): Double {
            val dx = x.toDouble() - other.x.toDouble()
            val dy = y.toDouble() - other.y.toDouble()
            val dz = z.toDouble() - other.z.toDouble()
            return sqrt((dx * dx) + (dy * dy) + (dz * dz))
        }

        override fun toString(): String = "[$x,$y,$z]"
    }
}