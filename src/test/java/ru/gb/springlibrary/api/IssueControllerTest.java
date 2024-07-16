package ru.gb.springlibrary.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.gb.springlibrary.JUnitSpringBootBase;
import ru.gb.springlibrary.model.Book;
import ru.gb.springlibrary.model.Issue;
import ru.gb.springlibrary.model.Reader;
import ru.gb.springlibrary.repository.BookRepository;
import ru.gb.springlibrary.repository.IssueRepository;
import ru.gb.springlibrary.repository.ReaderRepository;

import java.time.LocalDate;
import java.util.List;


class IssueControllerTest extends JUnitSpringBootBase {
	@Autowired
	WebTestClient webTestClient;
	@Autowired
	BookRepository bookRepository;
	@Autowired
	ReaderRepository readerRepository;
	@Autowired
	IssueRepository issueRepository;

	@BeforeEach
	void cleanRepo() {
		issueRepository.deleteAll();
		bookRepository.deleteAll();
		readerRepository.deleteAll();
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class JUnitIssue {
		private Long id;
		private Long bookId;
		private Long readerId;
		private String readerName;
		private String bookName;
		private LocalDate issuedAt;
		private LocalDate returnedAt;

		public JUnitIssue(Long bookId, Long readerId) {
			this.bookId = bookId;
			this.readerId = readerId;
		}

		public JUnitIssue(Long bookId, Long readerId, LocalDate issuedAt) {
			this.bookId = bookId;
			this.readerId = readerId;
			this.issuedAt = issuedAt;
		}
	}

	@Test
	void getIssueByIdSuccess() {
		Book book = bookRepository.save(new Book("War"));
		Reader reader = readerRepository.save(new Reader("Andrey"));
		Issue expected = issueRepository.save(new Issue(book.getId(), reader.getId()));
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
		List<Book> bookList = bookRepository.saveAll(List.of(
				new Book("Peace"),
				new Book("Peace2")
		));
		Reader reader = readerRepository.save(new Reader("Andy"));
		issueRepository.saveAll(List.of(
				new Issue(bookList.get(0).getId(), reader.getId(), LocalDate.now()),
				new Issue(bookList.get(1).getId(), reader.getId(), LocalDate.now())
		));
		List<Issue> expected = issueRepository.findAll();
		List<JUnitIssue> responseBody = webTestClient.get()
				.uri("/issue/all")
				.exchange()
				.expectStatus().isOk()
				.expectBody(new ParameterizedTypeReference<List<JUnitIssue>>() {
				})
				.returnResult()
				.getResponseBody();
		Assertions.assertNotNull(responseBody);
		Assertions.assertEquals(expected.size(), responseBody.size());
		for (JUnitIssue jUnitIssue : responseBody) {
			boolean found = expected.stream()
					.filter(it -> it.getId().equals(jUnitIssue.getId()))
					.filter(it -> it.getBookId().equals(jUnitIssue.getBookId()))
					.filter(it -> it.getReaderId().equals(jUnitIssue.getReaderId()))
					.anyMatch(it -> it.getIssuedAt().equals(jUnitIssue.getIssuedAt()));
			Assertions.assertTrue(found);
		}
	}

	@Test
	void deleteIssueById() {
		Book book = new Book("War");
		Reader reader = new Reader("Rey");
		Issue issue = issueRepository.save(new Issue(book.getId(), reader.getId()));
		webTestClient.delete()
				.uri("/issue/" + issue.getId())
				.exchange()
				.expectStatus().isNoContent();
		Assertions.assertFalse(issueRepository.findById(issue.getId()).isPresent());
	}

	@Test
	void addIssueTest() {
		Book book = bookRepository.save(new Book("Peace"));
		Reader reader = readerRepository.save(new Reader("Pol"));
		JUnitIssue jUnitIssue = new JUnitIssue(book.getId(), reader.getId());
		JUnitIssue responseBody = webTestClient.post()
				.uri("/issue")
				.bodyValue(jUnitIssue)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(JUnitIssue.class)
				.returnResult().getResponseBody();
		Assertions.assertNotNull(responseBody);
		Assertions.assertNotNull(responseBody.getId());
		Assertions.assertEquals(responseBody.getBookId(), jUnitIssue.getBookId());
		Assertions.assertEquals(responseBody.getBookId(), jUnitIssue.getBookId());
		Assertions.assertEquals(responseBody.getReaderId(), jUnitIssue.getReaderId());
		Assertions.assertEquals(responseBody.getReaderName(), jUnitIssue.getReaderName());
		Assertions.assertEquals(responseBody.getBookName(), jUnitIssue.getBookName());
		Assertions.assertEquals(responseBody.getIssuedAt(), jUnitIssue.getIssuedAt());
		Assertions.assertTrue(issueRepository.findById(responseBody.getId()).isPresent());
	}

	@Test
	void updateIssueTest() {
		Book book = bookRepository.save(new Book("Java"));
		Reader reader = readerRepository.save(new Reader("Artem"));
		Issue saveIssue = issueRepository.save(new Issue(book.getId(), reader.getId()));
		JUnitIssue updatedIssue = new JUnitIssue(book.getId(), reader.getId());
		JUnitIssue responseBody = webTestClient.put()
				.uri("/issue/" + saveIssue.getId())
				.bodyValue(updatedIssue)
				.exchange()
				.expectStatus().isOk()
				.expectBody(JUnitIssue.class)
				.returnResult().getResponseBody();
		Assertions.assertNotNull(responseBody);
		Assertions.assertEquals(responseBody.getId(), saveIssue.getId());
		Assertions.assertEquals(responseBody.getBookId(), saveIssue.getBookId());
		Assertions.assertEquals(responseBody.getReaderId(), saveIssue.getReaderId());
		Assertions.assertEquals(responseBody.getBookName(), saveIssue.getBookName());
		Assertions.assertEquals(responseBody.getReaderName(), saveIssue.getReaderName());
		Assertions.assertEquals(responseBody.getIssuedAt(), saveIssue.getIssuedAt());
		Assertions.assertEquals(responseBody.getReturnedAt(), saveIssue.getReturnedAt());
		Assertions.assertTrue(issueRepository.findById(responseBody.getId()).isPresent());
	}

}