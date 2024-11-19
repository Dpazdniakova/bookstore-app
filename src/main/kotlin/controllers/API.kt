package controllers

import ie.setu.models.Author
import ie.setu.models.Book
import ie.setu.utils.readNextInt

class API {
    private var authors = ArrayList<Author>()
    private var books = ArrayList<Book>()

    fun addAuthor(author: Author): Boolean {
        return authors.add(author)
    }
    fun addBook(book:Book): Boolean {
        return books.add(book)
    }

    fun listAllAuthors(): String {
        return if (authors.isEmpty()) {
            "No authors yet"
        } else {
            authors.joinToString(separator = "\n") { " $it" }
        }
    }

    fun listAllBooks(): String {
        return if (books.isEmpty()) {
            "No books yet"
        } else {
            books.joinToString(separator = "\n")
        }
    }
   fun searchExistingAuthor (Id:Int): Author{

           val author = authors.filter { it.authorId == Id }.first()
       return author
   }
 fun lastAddedAuthor (): Author {
     val lastAuthor = authors.last()
     return lastAuthor
 }
    val validAuthorId: (Int) -> Boolean = { Id ->
        authors.map { it.authorId }.contains(Id)
    }
}