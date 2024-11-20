package controllers

import ie.setu.models.Author
import ie.setu.models.Book
import java.time.LocalDateTime

class API {
    private var authors = ArrayList<Author>()
    private var books = ArrayList<Book>()

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
            authorToRemove.booksWritten.reversed().forEach { book ->
                books.removeIf { it.title == book.title }
            }
            authors.removeAt(authorIndex)
            return true
        }
        return false
    }
    fun searchBookByTitle(title: String): String {
        val books = bookList(
            books.filter { note -> note.title.contains(title, ignoreCase = true) })

        return if (books.isEmpty()) {
            "No book found with this title."
        } else {
            books
        }
    }
    fun searchAuthorByName(name: String): String {
        val authors = authorList(
            authors.filter { note -> note.name.contains(name, ignoreCase = true) })

        return if (authors.isEmpty()) {
            "No book found with this title."
        } else {
            authors
        }
    }

    fun bookList(list: List<Book>): String =
        list.joinToString(separator = "\n") { book -> book.toString() }

    fun authorList(list: List<Author>): String =
        list.joinToString(separator = "\n") { author -> author.toString() }

}
