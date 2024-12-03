package controllers
import java.io.File
import java.time.LocalDate
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ie.setu.models.Author
import ie.setu.models.Book
import persistence.JSONSerializer
class APITest {

    private var author1: Author? = null
    private var author2: Author? = null
    private var book1: Book? = null
    private var book2: Book? = null
    private var api: API? = null

    @BeforeEach
    fun setup() {
        author1 = Author(
            authorId = 1,
            name = "J.K. Rowling",
            country = "UK",
            dateOfBirth = LocalDate.of(1965, 7, 31),
            genres = listOf("Fantasy", "Drama").toMutableList(),
            booksWritten = ArrayList()
        )
        author2 = Author(
            authorId = 2,
            name = "George R.R. Martin",
            country = "USA",
            dateOfBirth = LocalDate.of(1948, 9, 20),
            genres = listOf("Fantasy", "Drama").toMutableList(),
            booksWritten = ArrayList()
        )

        book1 = Book(
            bookId = 1,
            title = "Harry Potter and the Philosopher's Stone",
            author = author1,
            genre = "Fantasy",
            publicationYear = 1997,
            price = 20.99f,
            isbn = 123456789
        )

        book2 = Book(
            bookId = 2,
            title = "A Game of Thrones",
            author = author2,
            genre = "Fantasy",
            publicationYear = 1996,
            price = 15.99f,
            isbn = 987654321
        )

        api = API(
            JSONSerializer(File("authors.json")),
            JSONSerializer(File("books.json"))
        )
        api!!.addAuthor(author1!!)
        api!!.addAuthor(author2!!)
        api!!.addBook(book1!!)
        api!!.addBook(book2!!)
    }

    @AfterEach
    fun tearDown() {
        File("authors.json").delete()
        File("books.json").delete()
        author1 = null
        author2 = null
        book1 = null
        book2 = null
        api = null
    }

    @Nested
    inner class AddEntities {

        @Test
        fun `adding an author to the API adds it to the authors list`() {
            val newAuthor = Author(
                authorId = 3,
                name = "J.R.R. Tolkien",
                country = "UK",
                dateOfBirth = LocalDate.of(1892, 1, 3),
                genres = listOf("Fantasy").toMutableList(),
                booksWritten = ArrayList()
            )
            assertEquals(2, api!!.listAllAuthors().lines().count())
            assertTrue(api!!.addAuthor(newAuthor))
            assertEquals(3, api!!.listAllAuthors().lines().count())
        }

        @Test
        fun `adding a book to the API adds it to the books list`() {
            val newBook = Book(
                bookId = 3,
                title = "The Hobbit",
                author = author1,
                genre = "Fantasy",
                publicationYear = 1937,
                price = 18.99f,
                isbn = 1122334455
            )
            assertEquals(2, api!!.listAllBooks().lines().count())
            assertTrue(api!!.addBook(newBook))
            assertEquals(3, api!!.listAllBooks().lines().count())
        }
    }

    @Nested
    inner class ListEntities {

        @Test
        fun `listAllAuthors returns correct authors when authors are added`() {
            val authorsList = api!!.listAllAuthors().lowercase()
            assertTrue(authorsList.contains("j.k. rowling"))
            assertTrue(authorsList.contains("george r.r. martin"))
        }

        @Test
        fun `listAllBooks returns correct books when books are added`() {
            val booksList = api!!.listAllBooks().lowercase()
            assertTrue(booksList.contains("harry potter and the philosopher's stone"))
            assertTrue(booksList.contains("a game of thrones"))
        }

        @Test
        fun `listAllAuthors returns No authors yet when no authors are added`() {
            val emptyApi = API(
                JSONSerializer(File("authors.json")),
                JSONSerializer(File("books.json"))
            )
            assertTrue(emptyApi.listAllAuthors().lowercase().contains("no authors yet"))
        }

        @Test
        fun `listAllBooks returns No books yet when no books are added`() {
            val emptyApi = API(
                JSONSerializer(File("authors.json")),
                JSONSerializer(File("books.json"))
            )
            assertTrue(emptyApi.listAllBooks().lowercase().contains("no books yet"))
        }
    }

    @Nested
    inner class SearchEntities {

        @Test
        fun `searchExistingAuthor returns the correct author by ID`() {
            val foundAuthor = api!!.searchExistingAuthor(1)
            assertEquals("J.K. Rowling", foundAuthor.name)
            assertEquals(1, foundAuthor.authorId)
        }

        @Test
        fun `searchExistingAuthor throws exception when author does not exist`() {
            assertThrows<NoSuchElementException> {
                api!!.searchExistingAuthor(999)
            }
        }

        @Test
        fun `searchExistingAuthor throws exception when list is empty`() {
            val emptyApi = API(
                JSONSerializer(File("authors.json")),
                JSONSerializer(File("books.json"))
            )
            assertThrows<NoSuchElementException> {
                emptyApi.searchExistingAuthor(999)
            }
        }

        @Test
        fun `lastAddedAuthor returns the last added author`() {
            val lastAuthor = api!!.lastAddedAuthor()
            assertEquals("George R.R. Martin", lastAuthor.name)
        }

        @Test
        fun `validAuthorId returns true for existing author ID`() {
            assertNotNull(api!!.validAuthorId(1))
            assertNotNull(api!!.validAuthorId(2))
        }

        @Test
        fun `validAuthorId returns false for non-existing author ID`() {
            assertNull(api!!.validAuthorId(999))
        }
    }

    @Nested
    inner class DeleteEntities {
        @Test
        fun testDeleteBook() {
            api!!.deleteBook(1)
            val authorBooks = author1!!.booksWritten
            assertFalse(authorBooks.any { it.bookId == 1 })
            val booksList = api!!.listAllBooks()
            assertFalse(booksList.contains("Harry Potter and the Philosopher's Stone"))
        }

        @Test
        fun testDeleteAuthor() {
            api!!.deleteAuthor(1)
            val authorsList = api!!.listAllAuthors()
            println("Remaining authors after deletion: $authorsList")
            assertFalse(authorsList.contains("J.K. Rowling"))
            val remainingBooks = api!!.listAllBooks()
            println("Remaining books after deletion: $remainingBooks")
            assertFalse(remainingBooks.contains("Harry Potter and the Philosopher's Stone"))
        }
    }

    @Nested
    inner class UpdateEntities {
        @Test
        fun testUpdateBook() {
            val updatedPrice = 25.99f
            val updatedGenre = "Adventure"
            val updatedPublicationYear = 2000
            val updatedIsbn = 111222333

            val bookToUpdate = api!!.validBookId(1)
            assertNotNull(bookToUpdate)

            bookToUpdate!!.price = updatedPrice
            bookToUpdate.genre = updatedGenre
            bookToUpdate.publicationYear = updatedPublicationYear
            bookToUpdate.isbn = updatedIsbn

            val updatedBook = api!!.validBookId(1)
            assertEquals(updatedPrice, updatedBook!!.price)
            assertEquals(updatedGenre, updatedBook.genre)
            assertEquals(updatedPublicationYear, updatedBook.publicationYear)
            assertEquals(updatedIsbn, updatedBook.isbn)
        }

        @Test
        fun testUpdateAuthor() {
            val updatedCountry = "Canada"
            val newGenre = "Mystery"

            val authorToUpdate = api!!.validAuthorId(1)
            assertNotNull(authorToUpdate)

            authorToUpdate!!.country = updatedCountry
            authorToUpdate.genres.add(newGenre)

            val updatedAuthor = api!!.validAuthorId(1)
            assertEquals(updatedCountry, updatedAuthor!!.country)
            assertTrue(updatedAuthor.genres.contains(newGenre))
        }
    }

    @Nested
    inner class BookListingEntities() {
        @Test
        fun `searchBookByTitle should return book details if a book with the title exists`() {
            val result = api!!.searchBookByTitle("Harry Potter")
            assertTrue(result.contains("Harry Potter and the Philosopher's Stone"))
        }

        @Test
        fun `searchBookByTitle should return a message if no book with the title exists`() {
            val result = api!!.searchBookByTitle("Nonexistent Title")
            assertEquals("No book found with this title.", result)
        }

        @Test
        fun `searchBooksByGenre should return books in the specified genre`() {
            val result = api!!.searchBooksByGenre("Fantasy")
            assertTrue(result.contains("Harry Potter and the Philosopher's Stone"))
            assertTrue(result.contains("A Game of Thrones"))
        }

        @Test
        fun `searchBooksByGenre should return a message if no books exist in the specified genre`() {
            val result = api!!.searchBooksByGenre("Science Fiction")
            assertEquals("No books found with this genre.", result)
        }

        @Test
        fun `listBooksByPrice should return books within the specified price range`() {
            val result = api!!.listBooksByPrice(10.0, 25.0)
            assertTrue(result.contains("Harry Potter and the Philosopher's Stone"))
            assertTrue(result.contains("A Game of Thrones"))
        }

        @Test
        fun `listBooksByPrice should return a message if no books exist within the specified price range`() {
            val result = api!!.listBooksByPrice(30.0, 40.0)
            assertEquals("No books found within the specified price range.", result)
        }
    }

    @Nested
    inner class AuthorListingEntities() {
        @Test
        fun `searchAuthorByName should return author details if an author with the name exists`() {
            val result = api!!.searchAuthorByName("J.K. Rowling")
            assertTrue(result.contains("J.K. Rowling"))
        }

        @Test
        fun `searchAuthorByName should return a message if no author with the name exists`() {
            val result = api!!.searchAuthorByName("Unknown Author")
            assertEquals("No book found with this title.", result)
        }

        @Test
        fun `searchAuthorsByGenre should return authors who have written books in the specified genre`() {
            val result = api!!.searchAuthorsByGenre("Fantasy")
            assertTrue(result.contains("J.K. Rowling"))
            assertTrue(result.contains("George R.R. Martin"))
        }

        @Test
        fun `searchAuthorsByGenre should return a message if no authors have written in the specified genre`() {
            val result = api!!.searchAuthorsByGenre("Science Fiction")
            assertEquals("No authors found with this genre.", result)
        }

        @Test
        fun `listAuthorsByMaxMinBooks should return authors with a book count within the specified range`() {
            author1!!.booksWritten.add(book1!!)
            val result = api!!.listAuthorsByMaxMinBooks(1, 5)
            assertTrue(result.contains("J.K. Rowling"))
        }

        @Test
        fun `listAuthorsByMaxMinBooks should return a message if no authors have a book count within the specified range`() {
            val result = api!!.listAuthorsByMaxMinBooks(10, 20)
            assertEquals("No authors found with a specified book count range", result)
        }
    }

    @Nested
    inner class PersistenceTests() {
        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            val emptyApi = API(
                JSONSerializer(File("emptyAuthors.json")),
                JSONSerializer(File("emptyBooks.json"))
            )
            emptyApi.store()
            val loadedApi = API(
                JSONSerializer(File("emptyAuthors.json")),
                JSONSerializer(File("emptyBooks.json"))
            )
            loadedApi.load()
            assertEquals(0, emptyApi.numberOfAuthors())
            assertEquals(0, emptyApi.numberOfBooks())
            assertEquals(emptyApi.numberOfAuthors(), loadedApi.numberOfAuthors())
            assertEquals(emptyApi.numberOfBooks(), loadedApi.numberOfBooks())
        }

        @Test
        fun `saving and loading a populated collection in JSON retains data`() {
            api!!.store()
            val loadedApi = API(
                JSONSerializer(File("authors.json")),
                JSONSerializer(File("books.json"))
            )
            loadedApi.load()

            assertEquals(loadedApi.numberOfAuthors(), api!!.numberOfAuthors())
            assertEquals(api!!.numberOfBooks(), loadedApi.numberOfBooks())

            assertEquals(api!!.searchExistingAuthor(1), loadedApi.searchExistingAuthor(1))
            assertEquals(api!!.findBookById(1), loadedApi.findBookById(1))
        }
    }
}
