package ru.gb.springlibrary.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.gb.springlibrary.model.Reader;
import ru.gb.springlibrary.repository.ReaderRepository;

/**
 * Валидация читателя
 */
@Component
public class ReaderValidator implements Validator {
	private final ReaderRepository readerRepository;

	@Autowired
	public ReaderValidator(ReaderRepository readerRepository) {
		this.readerRepository = readerRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Reader.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Reader reader = (Reader) target;

		if (readerRepository.findReaderByName(reader.getName()).isPresent()) {
			errors.rejectValue("name", "", "Человек с таким именем уже существует");
		}

		if (!Character.isUpperCase(reader.getName().codePointAt(0)))
			errors.rejectValue("name", "", "Имя должно начинаться с заглавной буквы");
	}
}
