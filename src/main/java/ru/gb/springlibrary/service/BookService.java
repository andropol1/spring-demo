package ru.gb.springlibrary.service;

import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.springlibrary.aspect.Timer;
import ru.gb.springlibrary.model.Book;
import ru.gb.springlibrary.repository.BookRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Бизнес-логика работы с книгами
 */
@Service
@RequiredArgsConstructor
public class BookService {
	private final BookRepository bookRepository;

	public Optional<Book> getBookById(UUID id) {
		return bookRepository.findById(id);
	}

	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	public void deleteBookById(UUID id) {
		bookRepository.deleteById(id);
	}

	public Book addBook(Book book) {
		return bookRepository.save(book);
	}

	public Book updateBook(Book book) {
		return bookRepository.save(book);
	}

	@PostConstruct
	@Timer
	public void initTestData() {
		Faker faker = new Faker();
		for (int i = 0; i < 15; i++) {
			Book book = new Book();
			book.setId(UUID.randomUUID());
			book.setName(faker.book().title());
			this.addBook(book);
		}
	}
}
