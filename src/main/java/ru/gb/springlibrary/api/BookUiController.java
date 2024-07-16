package ru.gb.springlibrary.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.gb.springlibrary.model.Book;
import ru.gb.springlibrary.service.BookService;
import ru.gb.springlibrary.util.BookValidator;

import java.util.Optional;
import java.util.UUID;

/**
 * Контроллер для обратки запросов на управление книгами. Ответ HTML-страницы
 */
@Controller
@RequestMapping("/ui/book")
@Tag(name = "BooksUI", description = "HTML pages to manage books")
public class BookUiController {
	private final BookService bookService;
	private final BookValidator bookValidator;

	@Autowired
	public BookUiController(BookService bookService, BookValidator bookValidator) {
		this.bookService = bookService;
		this.bookValidator = bookValidator;
	}

	@GetMapping("/all")
	@Operation(
			summary = "Get all books",
			description = "You'll get the list of all allowed books"
	)
	public String getBooks(Model model) {
		model.addAttribute("books", bookService.getAllBooks());
		return "books";
	}

	@GetMapping("/new")
	@Operation(
			summary = "Page to add book",
			description = "You'll get the page, where you can send post-request"
	)
	public String addBook(@ModelAttribute("book") Book book) {
		return "book_add";
	}

	@GetMapping("/{id}")
	@Operation(
			summary = "Page to get book info",
			description = "You'll get the page, where you can find book owner, get back the book"
	)
	public String infoBook(@PathVariable UUID id, Model model) {
		Optional<Book> book = bookService.getBookById(id);
		if (book.isPresent()) {
			model.addAttribute("book", book.get());
			return "bookById";
		} else {
			return "bookNotFound";
		}
	}

	@PostMapping()
	@Operation(
			summary = "Creates and adds a new book to DB",
			description = "Creates a new book and adds it to the DB. Requires a right filled form"
	)
	public String create(@ModelAttribute("book") @Valid Book book,
						 BindingResult bindingResult) {
		bookService.addBook(book);
		return "redirect:/ui/book/all";
	}

	@GetMapping("/{id}/edit")
	public String edit(Model model, @PathVariable UUID id) {
		Optional<Book> bookById = bookService.getBookById(id);
		if (bookById.isPresent()) {
			model.addAttribute("book", bookById.get());
			return "book_edit";
		} else {
			return "redirect:/ui/book/all";
		}
	}

	@PutMapping("/{id}")
	public String update(@ModelAttribute("book") @Valid Book book,
						 BindingResult bindingResult,
						 @PathVariable UUID id) {

		bookValidator.validate(book, bindingResult);

		if (bindingResult.hasErrors()) {
			return "book_edit";
		}
		book.setId(id);
		bookService.updateBook(book);
		return "redirect:/ui/book/all";
	}
}
