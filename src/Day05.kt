import com.soberg.aoc.utlities.extensions.asyncCountBlocking
import kotlin.math.max
import kotlin.math.min

// https://adventofcode.com/2025/day/5
fun main() {
    val input = readInput(day = 5)
    val ingredientDatabase = Day05.parseIngredientDatabase(input)
    println("Part 1: ${Day05.Part1.sumAvailableFreshIngredientIds(ingredientDatabase)}")
    println("Part 2: ${Day05.Part2.totalFreshIngredientIds(ingredientDatabase)}")
}


private object Day05 {

    object Part1 {
        fun sumAvailableFreshIngredientIds(database: IngredientDatabase) =
            database.availableIds.asyncCountBlocking { id ->
                isIdFresh(id, database)
            }

        private fun isIdFresh(id: Long, database: IngredientDatabase): Boolean =
            database.freshIdRanges.any { range ->
                id in range
            }
    }

    object Part2 {
        fun totalFreshIngredientIds(database: IngredientDatabase): Long =
            buildList {
                for (range in database.freshIdRanges) {
                    merge(range)
                }
            }.sumOf { range -> (range.last - range.first) + 1 }

        private fun MutableList<LongRange>.merge(range: LongRange) {
            var mergedRange = range
            for (i in indices.reversed()) {
                if (mergedRange.overlaps(this[i])) {
                    mergedRange = mergedRange.merge(this[i])
                    removeAt(i)
                }
            }
            add(mergedRange)
        }

        private fun LongRange.overlaps(other: LongRange): Boolean =
            first <= other.last && last >= other.first

        private fun LongRange.merge(other: LongRange): LongRange =
            min(first, other.first)..max(last, other.last)
    }

    fun parseIngredientDatabase(input: List<String>): IngredientDatabase {
        val freshIdRanges = mutableListOf<LongRange>()
        val availableIds = mutableListOf<Long>()
        for (line in input) {
            when {
                line.isBlank() -> continue
                line.contains('-') -> {
                    val split = line.split('-')
                    freshIdRanges.add(
                        LongRange(
                            start = split.first().toLong(),
                            endInclusive = split.last().toLong()
                        )
                    )
                }

                else -> availableIds.add(line.toLong())
            }
        }
        return IngredientDatabase(freshIdRanges, availableIds)
    }

    data class IngredientDatabase(
        val freshIdRanges: List<LongRange>,
        val availableIds: List<Long>,
    ) {
        override fun toString(): String = buildString {
            for (range in freshIdRanges) {
                appendLine(range)
            }
            appendLine()
            for (id in availableIds) {
                appendLine(id)
            }
        }
    }
}