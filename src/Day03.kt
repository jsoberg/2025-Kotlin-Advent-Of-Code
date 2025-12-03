import Day03.Battery
import com.soberg.aoc.utlities.extensions.asyncSumOfBlocking

// https://adventofcode.com/2025/day/3
fun main() {
    val input = readInput(day = 3)
    val banks = Day03.parseBatteryBanks(input)
    println("Part 1: ${Day03.Part1.totalOutputJoltage(banks)}")
    println("Part 2: ${Day03.Part2.totalOutputJoltage(banks)}")
}

typealias BatteryBank = List<Battery>

object Day03 {

    fun totalOutputJoltage(banks: List<BatteryBank>, numDigits: Int) =
        banks.asyncSumOfBlocking { bank ->
            outputJoltage(bank, numDigits)
        }

    fun outputJoltage(bank: BatteryBank, numDigits: Int): Long {
        var checkableBank = bank
        return buildString {
            for (digit in (0 until numDigits)) {
                // Find the largest battery that could be part of the result (front of the list).
                val max = checkableBank.dropLast(numDigits - digit - 1).maxBy { it.joltage }
                append(max)
                // Drop everything before (and including) this battery, and check for the next digit.
                checkableBank = checkableBank.drop(checkableBank.indexOf(max) + 1)
            }
        }.toLong()
    }

    object Part1 {
        fun totalOutputJoltage(banks: List<BatteryBank>) = totalOutputJoltage(banks, numDigits = 2)
    }

    object Part2 {
        fun totalOutputJoltage(banks: List<BatteryBank>) = totalOutputJoltage(banks, numDigits = 12)
    }

    fun parseBatteryBanks(input: List<String>) = input.map { line ->
        line.map { char -> Battery(char.digitToInt()) }
    }

    @JvmInline
    value class Battery(val joltage: Int) {
        override fun toString(): String = "$joltage"
    }
}
