package ru.gb.springdemo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.service.IssueService;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/issue")
public class IssueController {
	@Autowired
	private IssueService issueService;

	@GetMapping("/{id}")
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
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		issueService.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
