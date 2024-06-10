package ru.gb.springdemo.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.repository.BookRepository;
import ru.gb.springdemo.repository.IssueRepository;
import ru.gb.springdemo.repository.ReaderRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@DependsOn({"bookService", "readerService"})
public class IssueService {
	private final BookRepository bookRepository;
	private final ReaderRepository readerRepository;
	private final IssueRepository issueRepository;
	@Value("${application.max-allowed-books:1}")
	private int maxAllowedBooks;

	@Autowired
	public IssueService(BookRepository bookRepository, ReaderRepository readerRepository,
						IssueRepository issueRepository) {
		this.bookRepository = bookRepository;
		this.readerRepository = readerRepository;
		this.issueRepository = issueRepository;
	}

	public Optional<Issue> getByID(Long id) {
		return issueRepository.findById(id);
	}

	public List<Issue> getAllIssues() {
		return issueRepository.findAll();
	}

	public List<Issue> getAllIssuesByReaderId(long id) {
		List<Issue> list = issueRepository.findIssuesByReaderId(id);
		for (Issue issue :
				list) {
			if (readerRepository.findById(id).isPresent()) {
				issue.setReaderName(readerRepository.findById(id).get().getName());
			}
			if (bookRepository.findById(issue.getBookId()).isPresent()) {
				issue.setBookName(bookRepository.findById(issue.getBookId()).get().getName());
			}
		}
		return list;
	}

	public Issue saveIssue(Issue issue) {
		if (bookRepository.findById(issue.getBookId()).isEmpty()) {
			throw new NoSuchElementException("Не найдена книга с идентификатором \"" + issue.getBookId() + "\"");
		}
		if (readerRepository.findById(issue.getReaderId()).isEmpty()) {
			throw new NoSuchElementException("Не найден читатель с идентификатором \"" + issue.getReaderId() + "\"");
		}
		if (maxAllowedBooks <= issueRepository.findIssuesByReaderId(issue.getReaderId()).size()) {
			throw new RuntimeException("Читателю \"" + issue.getReaderId() + "\" отказано в выдаче по причине " +
					"максимального количества книг на руках. Необходимо осуществить предварительный возврат книг.");
		}
		return issueRepository.save(issue);
	}

	public Issue update(Issue issue) {
		return issueRepository.save(issue);
	}

	public void deleteById(Long id) {
		issueRepository.deleteById(id);
	}

	@PostConstruct
	void initTestData() {
		Issue issue = new Issue();
		issue.setBookId(1L);
		issue.setReaderId(1L);
		this.saveIssue(issue);
	}

}
