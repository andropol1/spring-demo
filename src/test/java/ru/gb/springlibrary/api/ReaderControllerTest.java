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
import ru.gb.springlibrary.model.Reader;
import ru.gb.springlibrary.repository.ReaderRepository;

import java.util.List;
import java.util.UUID;

public class ReaderControllerTest extends JUnitSpringBootBase {
	@Autowired
	WebTestClient webTestClient;

	@Autowired
	ReaderRepository readerRepository;

	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	static class JUnitReader {
		private UUID id;
		private String name;

		public JUnitReader(String name) {
			this.name = name;
		}
	}

	@Test
	void getReaderByIdSuccess() {
		Reader expected = readerRepository.save(new Reader("Andy"));
		JUnitReader responseBody = webTestClient.get()
				.uri("/reader/" + expected.getId())
				.exchange()
				.expectStatus().isOk()
				.expectBody(JUnitReader.class)
				.returnResult().getResponseBody();
		Assertions.assertNotNull(responseBody);
		Assertions.assertEquals(expected.getId(), responseBody.getId());
		Assertions.assertEquals(expected.getId(), responseBody.getId());
	}

	@Test
	void getReaderByIdNotFound() {
		webTestClient.get()
				.uri("/reader/" + UUID.randomUUID())
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	void getAllSuccess() {
		readerRepository.saveAll(List.of(
				new Reader("Carl"),
				new Reader("Sam")
		));
		List<Reader> expected = readerRepository.findAll();
		List<JUnitReader> responseBody = webTestClient.get()
				.uri("/reader/all")
				.exchange()
				.expectStatus().isOk()
				.expectBody(new ParameterizedTypeReference<List<JUnitReader>>() {
				})
				.returnResult()
				.getResponseBody();
		Assertions.assertNotNull(responseBody);
		Assertions.assertEquals(expected.size(), responseBody.size());
		for (JUnitReader jUnitReader : responseBody) {
			boolean found = expected.stream()
					.filter(it -> it.getId().equals(jUnitReader.getId()))
					.anyMatch(it -> it.getName().equals(jUnitReader.getName()));
			Assertions.assertTrue(found);
		}

	}

	@Test
	void deleteReaderById() {
		Reader reader = readerRepository.save(new Reader("Any"));
		webTestClient.delete()
				.uri("/reader/" + reader.getId())
				.exchange()
				.expectStatus().isNoContent();
		Assertions.assertFalse(readerRepository.findById(reader.getId()).isPresent());
	}

	@Test
	void addReaderTest() {
		JUnitReader reader = new JUnitReader("Sam");
		JUnitReader responseBody = webTestClient.post()
				.uri("/reader")
				.bodyValue(reader)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(JUnitReader.class)
				.returnResult().getResponseBody();
		Assertions.assertNotNull(responseBody);
		Assertions.assertNotNull(responseBody.getId());
		Assertions.assertEquals(reader.getName(), responseBody.getName());
		Assertions.assertTrue(readerRepository.findById(responseBody.getId()).isPresent());
	}

	@Test
	void updateBookTest() {
		Reader updatedReader = readerRepository.save(new Reader("Ray"));
		JUnitReader requestForUpdate = new JUnitReader("Update");
		JUnitReader responseBody = webTestClient.put()
				.uri("/reader/" + updatedReader.getId())
				.bodyValue(requestForUpdate)
				.exchange()
				.expectStatus().isOk()
				.expectBody(JUnitReader.class)
				.returnResult().getResponseBody();
		Assertions.assertNotNull(responseBody);
		Assertions.assertEquals(responseBody.getId(), updatedReader.getId());
		Assertions.assertEquals(requestForUpdate.getName(), responseBody.getName());
	}
}
