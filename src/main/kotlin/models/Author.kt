package ie.setu.models

import java.time.LocalDate

data class Author ( var authorId: Int, var name: String, var country: String? = null, var dateOfBirth: LocalDate? = null, var genres: MutableList<String>,
               var booksWritten: ArrayList<Book>)
{
    override fun toString(): String {
        val bookTitles = booksWritten.joinToString(", ") { it.title }
        val genresList = genres.joinToString(", ") { it }
        return "Author(id=$authorId, name=$name, country=$country, dateOfBirth=$dateOfBirth, genres=$genresList, booksWritten=$bookTitles)"
    }
}