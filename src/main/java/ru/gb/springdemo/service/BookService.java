package ru.gb.springdemo.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.springdemo.aspect.RecoverException;
import ru.gb.springdemo.aspect.Timer;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
	private final BookRepository bookRepository;

	public Optional<Book> getBookById(long id) {
		return bookRepository.findById(id);
	}

	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	public void deleteBookById(long id) {
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
	@RecoverException(noRecoverFor = {ArithmeticException.class, NullPointerException.class})
	public void initTestData() {
		String[] books = {"War", "Piece", "Walk", "Soon"};
		long i = 1L;
		for (String name : books) {
			Book book = new Book();
			book.setName(name);
			book.setId(i++);
			this.addBook(book);
		}
	}
}
