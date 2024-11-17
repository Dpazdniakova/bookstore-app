package ie.setu.models

data class Book (var bookId: Int, var title: String, var author: Author, var genre: String, var publicationYear: Int,
            var price: Double, var isbn: Int) {}