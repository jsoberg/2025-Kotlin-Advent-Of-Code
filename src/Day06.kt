import com.soberg.aoc.utlities.datastructures.Grid2D
import com.soberg.aoc.utlities.datastructures.toGrid2D
import com.soberg.aoc.utlities.extensions.asyncSumOfBlocking

// https://adventofcode.com/2025/day/6
fun main() {
    val input = readInput(day = 6)
    println("Part 1: ${Day06.Part1.sumColumnCalculations(input)}")
    println("Part 2: ${Day06.Part2.sumCephalopodColumnCalculations(input)}")
}

private object Day06 {
    object Part1 {
        fun sumColumnCalculations(input: List<String>): Long {
            val homework = parseMathHomework(input)
            return (0 until homework.colSize).asyncSumOfBlocking { col ->
                calculate(homework.column(col))
            }
        }

        private fun parseMathHomework(input: List<String>) = input.toGrid2D { line ->
            line.split("\\s+".toRegex())
                .filter { it.isNotBlank() }
                .map { item -> item.toMathItem() }
        }

        private fun calculate(column: List<MathItem>): Long {
            val operator = column.last() as MathItem.Operator
            var result = when (operator) {
                MathItem.Operator.Add -> 0L
                MathItem.Operator.Multiply -> 1L
            }
            column.dropLast(1).forEach { item ->
                val num = item as MathItem.Num
                result = when (operator) {
                    MathItem.Operator.Add -> result + num.value
                    MathItem.Operator.Multiply -> result * num.value
                }
            }
            return result
        }
    }

    object Part2 {
        fun sumCephalopodColumnCalculations(input: List<String>): Long {
            val homework = parseMathHomework(input)
            return (0 until homework.colSize).asyncSumOfBlocking { col ->
                calculate(homework.column(col))
            }
        }

        private fun parseMathHomework(input: List<String>): Grid2D<String> {
            val colSizes = parseColSizes(input)
            return input.map { line ->
                parseRow(line, colSizes)
            }.toGrid2D()
        }

        private fun parseColSizes(input: List<String>): List<Int> = buildList {
            val operatorsLine = input.last()
            var currentColSize = 1
            for (i in 1 until operatorsLine.length) {
                if (operatorsLine[i].isWhitespace()) {
                    currentColSize++
                } else {
                    add(currentColSize - 1)
                    currentColSize = 1
                }
            }
            // Since trailing whitespace is going to be trimmed, the last line needs to be handled separately.
            add(input.maxOf { line -> line.length - operatorsLine.length } + 1)
        }

        private fun parseRow(line: String, colSizes: List<Int>): List<String> = buildList {
            var currentIndex = 0
            colSizes.forEach { colSize ->
                val end = currentIndex + colSize
                if (end >= line.length) {
                    add(line.substring(currentIndex))
                } else {
                    add(
                        line.substring(
                            startIndex = currentIndex,
                            endIndex = currentIndex + colSize,
                        )
                    )
                }
                currentIndex += colSize + 1
            }
        }

        private fun calculate(column: List<String>): Long {
            val operator = column.last().trim().toMathItem() as MathItem.Operator
            var result = when (operator) {
                MathItem.Operator.Add -> 0L
                MathItem.Operator.Multiply -> 1L
            }

            val columnNums = column.dropLast(1)
            val maxCol = columnNums.maxOf { it.length } - 1
            for (i in maxCol downTo 0) {
                val number = parseNumberForColumn(columnNums, i)
                result = when (operator) {
                    MathItem.Operator.Add -> result + number
                    MathItem.Operator.Multiply -> result * number
                }
            }
            return result
        }

        private fun parseNumberForColumn(columnNums: List<String>, col: Int): Int =
            buildString {
                columnNums.forEach { number ->
                    if (number.length > col && number[col].isDigit()) {
                        append(number[col])
                    }
                }
            }.toInt()
    }

    private fun String.toMathItem(): MathItem = when (this) {
        "+" -> MathItem.Operator.Add
        "*" -> MathItem.Operator.Multiply
        else -> MathItem.Num(toInt())
    }

    sealed interface MathItem {

        @JvmInline
        value class Num(val value: Int) : MathItem {
            override fun toString(): String = value.toString()
        }

        enum class Operator : MathItem {
            Add,
            Multiply,
        }
    }
}