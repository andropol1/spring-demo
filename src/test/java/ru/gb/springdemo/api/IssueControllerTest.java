package ru.gb.springdemo.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.gb.springdemo.JUnitSpringBootBase;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.service.BookService;
import ru.gb.springdemo.service.IssueService;
import ru.gb.springdemo.service.ReaderService;

import java.time.LocalDateTime;
import java.util.List;


class IssueControllerTest extends JUnitSpringBootBase {
	@Autowired
	WebTestClient webTestClient;
	@Autowired
	BookService bookService;
	@Autowired
	ReaderService readerService;
	@Autowired
	IssueService issueService;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class JUnitIssue {
		private Long id;
		private Long bookId;
		private Long readerId;
		private String readerName;
		private String bookName;
		private LocalDateTime issuedAt;
		private LocalDateTime returnedAt;
	}

	@Test
	void getIssueByIdSuccess() {
		Book book = bookService.addBook(new Book("War"));
		Reader reader = readerService.addReader(new Reader("Andrey"));
		Issue expected = issueService.saveIssue(new Issue(book.getId(), reader.getId()));
		JUnitIssue responseBody = webTestClient.get()
				.uri("/issue/" + expected.getId())
				.exchange()
				.expectStatus().isOk()
				.expectBody(JUnitIssue.class)
				.returnResult()
				.getResponseBody();
		Assertions.assertNotNull(responseBody);
		Assertions.assertEquals(expected.getId(), responseBody.getId());
		Assertions.assertEquals(expected.getBookId(), responseBody.getBookId());
		Assertions.assertEquals(expected.getReaderId(), responseBody.getReaderId());
		Assertions.assertEquals(expected.getIssuedAt(), responseBody.getIssuedAt());
		Assertions.assertEquals(expected.getBookName(), responseBody.getBookName());
		Assertions.assertEquals(expected.getReaderName(), responseBody.getReaderName());
		Assertions.assertNull(responseBody.getReturnedAt());
	}

	@Test
	void getIssueByIdNotFound() {
		webTestClient.get()
				.uri("/issue/" + Long.MAX_VALUE)
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	void getAllSuccess() {
		Book book = bookService.addBook(new Book("Peace"));
		Book book1 = bookService.addBook(new Book("Peace2"));
		Reader reader = readerService.addReader(new Reader("Andy"));
		issueService.saveIssue(new Issue(book.getId(), reader.getId()));
		issueService.saveIssue(new Issue(book1.getId(), reader.getId()));
		List<Issue> expected = issueService.getAllIssues();
		List<Issue> responseBody = webTestClient.get()
				.uri("/issue/all")
				.exchange()
				.expectStatus().isOk()
				.expectBody(new ParameterizedTypeReference<List<Issue>>() {
				})
				.returnResult()
				.getResponseBody();
		Assertions.assertNotNull(responseBody);
		Assertions.assertEquals(expected.size(), responseBody.size());
		for (Issue issue : responseBody) {
			boolean found = expected.stream()
					.filter(it -> it.getId().equals(issue.getId()))
					.filter(it -> it.getBookId().equals(issue.getBookId()))
					.anyMatch(it -> it.getReaderId().equals(issue.getReaderId()));
			Assertions.assertTrue(found);
		}
	}

}