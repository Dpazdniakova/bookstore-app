
import ie.setu.utils.readNextInt
import java.lang.System.exit
import controllers.API
import ie.setu.models.Author
import ie.setu.models.Book
import ie.setu.utils.readNextDate
import ie.setu.utils.readNextLine

var aId =0
val bId=0
private val API = API()
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
          |   5) Add an Author             |
          |   6) List Authors              |
          |   7) Update an Author          |
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
                API.listAllAuthors()

            }
            3 -> {
                println("\nYou selected Update a Book")

            }
            4 -> {
                println("\nYou selected Delete a Book")

            }
            5 -> {
                addAuthor()
            }
            6 -> {
                API.listAllAuthors()
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
fun getGenres(): List<String> {
    val genres = mutableListOf<String>()
    var userWantsToContinue = true

    while (userWantsToContinue) {

        val genre = readNextLine("Enter a genre (type 'done' to finish): ").trim()

        if (genre.equals("done", ignoreCase = true))
            userWantsToContinue = false
        else if (genre.isNotBlank()) {
            genres.add(genre)
        }
    }

    return genres
}
fun addAuthor () {
    var authorId = aId
    aId++
    var name = readNextLine("Enter Author's name: ")
    var country= readNextLine("Enter Author's country of origin: ")
    var dateOfBirth = readNextDate()
    var genres= getGenres()
    var booksWritten= ArrayList<Book>()

    val isAdded = API.addAuthor(Author(authorId, name, country, dateOfBirth, genres, booksWritten))

  if (isAdded) {
      println("Successfully added ")

   }
    else {
        println("Add failed")
    }
}

