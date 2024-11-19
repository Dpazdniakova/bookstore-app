
import java.lang.System.exit
import controllers.API
import ie.setu.models.Author
import ie.setu.models.Book
import ie.setu.utils.*

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
                println(API.listAllBooks())

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
            0  -> {exitApp()
                println ("Exiting book creation")}
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

    val dateOfBirth = readNextDate()
    val genres= getGenres()
    val booksWritten= ArrayList<Book>()

    val isAdded = API.addAuthor(Author(authorId, name, country, dateOfBirth, genres, booksWritten))

  if (isAdded) {
      println("Successfully added ")

   }
    else {
        println("Add failed")
    }
}

fun addBook () {
    val bookId = bId
    bId++
    val title = readNextLine("Enter book title: ")
    val genre = readNextLine("Enter book's genre: ")
    var authorForObject: Author?

    do {
        var done = false
        println("Sub-Menu:")
        println("1. Pick existing author")
        println("2. Create new author")
        println("0. Exit")

        val option= readNextInt("Please, enter your option: ")
        when (option) {
            1  ->{ val result =addingBookWithExistingAuthor(genre)
                if (result!=null) {
                    authorForObject=result
                    val publicationYear = readValidYear("Please, enter publication year: ")
                    val price = readNextFloat("Please, enter price: ")
                    val isbn = readNextInt("Please, enter a 9-digit ISBN: ")
                    val newBook = Book(bookId, title, authorForObject, genre, publicationYear, price, isbn)
                    authorForObject.booksWritten.add(newBook)
                    API.addBook(newBook)
                    println ("Book successfully added")

                }
                else  {
                    println("No valid author was selected. Book creation is cancelled")
                }
                    done = true
            }
            2  -> {addAuthor()
            authorForObject=API.lastAddedAuthor()
                val publicationYear = readValidYear("Please, enter publication year: ")
                val price = readNextFloat("Please, enter price: ")
                val isbn = readNextInt("Please, enter a 9-digit ISBN: ")
                val newBook = Book(bookId, title, authorForObject, genre, publicationYear, price, isbn)
                authorForObject.booksWritten.add(newBook)
                API.addBook(newBook)
                println ("Book successfully added")
                done=true
            }
            0 -> runMenu()
            else -> println("Invalid option entered: $option ")

        }

    }
        while (option!=0 && done==false)

 }

fun addingBookWithExistingAuthor (genre: String): Author? {
    var result: Author? = null
    println(API.listAllAuthors())
    val Id = readNextInt("Please, enter Author's Id: ")
    if (API.validAuthorId(Id)) {
        val author = API.searchExistingAuthor(Id)
            if (author.genres.isNotEmpty()) {
                if (author.genres.any { it.equals(genre, ignoreCase = true) }) {
                    println("Author found.")
                    result = author
                } else {
                    println("The author you chose does not have the specified genre '$genre'.")
                }
            }
        else {
            result = author

        }
    }
    else {
        println ("Author ID not valid.")
    }
    return result
}

