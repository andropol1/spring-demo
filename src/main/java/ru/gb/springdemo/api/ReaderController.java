package ru.gb.springdemo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.service.ReaderService;

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
	public ResponseEntity<Reader> getReaderById(@PathVariable long id) {
		return readerService.getReaderById(id).map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
/*        Reader reader = readerService.getReaderById(id);
        if (reader != null){
            return new ResponseEntity<>(reader, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }*/
	}

	@PostMapping
	@Operation(
			summary = "Creates a reader",
			description = "Creates a reader and adds it to the Data Base."
	)
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
	public ResponseEntity<Reader> updateReader(@PathVariable long id, @RequestBody Reader reader) {
		if (reader != null) {
			reader.setId(id);
			Reader updatedReader = readerService.updateReader(reader);
			return new ResponseEntity<>(updatedReader, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/{id}")
	@Operation(
			summary = "Deletes the reader by :id",
			description = "Deletes the reader by :id from the Data Base.")
	public ResponseEntity<Void> deleteReader(@PathVariable long id) {
		readerService.deleteReader(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
