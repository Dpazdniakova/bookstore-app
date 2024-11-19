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
        var listOfAuthors= ""
         if (authors.isEmpty()) {
            return "No authors yet"
        } else {
            for (i in authors.indices) {
                listOfAuthors+= " ${authors[i]} \n"
            }
        }
        return listOfAuthors
    }
    fun listAllBooks (): String {
        var listOfBooks= ""
        if (books.isEmpty()) {
            return "No books yet"
        } else {
            for (i in books.indices) {
                listOfBooks+= "${books[i]} \n"
            }
        }
        return listOfBooks


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