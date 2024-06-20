package ru.gb.springdemo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.service.BookService;

@RestController
@RequestMapping("/book")
@Tag(name = "Books", description = "API to manage books")
public class BookController {
	@Autowired
	private BookService bookService;

	@GetMapping("/{id}")
	@Operation(
			summary = "Retrieves the book by :id",
			description = "The response is book object with id,name."
	)
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Book.class), mediaType = "application/json")}),
			@ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
	})
	public ResponseEntity<Book> getBookById(@PathVariable long id) {
		return bookService.getBookById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
/*		Book book = bookService.getBookById(id);
		if (book != null) {
			return new ResponseEntity<>(book, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}*/
	}

	@DeleteMapping("/{id}")
	@Operation(
			summary = "Delete book",
			description = "Delete book from the DB without answer"
	)
	public ResponseEntity<Void> deleteBookById(@PathVariable long id) {
		bookService.deleteBookById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping
	@Operation(
			summary = "Creates and adds a new book to DB",
			description = "Creates a new book and adds it to the DB. Requires a json body."
	)
	public ResponseEntity<Book> addBook(@RequestBody Book book) {
		if (book != null) {
			return new ResponseEntity<>(bookService.addBook(book), HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/{id}")
	@Operation(
			summary = "Update book",
			description = "Update book in db"
	)
	public ResponseEntity<Book> updateBook(@PathVariable long id, @RequestBody Book book) {
		if (book != null) {
			book.setId(id);
			Book updatedBook = bookService.updateBook(book);
			return new ResponseEntity<>(updatedBook, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
