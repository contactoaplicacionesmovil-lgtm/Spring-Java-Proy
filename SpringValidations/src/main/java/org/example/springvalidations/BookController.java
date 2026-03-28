package org.example.springvalidations;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    @Autowired
    private BookRepository repository;
    private final BookValidator bookValidator = new BookValidator();

    @PostMapping("/addBook")
    public String addBook(@Valid @RequestBody Book book, BindingResult result) {
        bookValidator.validate(book, result);

        if(result.hasErrors()){
            return "Validation failed "+ result.getAllErrors();
        }
        repository.save(book);
        return "Book added successfully";
    }             

}
