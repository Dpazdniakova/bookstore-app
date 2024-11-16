package ie.setu.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun readNextInt(prompt: String?): Int {
    do {
        try {
            print(prompt)
            return readln().toInt()
        } catch (e: NumberFormatException) {
            System.err.println("\tEnter a number please.")
        }
    } while (true)
}

fun readNextFloat(prompt: String?): Float {
    do {
        try {
            print(prompt)
            return readln().toFloat()
        } catch (e: NumberFormatException) {
            System.err.println("\tEnter a number please.")
        }
    } while (true)
}

fun readNextLine(prompt: String?): String {
    print(prompt)
    return readln()
}
fun readNextDate(): LocalDate? {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    while (true) {
        println("Please enter a date (yyyy-MM-dd):")
        val input = readLine()
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