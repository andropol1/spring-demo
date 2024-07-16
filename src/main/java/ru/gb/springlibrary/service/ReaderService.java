package ru.gb.springlibrary.service;

import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.springlibrary.model.Reader;
import ru.gb.springlibrary.repository.ReaderRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Бизнес-логика работы с читателями
 */
@Service
@RequiredArgsConstructor
public class ReaderService {
	private final ReaderRepository readerRepository;

	public Optional<Reader> getReaderById(UUID id) {
		return readerRepository.findById(id);
	}

	public List<Reader> getAllReaders() {
		return readerRepository.findAll();
	}

	public Reader addReader(Reader reader) {
		return readerRepository.save(reader);
	}

	public Reader updateReader(Reader reader) {
		return readerRepository.save(reader);
	}

	public void deleteReader(UUID id) {
		readerRepository.deleteById(id);
	}

	@PostConstruct
	void initTestData() {
		Faker faker = new Faker();
		for (int i = 0; i < 5; i++) {
			Reader reader = new Reader();
			reader.setId(UUID.randomUUID());
			reader.setName(faker.name().firstName());
			this.addReader(reader);
		}
	}

}
