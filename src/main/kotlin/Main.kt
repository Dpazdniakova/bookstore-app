
import java.lang.System.exit
import controllers.API
import ie.setu.models.Author
import ie.setu.models.Book
import ie.setu.utils.*
import java.time.LocalDateTime

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
          |   8) Delete an Author          |
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
                listingBooks()

            }
            3 -> {
             updateBook()
            }
            4 -> {
                deletingBook ()
            }
            5 -> {
                addAuthor()
            }
            6 -> {
               listingAuthors()
            }
            7 -> {
                updateAuthor()
            }
            8 -> {
                deletingAuthor()
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
fun getGenres(): MutableList<String> {
    val genres = mutableListOf<String>()
    var userWantsToContinue = true

    while (userWantsToContinue) {

        val genre = readNextLine("Enter a genre (type 'done' to finish): ")

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
    if (!API.listAllAuthors().contains("No authors")) {
        val Id = readNextInt("Please, enter Author's Id: ")
        if (API.validAuthorId(Id) != null) {
            val author = API.searchExistingAuthor(Id)
            if (author.genres.isNotEmpty()) {
                if (author.genres.any { it.equals(genre, ignoreCase = true) }) {
                    println("Author found.")
                    result = author
                } else {
                    println("The author you chose does not have the specified genre '$genre'.")
                }
            } else {
                result = author

            }
        } else {
            println("Author ID not valid.")
        }
    }
    return result
}

fun deletingBook () {
    println(API.listAllBooks())
    val Id = readNextInt("Please, enter the ID of the book you want to delete: ")
    val result=API.deleteBook(Id)
    if ( !result)  {
        println("Deletion successful.")
    }
    else {
        println("Deletion unsuccessful.")
    }

}

fun deletingAuthor() {
    println(API.listAllAuthors())
    val Id = readNextInt("Please, enter the ID of the author you want to delete: ")
    val result=API.deleteAuthor(Id)
    if ( !result)  {
        println("Deletion successful.")
    }
    else {
        println("Deletion unsuccessful.")
    }
}

fun updateBook() {
    if (!API.listAllBooks().contains("No books", ignoreCase = true)) {
        println(API.listAllBooks())
        val idToUpdate = readNextInt("Enter the ID of the book you want to update: ")
        val bookToUpdate = API.validBookId(idToUpdate)

        if (bookToUpdate != null) {
            var option: Int
            do {
                println("""
                    What would you like to update?
                    1. Price
                    2. Genre
                    3. Publication Year
                    4. ISBN
                    5. Exit
                """.trimIndent())
                option = readNextInt("Enter your choice: ")

                when (option) {
                    1 -> {
                        val newPrice = readNextFloat("Enter new price: ")
                        bookToUpdate.price = newPrice
                        println("Book price updated successfully.")
                    }
                    2 -> {
                        val newGenre = readNextLine("Enter new genre: ")
                        bookToUpdate.genre = newGenre
                        println("Book genre updated successfully.")
                    }
                    3 -> {
                        val newPublicationYear = readValidYear("Enter new publication year: ")
                        bookToUpdate.publicationYear = newPublicationYear
                        println("Book publication year updated successfully.")
                    }
                    4 -> {
                        val newIsbn = readNextInt("Enter new ISBN (9 digits): ")
                        bookToUpdate.isbn = newIsbn
                        println("Book ISBN updated successfully.")
                    }
                    5 -> println("Exiting update menu.")
                    else -> println("Invalid option. Please try again.")
                }
            } while (option != 5)
        } else {
            println("There are no books with this ID.")
        }
    }
}

fun updateAuthor() {
    if (!API.listAllAuthors().contains("No authors", ignoreCase = true)) {
        println(API.listAllAuthors())
        val idToUpdate = readNextInt("Enter the ID of the author you want to update: ")
        val authorToUpdate = API.validAuthorId(idToUpdate)

        if (authorToUpdate != null) {
            var option: Int
            do {
                println("""
                    What would you like to update?
                    1. Add New Genre
                    2. Change Country of Origin
                    3. Exit
                """.trimIndent())
                option = readNextInt("Enter your choice: ")

                when (option) {
                    1 -> {
                        val newGenre = readNextLine("Enter new genre to add: ")
                        if (newGenre.isNotBlank()&& !containsNumbers(newGenre)) {
                            authorToUpdate.genres.add(newGenre)
                            println("Genre added successfully.")
                        } else {
                            println("Invalid genre. Please try again.")
                        }
                    }
                    2 -> {
                        val newCountry = readNextLine("Enter new country of origin: ")
                        if (newCountry.isNotBlank()&& !containsNumbers(newCountry)) {
                            authorToUpdate.country = newCountry
                            println("Country of origin updated successfully.")
                        } else {
                            println("Invalid input. Please try again.")
                        }
                    }
                    3 -> println("Exiting update menu.")
                    else -> println("Invalid option. Please try again.")
                }
            } while (option != 3)
        } else {
            println("There are no authors with this ID.")
        }
    }
}

fun listingAuthors () {
    var option: Int
    do {
        println("""
                    Listing options
                    1. List all authors
                    2. List authors by genre
                    3. List authors by number of books written
                    4. List author by name
                    5. Exit
                """.trimIndent())
        option = readNextInt("Enter your choice: ")

        when (option) {
            1 -> {
            println(API.listAllAuthors())
            }
            2 -> {
                if (!API.listAllAuthors().contains("No authors"))
                {
                    val genre = readNextLine("Please, enter a genre: ")
                   if (genre.isNotBlank() && !containsNumbers(genre)) {
                       println(API.searchAuthorsByGenre(genre))
                }
                else {
                    println("Genre can't be blank or contain numbers.Please, re-enter")
                }
                }
                    else (println ("No authors yet."))
            }
            3 -> {
                if (!API.listAllAuthors().contains("No authors"))
                {
                    val min = readNextInt("Please, enter a minimum number of books for an author: ")
                    val max = readNextInt("Now, enter a maximum number of books for an author: ")
                    println(API.listAuthorsByMaxMinBooks(min,max))

                }
                else (println ("No authors yet."))
            }
            4 -> {
                if (!API.listAllAuthors().contains("No authors"))
                {
                    val name = readNextLine("Please, enter the author's name: ")
                    println(API.searchAuthorByName(name))

                }
                else (println ("No authors yet."))

            }
            5 -> println("Exiting menu.")
            else -> println("Invalid option. Please try again.")
        }
    } while (option != 5)
}

fun listingBooks () {
    var option: Int
    do {
        println("""
                    Listing options
                    1. List all books
                    2. List books by title
                    3. List books by price
                    4. List books by genre
                    5. Exit
                """.trimIndent())
        option = readNextInt("Enter your choice: ")

        when (option) {
            1 -> {
               println(API.listAllBooks())
            }
            2 -> {
            if (!API.listAllBooks().contains("No books")) {
                val title = readNextLine("Please, enter book's title: ")
               println( API.searchBookByTitle(title))
            }
                else {
                   println("No books yet.")
                }
            }
            3 -> {
                if (!API.listAllBooks().contains("No books")) {
                    val min = readNextDouble("Please, enter a minimum price: ")
                    val max = readNextDouble("Now, enter a maximum price: ")
                    println(API.listBooksByPrice(min,max))
                }
                else {
                    println("No books yet.")
                }
            }
            4 -> {

                if (!API.listAllBooks().contains("No books")) {
                    val genre = readNextLine("Please, enter a genre: ")
                    if (genre.isNotBlank() && !containsNumbers(genre)) {
                      println( API.searchBooksByGenre(genre))
                    }
                    else {
                        println("Genre can't be blank or contain numbers.Please, re-enter")
                    }
                }
                else {
                    println("No books yet.")
                }

            }
            5 -> println("Exiting menu.")
            else -> println("Invalid option. Please try again.")
        }
    } while (option != 5)
}


