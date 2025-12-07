import com.soberg.aoc.utlities.datastructures.Grid2D
import com.soberg.aoc.utlities.datastructures.toGrid2D

// https://adventofcode.com/2025/day/7
fun main() {
    val input = readInput(day = 7)
    val manifold = Day07.parseTachyonManifold(input)
    println("Part 1: ${Day07.Part1.tachyonBeamSplitCount(manifold)}")
}

private object Day07 {

    object Part1 {
        fun tachyonBeamSplitCount(manifold: TachyonManifold): Int {
            val beamSet = mutableSetOf(manifold.startingPoint.move(Grid2D.Direction.South))
            var splitCount = 0
            while (beamSet.isNotEmpty()) {
                val currentLocation = beamSet.first().also { current ->
                    beamSet.remove(current)
                }
                if (!manifold.diagram.isInBounds(currentLocation)) continue

                val item = manifold.diagram[currentLocation]
                when {
                    item == TachyonManifold.Item.Splitter -> {
                        splitCount++
                        beamSet.add(currentLocation.move(Grid2D.Direction.SouthEast))
                        beamSet.add(currentLocation.move(Grid2D.Direction.SouthWest))
                    }

                    else -> beamSet.add(currentLocation.move(Grid2D.Direction.South))
                }
            }
            return splitCount
        }
    }


    fun parseTachyonManifold(input: List<String>): TachyonManifold {
        var startingPointLocation: Grid2D.Location? = null
        val grid = input.mapIndexed { row, line ->
            line.mapIndexed { col, char ->
                when (char) {
                    'S' -> {
                        startingPointLocation = Grid2D.Location(row, col)
                        TachyonManifold.Item.StartingPoint
                    }

                    '^' -> TachyonManifold.Item.Splitter
                    else -> TachyonManifold.Item.EmptySpace
                }
            }
        }.toGrid2D()
        if (startingPointLocation == null) {
            error("No starting point found in tachyon manifold")
        }
        return TachyonManifold(
            startingPoint = startingPointLocation,
            diagram = grid,
        )
    }

    data class TachyonManifold(
        val startingPoint: Grid2D.Location,
        val diagram: Grid2D<Item>,
    ) {

        enum class Item {
            StartingPoint,
            Splitter,
            EmptySpace,
        }
    }
}
