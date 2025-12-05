import com.soberg.aoc.utlities.datastructures.Grid2D
import com.soberg.aoc.utlities.datastructures.toGrid2D

// https://adventofcode.com/2025/day/4
fun main() {
    val input = readInput(day = 4)
    val grid = Day04.parseGrid(input)
    println("Part 1: ${Day04.Part1.numAccessibleRolls(grid)}")
    println("Part 1: ${Day04.Part2.numAccessibleRollsWithIteration(grid)}")
}

private object Day04 {

    private fun isAccessiblePaperRoll(
        grid: Grid2D<PaperRoll?>,
        location: Grid2D.Location
    ): Boolean {
        if (grid[location] != PaperRoll) return false
        val neighborsWithPaperRolls =
            grid.neighbors(location, directions = Grid2D.Direction.entries)
                .sumOf { neighborLoc ->
                    if (grid[neighborLoc] == PaperRoll) 1 else 0
                }
        return neighborsWithPaperRolls < 4
    }

    object Part1 {
        fun numAccessibleRolls(grid: Grid2D<PaperRoll?>): Int =
            grid.count { location, _ ->
                isAccessiblePaperRoll(grid, location)
            }
    }

    object Part2 {
        fun numAccessibleRollsWithIteration(grid: Grid2D<PaperRoll?>): Int {
            var currentGrid = grid
            var removedRollCount = 0
            var accessibleRollLocations = accessibleRollLocations(currentGrid)
            while (accessibleRollLocations.isNotEmpty()) {
                removedRollCount += accessibleRollLocations.size
                currentGrid = withRemovedRolls(currentGrid, accessibleRollLocations)

                accessibleRollLocations = accessibleRollLocations(currentGrid)
            }
            return removedRollCount
        }

        private fun accessibleRollLocations(grid: Grid2D<PaperRoll?>): List<Grid2D.Location> =
            buildList {
                grid.traverse { location ->
                    if (isAccessiblePaperRoll(grid, location)) {
                        add(location)
                    }
                }
            }

        private fun withRemovedRolls(grid: Grid2D<PaperRoll?>, locations: List<Grid2D.Location>) =
            grid.modify { mutableGrid ->
                for (location in locations) {
                    mutableGrid[location.row][location.col] = null
                }
            }
    }

    fun parseGrid(input: List<String>): Grid2D<PaperRoll?> = input.toGrid2D { line ->
        line.map { char ->
            when (char) {
                '@' -> PaperRoll
                else -> null
            }
        }
    }

    data object PaperRoll
}