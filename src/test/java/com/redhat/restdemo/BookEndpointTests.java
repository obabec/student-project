package com.redhat.restdemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.restdemo.model.entity.Book;
import com.redhat.restdemo.model.repository.BookRepository;
import com.redhat.restdemo.utils.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.*;

import static com.redhat.restdemo.utils.Utils.countGetResult;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class BookEndpointTests extends EndpointTestTemplate {
    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    public void prepareBookSchema() throws IOException {
        prepareSchema(bookRepository, createURLWithPort("/book/add"), TestData.books);
    }

    @Test
    void testGetAllBooksEndpoint() throws JsonProcessingException {
        String bookUrl = createURLWithPort("/book");

        ResponseEntity<String> response = testRequests.get(bookUrl);

        ObjectMapper objectMapper = new ObjectMapper();
        List<Book> books = objectMapper.readValue(response.getBody(), new TypeReference<List<Book>>() {
        });
        assertThat(books.size(), is(6));

        assertThat(books.get(0).getIsbn(), is(9780747532743L));
        assertThat(books.get(0).getName(), is("Harry Potter and the Sorcerer's Stone"));
        assertThat(books.get(0).getYearOfRelease(), is(1997));
        assertThat(books.get(0).getGenre(), is("Fantasy"));

        assertThat(books.get(1).getIsbn(), is(9780451524935L));
        assertThat(books.get(1).getName(), is("Nineteen Eighty-Four"));
        assertThat(books.get(1).getYearOfRelease(), is(1949));
        assertThat(books.get(1).getGenre(), is("Dystopian Fiction"));

        assertThat(books.get(2).getIsbn(), is(9780141439518L));
        assertThat(books.get(2).getName(), is("Pride and Prejudice"));
        assertThat(books.get(2).getYearOfRelease(), is(1813));
        assertThat(books.get(2).getGenre(), is("Classic Fiction"));

        assertThat(books.get(3).getIsbn(), is(9780684801223L));
        assertThat(books.get(3).getName(), is("The Old Man and the Sea"));
        assertThat(books.get(3).getYearOfRelease(), is(1952));
        assertThat(books.get(3).getGenre(), is("Fiction"));

        assertThat(books.get(4).getIsbn(), is(9780345514400L));
        assertThat(books.get(4).getName(), is("I Know Why the Caged Bird Sings"));
        assertThat(books.get(4).getYearOfRelease(), is(1969));
        assertThat(books.get(4).getGenre(), is("Autobiography"));

        assertThat(books.get(5).getIsbn(), is(9780876857632L));
        assertThat(books.get(5).getName(), is("Post Office"));
        assertThat(books.get(5).getYearOfRelease(), is(1971));
        assertThat(books.get(5).getGenre(), is("Fiction"));
    }

    @Test
    void testGetBookById() throws IOException {
        String bookUrl = createURLWithPort("/book");

        ObjectMapper objectMapper = new ObjectMapper();

        ResponseEntity<String> response = testRequests.get(bookUrl);
        List<Book> books = objectMapper.readValue(response.getBody(), new TypeReference<List<Book>>() {
        });

        for (Book book : books) {
            Integer id = book.getId();
            Book testBook = objectMapper.readValue(testRequests.get(bookUrl + "/" + id).getBody(), new TypeReference<>() {
            });
            assertThat(book, is(testBook));
        }

        int nonSenseId = new Random().nextInt(50000) + 100;
        ResponseEntity<String> nonSenseResponse = testRequests.get(bookUrl + "/" + nonSenseId);
        assert (nonSenseResponse.getStatusCode().is4xxClientError());
    }

    @Test
    void testGetBooksByGenre() throws IOException {
        HashMap<String, HashSet<Book>> genres = new HashMap<>();

        ObjectMapper objectMapper = new ObjectMapper();

        Iterable<Book> books = bookRepository.findAll();

        for (Book book : books) {
            String genre = book.getGenre();
            if (genres.containsKey(genre)) {
                genres.get(genre).add(book);
            } else {
                genres.put(genre, new HashSet<>(Set.of(book)));
            }
        }

        String genreUrl = createURLWithPort("/book/genre/");

        for (String genre : genres.keySet()) {
            Iterable<Book> genreBooks = objectMapper.readValue(testRequests.get(genreUrl + genre).getBody(), new TypeReference<>() {
            });
            assert(countGetResult(genreBooks) == genres.get(genre).size());
            for (Book book : genreBooks) {
                assert(genres.get(genre).contains(book));
            }
        }
    }

    @Test
    void testGetBooksByAuthor() throws IOException {
        //TODO
    }

    @Test
    void testAddBookEndpoint() throws IOException {
        String bookUrl = createURLWithPort("/book");

        ResponseEntity<String> response = testRequests.get(bookUrl);

        ObjectMapper objectMapper = new ObjectMapper();

        List<Book> books = objectMapper.readValue(response.getBody(), new TypeReference<List<Book>>() {
        });

        int expectedSize = 6;

        assertThat(books.size(), is(expectedSize));

        for (Book book : books) {
            expectedSize--;
            assertThat(book.getId(), is(idCounter - expectedSize));
        }
    }

    @Test
    void testUpdateBookEndpoint() throws IOException {
        String bookUpdateUrl = createURLWithPort("/book/put");

        ObjectMapper objectMapper = new ObjectMapper();

        for (Book book : bookRepository.findAll()) {
            String newName = "New Book Title";
            testRequests.put(bookUpdateUrl + "/" + book.getId(), new Book(null, newName, null, null));
            Book updatedBook = objectMapper.readValue(testRequests.get(createURLWithPort("/book/" + book.getId())).getBody(), new TypeReference<>() {
            });
            assertThat(updatedBook.getId(), is(book.getId()));
            assertThat(updatedBook.getIsbn(), is(book.getIsbn()));
            assertThat(updatedBook.getName(), is(newName));
            assertThat(updatedBook.getYearOfRelease(), is(book.getYearOfRelease()));
            assertThat(updatedBook.getGenre(), is(book.getGenre()));
        }

        for (Book book : bookRepository.findAll()) {
            int newYearOfRelease = 2022;
            testRequests.put(bookUpdateUrl + "/" + book.getId(), new Book(null, null, newYearOfRelease, null));
            Book updatedBook = objectMapper.readValue(testRequests.get(createURLWithPort("/book/" + book.getId())).getBody(), new TypeReference<>() {
            });
            assertThat(updatedBook.getId(), is(book.getId()));
            assertThat(updatedBook.getIsbn(), is(book.getIsbn()));
            assertThat(updatedBook.getName(), is(book.getName()));
            assertThat(updatedBook.getYearOfRelease(), is(newYearOfRelease));
            assertThat(updatedBook.getGenre(), is(book.getGenre()));
        }

        for (Book book : bookRepository.findAll()) {
            String newGenre = "Sci-Fi";
            testRequests.put(bookUpdateUrl + "/" + book.getId(), new Book(null, null, null, newGenre));
            Book updatedBook = objectMapper.readValue(testRequests.get(createURLWithPort("/book/" + book.getId())).getBody(), new TypeReference<>() {
            });
            assertThat(updatedBook.getId(), is(book.getId()));
            assertThat(updatedBook.getIsbn(), is(book.getIsbn()));
            assertThat(updatedBook.getName(), is(book.getName()));
            assertThat(updatedBook.getYearOfRelease(), is(book.getYearOfRelease()));
            assertThat(updatedBook.getGenre(), is(newGenre));
        }

        for (Book book : bookRepository.findAll()) {
            String newName = "New Book Title";
            int newYearOfRelease = 2022;
            String newGenre = "Sci-Fi";
            testRequests.put(bookUpdateUrl + "/" + book.getId(), new Book(null, newName, newYearOfRelease, newGenre));
            Book updatedBook = objectMapper.readValue(testRequests.get(createURLWithPort("/book/" + book.getId())).getBody(), new TypeReference<>() {
            });
            assertThat(updatedBook.getId(), is(book.getId()));
            assertThat(updatedBook.getIsbn(), is(book.getIsbn()));
            assertThat(updatedBook.getName(), is(newName));
            assertThat(updatedBook.getYearOfRelease(), is(newYearOfRelease));
            assertThat(updatedBook.getGenre(), is(newGenre));
        }

        for (Book book : bookRepository.findAll()) {
            Long newIsbn = 37493234249L;
            testRequests.put(bookUpdateUrl + "/" + book.getId(), new Book(newIsbn, null, null, null));
            Book updatedBook = objectMapper.readValue(testRequests.get(createURLWithPort("/book/" + book.getId())).getBody(), new TypeReference<>() {
            });
            assertThat(updatedBook.getId(), is(book.getId()));
            assertThat(updatedBook.getIsbn(), is(newIsbn));
            assertThat(updatedBook.getName(), is(book.getName()));
            assertThat(updatedBook.getYearOfRelease(), is(book.getYearOfRelease()));
            assertThat(updatedBook.getGenre(), is(book.getGenre()));
        }

        ResponseEntity<String> nonSenseRequest;

        int nonSenseId = new Random().nextInt(50000) + 100;
        nonSenseRequest = testRequests.put(bookUpdateUrl + "/" + nonSenseId, new Book(4394732L, "Nonexistent Book", 2000, "Unknown"));
        assert (nonSenseRequest.getStatusCode().is4xxClientError());

        Book tryToChangeId = new Book(idCounter, 9999999999L, "Invalid Book", 2021, "Fiction");
        nonSenseRequest = testRequests.put(bookUpdateUrl + "/" + tryToChangeId.getIsbn(), tryToChangeId);
        assert (nonSenseRequest.getStatusCode().is4xxClientError());
    }

    @Test
    void testDeleteBookEndpoint() throws IOException {
        String bookDeleteUrl = createURLWithPort("/book/delete");

        Iterable<Book> books = bookRepository.findAll();

        Long booksCounter = countGetResult(books);

        for (int i = 0; i < 5; i++) {
            int nonSenseId = new Random().nextInt();
            ResponseEntity<String> response = testRequests.delete(bookDeleteUrl + "/" + nonSenseId);
            assert (response.getStatusCode().is4xxClientError());
            assertThat(countGetResult(bookRepository.findAll()), is(booksCounter));
        }

        for (Book book : books) {
            Integer bookId = book.getId();
            String deleteBookUrl = bookDeleteUrl + "/" + bookId;
            testRequests.delete(deleteBookUrl);
            booksCounter--;

            assertThat(booksCounter, is(countGetResult(bookRepository.findAll())));
            ResponseEntity<String> getResponse = testRequests.get(createURLWithPort("/book/" + book.getId()));
            assert (getResponse.getStatusCode().is4xxClientError());
        }

        for (int i = 0; i <= idCounter; i++) {
            ResponseEntity<String> response = testRequests.delete(bookDeleteUrl + "/" + i);
            assert (response.getStatusCode().is4xxClientError());
            assertThat(countGetResult(bookRepository.findAll()), is(booksCounter));
        }

        assertThat(countGetResult(bookRepository.findAll()), is(0L));
    }
}
