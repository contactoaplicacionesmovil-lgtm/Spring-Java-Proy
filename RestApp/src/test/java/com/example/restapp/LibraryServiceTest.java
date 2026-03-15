package com.example.restapp;

import com.example.restapp.model.Book;
import com.example.restapp.service.LibraryService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LibraryServiceTest {

    @Test
    void testGetBooksByTitle() {
        LibraryService service = new LibraryService();
        Book book1 = new Book("Effective Java", "Joshua Bloch", 2018, "Programming", 10);
        Book book2 = new Book("Clean Code", "Robert C. Martin", 2008, "Programming", 5);
        service.addBook(book1);
        service.addBook(book2);

        // Exact match (ignoring case)
        List<Book> results = service.getBooksByTitle("Effective Java");
        assertEquals(1, results.size());
        assertEquals("Effective Java", results.get(0).getTitle());

        // Partial match
        results = service.getBooksByTitle("Java");
        assertEquals(1, results.size());
        assertEquals("Effective Java", results.get(0).getTitle());

        // Partial match (case-insensitive)
        results = service.getBooksByTitle("code");
        assertEquals(1, results.size());
        assertEquals("Clean Code", results.get(0).getTitle());

        // No match
        results = service.getBooksByTitle("Python");
        assertTrue(results.isEmpty());
    }
}
