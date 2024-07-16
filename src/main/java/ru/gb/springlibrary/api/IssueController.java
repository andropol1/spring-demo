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
import ru.gb.springlibrary.model.Issue;
import ru.gb.springlibrary.service.IssueService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Контроллер для обработки запросов на управление выдачами книг
 */
@RestController
@RequestMapping("/issue")
@Tag(name = "Issues", description = "API to manage issues.")
public class IssueController {
	@Autowired
	private IssueService issueService;

	@GetMapping("/{id}")
	@Operation(
			summary = "Retrieves the issue by :id",
			description = "The response is the issue object with id,readerId,bookId,issued_at,returned_at."
	)
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Issue.class), mediaType = "application/json")}),
			@ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
	})
	public ResponseEntity<Issue> getByID(@PathVariable UUID id) {
		return issueService.getByID(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/all")
	@Operation(
			summary = "Get all issues",
			description = "Get the list of all existed issues"
	)
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = Issue.class)), mediaType = "application/json")}),
			@ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
	})
	public ResponseEntity<List<Issue>> getAll() {
		List<Issue> issues = issueService.getAllIssues();
		if (issues.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(issues, HttpStatus.OK);
		}
	}

	@PostMapping
	@Operation(
			summary = "Creates an issues",
			description = "Creates an issue using :readerId and :bookId from request."
	)
	@ApiResponses({
			@ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = Issue.class), mediaType = "application/json")}),
			@ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
			@ApiResponse(responseCode = "409", content = {@Content(schema = @Schema())})
	})
	public ResponseEntity<Issue> create(@RequestBody Issue issue) {
		if (issue != null) {
			try {
				issue = issueService.saveIssue(issue);
			} catch (NoSuchElementException e) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} catch (RuntimeException ex) {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
			return new ResponseEntity<>(issue, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/{id}")
	@Operation(
			summary = "Update issue",
			description = "Updates issue's returned_at field, book considered as \\\"RETURNED\\\""
	)
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = Issue.class)), mediaType = "application/json")}),
			@ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())})
	})
	public ResponseEntity<Issue> update(@PathVariable UUID id, @RequestBody Issue issue) {
		if (issue != null) {
			issue.setId(id);
			Issue updatedIssue = issueService.update(issue);
			return new ResponseEntity<>(updatedIssue, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/{id}")
	@Operation(
			summary = "Delete issue",
			description = "Delete issue from db"
	)
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Issue.class))}),
			@ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())})
	})
	public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
		issueService.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
