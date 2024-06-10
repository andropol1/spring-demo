package ru.gb.springdemo.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.repository.ReaderRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReaderService {
	private final ReaderRepository readerRepository;

	public Optional<Reader> getReaderById(long id) {
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

	public void deleteReader(long id) {
		readerRepository.deleteById(id);
	}

	@PostConstruct
	void initTestData() {
		String[] names = {"Andy", "Fill", "Stev", "Mark"};
		long i = 1L;
		for (String name : names) {
			Reader reader = new Reader();
			reader.setName(name);
			reader.setId(i++);
			this.addReader(reader);
		}

	}

}
