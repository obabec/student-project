package com.redhat.restdemo.model.repository;

import com.redhat.restdemo.model.entity.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {
    Iterable<Book> findBooksByGenre(String genre);

    @Query(value = "SELECT b.id, b.isbn, b.name, b.year_of_release, b.genre " +
            "FROM book b " +
            "JOIN authorship auth ON b.id = auth.author_id" +
            " JOIN author a ON a.id = auth.author_id " +
            "WHERE author_id = :authorId", nativeQuery = true)
    Iterable<Book> findBooksByAuthor(@Param("authorId") Integer authorId);

    @Query(value = "SELECT b.id, b.isbn, b.name, b.year_of_release, b.genre\n" +
            "FROM book b\n" +
            "INNER JOIN ownership o ON b.id = o.book_id\n" +
            "WHERE o.library_id = :libraryId", nativeQuery = true)
    Iterable<Book> findBooksByLibrary(@Param("libraryId") Integer libraryId);
}
