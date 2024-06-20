package ru.gb.springdemo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.service.IssueService;

import java.util.NoSuchElementException;

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

	public ResponseEntity<Issue> getByID(@PathVariable Long id) {
		return issueService.getByID(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
/*		Issue issue = issueService.getByID(id);
		if (issue != null) {
			return new ResponseEntity<>(issue, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}*/
	}

	@PostMapping
	@Operation(
			summary = "Creates an issues",
			description = "Creates an issue using :readerId and :bookId from request."
	)
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
	public ResponseEntity<Issue> update(@PathVariable Long id, @RequestBody Issue issue) {
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
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		issueService.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
