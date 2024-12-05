package ie.setu.models

data class Book(
    var bookId: Int,
    var title: String,
    var author: Author,
    var genre: String,
    var publicationYear: Int,
    var price: Float,
    var isbn: Int
) {
    override fun toString(): String {
        return "Book(id=$bookId, title=$title, genre=$genre, publicationYear=$publicationYear, price=$price, isbn=$isbn, author=${author.name})"
    }
}
