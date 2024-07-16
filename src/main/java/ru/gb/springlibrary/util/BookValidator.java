package ru.gb.springlibrary.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.gb.springlibrary.model.Book;

/**
 * Валидация книги
 */
@Component
public class BookValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Book.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Book book = (Book) target;

		if (!Character.isUpperCase(book.getName().codePointAt(0)))
			errors.rejectValue("title", "", "Название книги должно начинаться с заглавной буквы");
	}
}