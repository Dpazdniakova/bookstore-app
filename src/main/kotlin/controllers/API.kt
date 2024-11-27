package controllers

import ie.setu.models.Author
import ie.setu.models.Book
import persistence.JSONSerializer
import persistence.Serializer
import java.io.File
import java.time.LocalDateTime

class API(private val authorsSerializer: Serializer, private val booksSerializer: Serializer) {
    private var authors = ArrayList<Author>()
    private var books = ArrayList<Book>()

    @Throws(Exception::class)
    fun load() {
        // Load authors and books from their respective files
        authors = authorsSerializer.read() as ArrayList<Author>
        books = booksSerializer.read() as ArrayList<Book>
    }

    @Throws(Exception::class)
    fun store() {
        // Store authors and books in their respective files
        authorsSerializer.write(authors)
        booksSerializer.write(books)
    }


    fun addAuthor(author: Author): Boolean {
        return authors.add(author)
    }

    fun addBook(book: Book): Boolean {
        return books.add(book)
    }

    fun listAllAuthors(): String {
        return if (authors.isEmpty()) {
            "No authors yet"
        } else {
          authorList(authors)
        }
    }

    fun listAllBooks(): String {
        return if (books.isEmpty()) {
            "No books yet"
        } else {
            bookList(books)
        }
    }

    fun searchExistingAuthor(Id: Int): Author {

        val author = authors.first { it.authorId == Id }
        return author
    }

    fun lastAddedAuthor(): Author {
        val lastAuthor = authors.last()
        return lastAuthor
    }

    val validAuthorId: (Int) -> Author? = { Id ->
        authors.find { it.authorId == Id}
    }
    val validBookId: (Int) -> Book? = { Id ->
         books.find { it.bookId == Id}
    }
    fun deleteBook(id: Int): Boolean {
        val index = books.indexOfFirst { it.bookId == id }
        if (index != -1) {
            val bookToRemove = books[index]
            bookToRemove.author?.booksWritten?.removeIf { it.bookId == id }
            books.removeAt(index)
            return true
        }
        return false
    }

    fun deleteAuthor(id: Int) : Boolean{
        val authorIndex = authors.indexOfFirst {it.authorId ==id}
        if (authorIndex != -1) {
            val authorToRemove =authors[authorIndex]
            authorToRemove.booksWritten.forEach { book ->
                books.removeIf { it.title == book.title }
            }
            authors.removeAt(authorIndex)
            return true
        }
        return false
    }
    fun searchBookByTitle(title: String): String {
        val books = bookList(
            books.filter { book -> book.title.contains(title, ignoreCase = true) })

        return if (books.isEmpty()) {
            "No book found with this title."
        } else {
            books
        }
    }
    fun searchAuthorByName(name: String): String {
        val authors = authorList(
            authors.filter { author -> author.name.contains(name, ignoreCase = true) })

        return if (authors.isEmpty()) {
            "No book found with this title."
        } else {
            authors
        }
    }
    fun searchAuthorsByGenre(genre: String): String {
        val authorsWithGenre = authors.filter { author ->
            author.genres.any { it.equals (genre , ignoreCase =true)}
        }

        return if (authorsWithGenre.isEmpty()) {
            "No authors found with this genre."
        } else {
            authorList(authorsWithGenre)
        }
    }
    fun searchBooksByGenre(genre: String): String {
        val booksWithGenre = books.filter { book ->
            book.genre.equals(genre,ignoreCase = true)
        }

        return if (booksWithGenre.isEmpty()) {
            "No books found with this genre."
        } else {
            bookList(booksWithGenre)
        }
    }
    fun listBooksByPrice(minPrice: Double, maxPrice: Double): String {
        val booksInRange = books.filter { book ->
            book.price > minPrice && book.price < maxPrice
        }

        return if (booksInRange.isEmpty()) {
            "No books found within the specified price range."
        } else {
           bookList(booksInRange)
        }
    }
    fun listAuthorsByMaxMinBooks (min: Int, max: Int) : String {
        val suitableAuthors = authors.filter {author -> author.booksWritten.size in min .. max
        }
       return if (suitableAuthors.isEmpty()) {
           "No authors found with a specified book count range"
       }
       else {
           authorList(suitableAuthors)
       }
    }
    fun bookList(list: List<Book>): String =
        list.joinToString(separator = "\n") { book -> book.toString() }

    fun authorList(list: List<Author>): String =
        list.joinToString(separator = "\n") { author -> author.toString() }


    fun numberOfBooks(): Int {
        return books.size
    }
    fun numberOfAuthors(): Int {
        return authors.size
    }
    fun findBookById(Id: Int): Book {
        val book = books.first { it.bookId == Id }
        return book
    }
}


