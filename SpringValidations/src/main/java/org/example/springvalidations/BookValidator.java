package org.example.springvalidations;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class BookValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;
        if (book.getPrice() < 1){
            errors.rejectValue("price", "price.invalid", "Price must be greater than 0");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"title","title.empty","Title must not be empty or whitespace");
    }
}
