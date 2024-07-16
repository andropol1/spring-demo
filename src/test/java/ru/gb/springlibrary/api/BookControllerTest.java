package ru.gb.springlibrary.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.gb.springlibrary.JUnitSpringBootBase;
import ru.gb.springlibrary.model.Book;
import ru.gb.springlibrary.repository.BookRepository;

import java.util.List;

class BookControllerTest extends JUnitSpringBootBase {
	@Autowired
	WebTestClient webTestClient;
	@Autowired
	BookRepository bookRepository;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class JUnitBook {
		private Long id;
		private String name;

		public JUnitBook(String name) {
			this.name = name;
		}
	}

	@Test
	void getBookByIdSuccess() {
		Book expected = bookRepository.save(new Book("War and Peace"));
		JUnitBook responseBody = webTestClient.get()
				.uri("/book/" + expected.getId())
				.exchange()
				.expectStatus().isOk()
				.expectBody(JUnitBook.class)
				.returnResult().getResponseBody();
		Assertions.assertNotNull(responseBody);
		Assertions.assertEquals(expected.getId(), responseBody.getId());
		Assertions.assertEquals(expected.getName(), responseBody.getName());
	}

	@Test
	void getBookByIdNotFound() {
		webTestClient.get()
				.uri("/book/" + Long.MAX_VALUE)
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	void getAllSuccess() {
		bookRepository.saveAll(List.of(
				new Book("Crime"),
				new Book("Single")
		));
		List<Book> expected = bookRepository.findAll();
		List<JUnitBook> responseBody = webTestClient.get()
				.uri("/book/all")
				.exchange()
				.expectStatus().isOk()
				.expectBody(new ParameterizedTypeReference<List<JUnitBook>>() {
				})
				.returnResult()
				.getResponseBody();
		Assertions.assertNotNull(responseBody);
		Assertions.assertEquals(expected.size(), responseBody.size());
		for (JUnitBook jUnitBook : responseBody) {
			boolean found = expected.stream()
					.filter(it -> it.getId().equals(jUnitBook.getId()))
					.anyMatch(it -> it.getName().equals(jUnitBook.getName()));
			Assertions.assertTrue(found);
		}
	}

	@Test
	void deleteBookById() {
		Book book = bookRepository.save(new Book("Day"));
		webTestClient.delete()
				.uri("/book/" + book.getId())
				.exchange()
				.expectStatus().isNoContent();
		Assertions.assertFalse(bookRepository.findById(book.getId()).isPresent());

	}

	@Test
	void addBookTest() {
		JUnitBook book = new JUnitBook("Will");
		JUnitBook responseBody = webTestClient.post()
				.uri("/book")
				.bodyValue(book)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(JUnitBook.class)
				.returnResult().getResponseBody();
		Assertions.assertNotNull(responseBody);
		Assertions.assertNotNull(responseBody.getId());
		Assertions.assertEquals(book.getName(), responseBody.getName());
		Assertions.assertTrue(bookRepository.findById(responseBody.getId()).isPresent());
	}

	@Test
	void updateBookTest() {
		Book updatedBook = bookRepository.save(new Book("War"));
		JUnitBook requestForUpdate = new JUnitBook("Update");
		JUnitBook responseBody = webTestClient.put()
				.uri("/book/" + updatedBook.getId())
				.bodyValue(requestForUpdate)
				.exchange()
				.expectStatus().isOk()
				.expectBody(JUnitBook.class)
				.returnResult().getResponseBody();
		Assertions.assertNotNull(responseBody);
		Assertions.assertEquals(responseBody.getId(), updatedBook.getId());
		Assertions.assertEquals(requestForUpdate.getName(), responseBody.getName());
	}
}