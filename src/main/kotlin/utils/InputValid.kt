package ie.setu.utils

import controllers.API
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun readNextInt(prompt: String?): Int {
    do {
        try {
            print(prompt)
            val input = readln().trim()
            if (input.isBlank()) {
                System.err.println("Input cannot be blank. Please enter a number.")
                continue
            }
            return input.toInt()
        } catch (e: NumberFormatException) {
            System.err.println("Enter a number please.")
        }
    } while (true)
}

fun readNextFloat(prompt: String?): Float {
    do {
        try {
            print(prompt)
            val input = readln().trim()
            if (input.isBlank()) {
                System.err.println("Input cannot be blank. Please enter a number.")
                continue
            }
            return input.toFloat()
        } catch (e: NumberFormatException) {
            System.err.println("Enter a number please.")
        }
    } while (true)
}

fun readNextLine(prompt: String?): String {
    do {
        print(prompt)
        val input = readln().trim()
        if (input.isBlank()) {
            println("Input cannot be blank. Please try again.")
        } else {
            return input
        }
    } while (true)
}
fun readNextDate(): LocalDate? {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    while (true) {
        println("Please enter a date (yyyy-MM-dd):")
        val input = readLine()?.trim()
        if (input.isNullOrBlank()) {
            println("Input cannot be blank. Please try again.")
            continue
        }
        try {
            val date = LocalDate.parse(input, formatter)
            if (date.isAfter(LocalDate.now())) {
                println("The date cannot be in the future. Please try again.")
                continue
            }
            return date
        } catch (e: DateTimeParseException) {
            println("Invalid date format. Please enter the date in yyyy-MM-dd format.")
        }
    }
}

fun containsNumbers(input: String): Boolean {
    for (char in input) {
        if (char.isDigit()) {
            return true
        }
    }
    return false
}

