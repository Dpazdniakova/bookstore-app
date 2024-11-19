package controllers

import ie.setu.models.Author
import ie.setu.models.Book
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Assertions.assertThrows
import java.time.LocalDate

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
            genres = listOf("Fantasy", "Drama"),
            booksWritten = ArrayList()
        )
        author2 = Author(
            authorId = 2,
            name = "George R.R. Martin",
            country = "USA",
            dateOfBirth = LocalDate.of(1948, 9, 20),
            genres = listOf("Fantasy", "Drama"),
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

        api = API()
        api!!.addAuthor(author1!!)
        api!!.addAuthor(author2!!)
        api!!.addBook(book1!!)
        api!!.addBook(book2!!)
    }

    @AfterEach
    fun tearDown() {
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
                genres = listOf("Fantasy"),
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
            val emptyApi = API()
            assertTrue(emptyApi.listAllAuthors().lowercase().contains("no authors yet"))
        }

        @Test
        fun `listAllBooks returns No books yet when no books are added`() {
            val emptyApi = API()
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
                api!!.searchExistingAuthor(999)  // 999 is an ID that does not exist
            }
        }

        @Test
        fun `searchExistingAuthor throws exception when list is empty`() {
            val emptyApi = API()  // Empty list of authors
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
            assertTrue(api!!.validAuthorId(1))
            assertTrue(api!!.validAuthorId(2))
        }

        @Test
        fun `validAuthorId returns false for non-existing author ID`() {
            assertFalse(api!!.validAuthorId(999))
        }
    }
}
