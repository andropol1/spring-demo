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
import ru.gb.springlibrary.model.Reader;
import ru.gb.springlibrary.service.ReaderService;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер для обработки запросов на управление читателями
 */

@RestController
@RequestMapping("/reader")
@Tag(name = "Readers", description = "API to manage readers.")
public class ReaderController {
	@Autowired
	private ReaderService readerService;

	@GetMapping("/{id}")
	@Operation(
			summary = "Retrieves thr reader by :id",
			description = "The response is the reader object with id,name"
	)
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Reader.class), mediaType = "application/json")}),
			@ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
	})
	public ResponseEntity<Reader> getReaderById(@PathVariable UUID id) {
		return readerService.getReaderById(id).map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/all")
	@Operation(
			summary = "Get all readers",
			description = "Get the list of all readers"
	)
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = Reader.class)), mediaType = "application/json")}),
			@ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
	})
	public ResponseEntity<List<Reader>> getAll() {
		List<Reader> readers = readerService.getAllReaders();
		if (readers.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} else {
			return ResponseEntity.ok(readers);
		}
	}

	@PostMapping
	@Operation(
			summary = "Creates a reader",
			description = "Creates a reader and adds it to the Data Base."
	)
	@ApiResponses({
			@ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = Reader.class), mediaType = "application/json")}),
			@ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())})
	})
	public ResponseEntity<Reader> addReader(@RequestBody Reader reader) {
		if (reader != null) {
			return new ResponseEntity<>(readerService.addReader(reader), HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/{id}")
	@Operation(
			summary = "update reader",
			description = "Update reader info in db"
	)
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Reader.class), mediaType = "application/json")}),
			@ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
	})
	public ResponseEntity<Reader> updateReader(@PathVariable UUID id, @RequestBody Reader reader) {
		if (reader != null) {
			reader.setId(id);
			Reader updatedReader = readerService.updateReader(reader);
			return new ResponseEntity<>(updatedReader, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	@Operation(
			summary = "Deletes the reader by :id",
			description = "Deletes the reader by :id from the Data Base."
	)
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Reader.class))}),
			@ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())})
	})
	public ResponseEntity<Void> deleteReader(@PathVariable UUID id) {
		readerService.deleteReader(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
