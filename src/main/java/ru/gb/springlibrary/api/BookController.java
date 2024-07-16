package ru.gb.springlibrary.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.springlibrary.model.Book;
import ru.gb.springlibrary.service.BookService;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер для обратки запросов на управление книгами
 */
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
	public ResponseEntity<Book> getBookById(@PathVariable UUID id) {
		return bookService.getBookById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/all")
	@Operation(
			summary = "Get all books",
			description = "You'll get the list of all allowed books"
	)
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = Book.class)), mediaType = "application/json")}),
			@ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
	})
	public ResponseEntity<List<Book>> getAll() {
		List<Book> books = bookService.getAllBooks();
		if (books.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} else {
			return ResponseEntity.ok(books);
		}
	}

	@DeleteMapping("/{id}")
	@Operation(
			summary = "Delete book",
			description = "Delete book from the DB without answer"
	)
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Book.class))}),
			@ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())})
	})
	public ResponseEntity<Void> deleteBookById(@PathVariable UUID id) {
		bookService.deleteBookById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping
	@Operation(
			summary = "Creates and adds a new book to DB",
			description = "Creates a new book and adds it to the DB. Requires a json body."
	)
	@ApiResponses({
			@ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = Book.class), mediaType = "application/json")}),
			@ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())})
	})
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
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Book.class), mediaType = "application/json")}),
			@ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
	})
	public ResponseEntity<Book> updateBook(@PathVariable UUID id, @RequestBody Book book) {
		if (book != null) {
			book.setId(id);
			Book updatedBook = bookService.updateBook(book);
			return new ResponseEntity<>(updatedBook, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
