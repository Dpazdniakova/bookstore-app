package ie.setu

import ie.setu.utils.readNextInt
import java.lang.System.exit

fun main() {
    runMenu()
}

fun mainMenu() : Int {
    print("""
          ----------------------------------
          |        Book Store System       |
          ----------------------------------
          |   1) Add a Book                |
          |   2) List Books                |
          |   3) Update a Book             |
          |   4) Delete a Book             |
          |   0) Exit                      |
           --------------------------------
          """.trimMargin(">"))
     return readNextInt("\nPlease select an option 0-4: ")
}

fun runMenu() {
    do {
        val option= mainMenu()
        when (option) {
            1 -> {
                println("\nYou selected Add a Book")

            }
            2 -> {
                println("\nYou selected List Books")

            }
            3 -> {
                println("\nYou selected Update a Book")

            }
            4 -> {
                println("\nYou selected Delete a Book")

            }
            0  -> exitApp()
            else -> println("Invalid option entered: $option ")

        }


    } while (true)

}

fun exitApp(){
    println("Exiting")
    exit(0)
}