
import ie.setu.utils.readNextInt
import java.lang.System.exit
import controllers.API
import ie.setu.models.Author
import ie.setu.models.Book
import ie.setu.utils.readNextDate
import ie.setu.utils.readNextLine
import ie.setu.utils.containsNumbers
var aId =0
var bId=0
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
               addBook()

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
            5 -> {
                addAuthor()
            }
            6 -> {
                println(API.listAllAuthors())
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
        else if (genre.isNotBlank() && !containsNumbers(genre)) {
            genres.add(genre)
        }
        else {
            println("Genre can't be blank or contain numbers.Please, re-enter")
        }
    }

    return genres
}


fun addAuthor () {
    val authorId = aId
    aId++
    var name : String

    do {
        name = readNextLine("Enter Author's name:")
        if (containsNumbers(name)) {
            println("Name can't contain numbers. Please, re-enter")
        }
    } while (containsNumbers(name) || name.isBlank())

    var country: String
    do {
        country = readNextLine("Enter Author's country of origin: ")
        if (containsNumbers(country) || name.isBlank() ) {
            println("Country can't contain numbers. Please, re-enter")
        }
    } while (containsNumbers(country) || name.isBlank() )

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

fun addBook () {
    println("You've chosen to add a book")
//    var bookId = bId
//    bId++
//    var title = readNextLine("Enter book title: ")
//    var genre = readNextLine("Enter book's genre: ")
//    var author: Author
//
//    do {
//        var switch = true
//        println("Sub-Menu:")
//        println("1. Pick existing author")
//        println("2. Create new author")
//        println("0. Exit")
//        val option= readNextInt("Please, enter your option: ")
//        when (option) {
//            1  ->  { println(API.listAllAuthors())
//               val Id= readNextInt("Please, enter Author's Id: ")
//             API.addingBookWithExistingAuthor(Id)
//
//            }
//            2  -> addAuthor()
//            0 -> runMenu()
//            else -> println("Invalid option entered: $option ")
//
//        }
//
//
//
//    }
//        while (switch)
//    var publicationYear
//    var price
//    var isbn


}