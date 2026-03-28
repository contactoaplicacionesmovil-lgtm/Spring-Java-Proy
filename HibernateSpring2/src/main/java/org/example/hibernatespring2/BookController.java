package org.example.hibernatespring2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class BookController {

    @Autowired
    BookService bookService;


    @PostMapping("/saveBook")
    public String createBook(@RequestBody Book book) {
        bookService.saveBook(book);
        return "Book saved with ID" + book.getId();
    }

    @GetMapping("/getBooks")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }


    @PutMapping("/{id}")
    public String updateBook(@PathVariable Long id, @RequestBody Book book) {
        book.setId(id);
        bookService.updateBook(book);
        return "Book update "+ id;
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}
