package controllers

import ie.setu.models.Author
import ie.setu.models.Book
import persistence.Serializer

/**
 * The API class provides functionality to manage authors and books.
 * It allows CRUD operations and provides methods to filter and list authors and books based on various criteria.
 *
 * @property authorsSerializer Serializer for managing authors' data persistence.
 * @property booksSerializer Serializer for managing books' data persistence.
 */
class API(private val authorsSerializer: Serializer, private val booksSerializer: Serializer) {
    private var authors = ArrayList<Author>()
    private var books = ArrayList<Book>()

    /**
     * Loads authors and books data from the serializers.
     *
     * @throws Exception if there is an issue reading data.
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(Exception::class)
    fun load() {
        authors = authorsSerializer.read() as ArrayList<Author>
        books = booksSerializer.read() as ArrayList<Book>
    }

    /**
     * Stores authors and books data to the serializers.
     *
     * @throws Exception if there is an issue writing data.
     */
    @Throws(Exception::class)
    fun store() {
        authorsSerializer.write(authors)
        booksSerializer.write(books)
    }

    /**
     * Adds an author to the authors list.
     *
     * @param author The author to be added.
     * @return `true` if the author was successfully added, otherwise `false`.
     */
    fun addAuthor(author: Author): Boolean {
        return authors.add(author)
    }

    /**
     * Adds a book to the books list.
     *
     * @param book The book to be added.
     * @return `true` if the book was successfully added, otherwise `false`.
     */
    fun addBook(book: Book): Boolean {
        return books.add(book)
    }

    /**
     * Lists all authors in the system.
     *
     * @return A formatted string of all authors or a message indicating no authors exist.
     */
    fun listAllAuthors(): String {
        return if (authors.isEmpty()) {
            "No authors yet"
        } else {
            authorList(authors)
        }
    }

    /**
     * Lists all books in the system.
     *
     * @return A formatted string of all books or a message indicating no books exist.
     */
    fun listAllBooks(): String {
        return if (books.isEmpty()) {
            "No books yet"
        } else {
            bookList(books)
        }
    }

    /**
     * Searches for an author by their ID.
     *
     * @param id The ID of the author to search for.
     * @return The found Author object.
     * @throws NoSuchElementException if no author with the given ID exists.
     */
    fun searchExistingAuthor(id: Int): Author {
        return authors.first { it.authorId == id }
    }

    /**
     * Retrieves the last added author.
     *
     * @return The most recently added Author object.
     */
    fun lastAddedAuthor(): Author {
        return authors.last()
    }

    /**
     * A lambda function to validate and retrieve an author by ID.
     */
    val validAuthorId: (Int) -> Author? = { id ->
        authors.find { it.authorId == id }
    }

    /**
     * A lambda function to validate and retrieve a book by ID.
     */
    val validBookId: (Int) -> Book? = { id ->
        books.find { it.bookId == id }
    }

    /**
     * Deletes a book by its ID.
     *
     * @param id The ID of the book to delete.
     * @return `true` if the book was successfully deleted, otherwise `false`.
     */
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

    /**
     * Deletes an author by their ID, also removing their books.
     *
     * @param id The ID of the author to delete.
     * @return `true` if the author was successfully deleted, otherwise `false`.
     */
    fun deleteAuthor(id: Int): Boolean {
        val authorIndex = authors.indexOfFirst { it.authorId == id }
        if (authorIndex != -1) {
            val authorToRemove = authors[authorIndex]
            authorToRemove.booksWritten.forEach { book ->
                books.removeIf { it.title == book.title }
            }
            authors.removeAt(authorIndex)
            return true
        }
        return false
    }

    /**
     * Searches for books by their title.
     *
     * @param title The title (or partial title) to search for.
     * @return A formatted string of books matching the title or a message indicating no matches.
     */
    fun searchBookByTitle(title: String): String {
        val books = bookList(
            books.filter { book -> book.title.contains(title, ignoreCase = true) }
        )
        return if (books.isEmpty()) {
            "No book found with this title."
        } else {
            books
        }
    }

    /**
     * Searches for authors by their name.
     *
     * @param name The name (or partial name) to search for.
     * @return A formatted string of authors matching the name or a message indicating no matches.
     */
    fun searchAuthorByName(name: String): String {
        val authors = authorList(
            authors.filter { author -> author.name.contains(name, ignoreCase = true) }
        )
        return if (authors.isEmpty()) {
            "No book found with this title."
        } else {
            authors
        }
    }

    /**
     * Searches for authors by their genre.
     *
     * @param genre The genre to search for.
     * @return A formatted string of authors who have written in the specified genre, or a message indicating no matches.
     */
    fun searchAuthorsByGenre(genre: String): String {
        val authorsWithGenre = authors.filter { author ->
            author.genres.any { it.equals(genre, ignoreCase = true) }
        }

        return if (authorsWithGenre.isEmpty()) {
            "No authors found with this genre."
        } else {
            authorList(authorsWithGenre)
        }
    }

    /**
     * Searches for books by their genre.
     *
     * @param genre The genre to search for.
     * @return A formatted string of books that belong to the specified genre, or a message indicating no matches.
     */
    fun searchBooksByGenre(genre: String): String {
        val booksWithGenre = books.filter { book ->
            book.genre.equals(genre, ignoreCase = true)
        }

        return if (booksWithGenre.isEmpty()) {
            "No books found with this genre."
        } else {
            bookList(booksWithGenre)
        }
    }

    /**
     * Lists books within a specified price range.
     *
     * @param minPrice The minimum price (exclusive).
     * @param maxPrice The maximum price (exclusive).
     * @return A formatted string of books within the specified price range, or a message indicating no matches.
     */
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

    /**
     * Lists authors who have written between a minimum and maximum number of books.
     *
     * @param min The minimum number of books (inclusive).
     * @param max The maximum number of books (inclusive).
     * @return A formatted string of authors within the specified book count range, or a message indicating no matches.
     */
    fun listAuthorsByMaxMinBooks(min: Int, max: Int): String {
        val suitableAuthors = authors.filter { author -> author.booksWritten.size in min..max }
        return if (suitableAuthors.isEmpty()) {
            "No authors found with a specified book count range"
        } else {
            authorList(suitableAuthors)
        }
    }

    /**
     * Generates a formatted string of books from the provided list.
     *
     * @param list The list of books to format.
     * @return A formatted string of book details.
     */
    private fun bookList(list: List<Book>): String =
        list.joinToString(separator = "\n") { book -> book.toString() }

    /**
     * Generates a formatted string of authors from the provided list.
     *
     * @param list The list of authors to format.
     * @return A formatted string of author details.
     */
    private fun authorList(list: List<Author>): String =
        list.joinToString(separator = "\n") { author -> author.toString() }

    /**
     * Retrieves the total number of books.
     *
     * @return The count of books in the system.
     */
    fun numberOfBooks(): Int {
        return books.size
    }

    /**
     * Retrieves the total number of authors.
     *
     * @return The count of authors in the system.
     */
    fun numberOfAuthors(): Int {
        return authors.size
    }

    /**
     * Finds a book by its ID.
     *
     * @param id The ID of the book to find.
     * @return The Book object with the specified ID.
     * @throws NoSuchElementException if no book with the given ID exists.
     */
    fun findBookById(id: Int): Book {
        return books.first { it.bookId == id }
    }
}
