package ie.setu.models

import java.time.LocalDate

class Author ( var authorId: Int, var name: String, var country: String? = null, var dateOfBirth: LocalDate? = null, var genres: List<String>,
               var booksWritten: ArrayList<Book>)
{}