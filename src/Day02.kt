import Day02.Part1
import Day02.Part2

// https://adventofcode.com/2025/day/2
fun main() {
    val input = readInput(day = 2)
    val ranges = Day02.parseIdRanges(input.first())
    val part1Result = Day02.sumInvalidIds(ranges, Part1::isValidId)
    println("Part 1: $part1Result")
    val part2Result = Day02.sumInvalidIds(ranges, Part2::isValidId)
    println("Part 2: $part2Result")
}

private object Day02 {

    fun sumInvalidIds(ranges: List<LongRange>, isValidId: (Long) -> Boolean): Long =
        ranges.sumOf { range ->
            range.filter { id -> !isValidId(id) }.sum()
        }

    object Part1 {
        fun isValidId(id: Long): Boolean {
            val stringRep = id.toString()
            // An ID that can't be split evenly (e.g. 5252) can't be repeating
            if (stringRep.length % 2 != 0) {
                return true
            }

            val firstHalf = stringRep.take(stringRep.length / 2)
            val secondHalf = stringRep.substring(startIndex = stringRep.length / 2)
            return firstHalf != secondHalf
        }
    }

    object Part2 {
        fun isValidId(id: Long): Boolean {
            val stringRep = id.toString()
            for (i in 1..stringRep.length / 2) {
                val group = stringRep.take(i)
                val combined = StringBuilder().apply {
                    while (length < stringRep.length) {
                        append(group)
                    }
                }.toString()
                if (combined == stringRep) {
                    return false
                }
            }
            return true
        }
    }

    fun parseIdRanges(input: String): List<LongRange> = input.split(",").map { range ->
        val rangeSplit = range.split("-")
        LongRange(
            start = rangeSplit.first().toLong(),
            endInclusive = rangeSplit.last().toLong(),
        )
    }
}
