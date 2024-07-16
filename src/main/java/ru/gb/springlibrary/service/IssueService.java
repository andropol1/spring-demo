package ru.gb.springlibrary.service;

import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import ru.gb.springlibrary.aspect.Timer;
import ru.gb.springlibrary.model.Issue;
import ru.gb.springlibrary.repository.BookRepository;
import ru.gb.springlibrary.repository.IssueRepository;
import ru.gb.springlibrary.repository.ReaderRepository;
import ru.gb.springlibrary.util.IssueProperties;

import java.time.ZoneId;
import java.util.*;

/**
 * Бизнес-логика работы с выдачей книг
 */
@Service
@DependsOn({"bookService", "readerService"})
@Timer
public class IssueService {
	private final BookRepository bookRepository;
	private final ReaderRepository readerRepository;
	private final IssueRepository issueRepository;
	private final IssueProperties issueProperties;

	@Autowired
	public IssueService(BookRepository bookRepository, ReaderRepository readerRepository,
						IssueRepository issueRepository, IssueProperties issueProperties) {
		this.bookRepository = bookRepository;
		this.readerRepository = readerRepository;
		this.issueRepository = issueRepository;
		this.issueProperties = issueProperties;
	}

	public Optional<Issue> getByID(UUID id) {
		return issueRepository.findById(id);
	}

	public List<Issue> getAllIssues() {
		return issueRepository.findAll();
	}

	public List<Issue> getAllIssuesByReaderId(UUID id) {
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
		if (issueProperties.getMaxAllowedBooks() <= issueRepository.findIssuesByReaderId(issue.getReaderId()).size()) {
			throw new RuntimeException("Читателю \"" + issue.getReaderId() + "\" отказано в выдаче по причине " +
					"максимального количества книг на руках. Необходимо осуществить предварительный возврат книг.");
		}
		return issueRepository.save(issue);
	}

	public Issue update(Issue issue) {
		return issueRepository.save(issue);
	}

	public void deleteById(UUID id) {
		issueRepository.deleteById(id);
	}

	@PostConstruct
	void initTestData() {
		Faker faker = new Faker();
		for (int i = 0; i < 5; i++) {
			Issue issue = new Issue();
			Random r = new Random();
			issue.setBookId(bookRepository.findAll().get(r.nextInt(bookRepository.findAll().size())).getId());
			issue.setReaderId(readerRepository.findAll().get(r.nextInt(readerRepository.findAll().size())).getId());
			issue.setBookName(bookRepository.findById(issue.getBookId()).get().getName());
			issue.setReaderName(readerRepository.findById(issue.getReaderId()).get().getName());
			Date between = faker.date().between(startOfYear(), endOfYear());
			issue.setIssuedAt(between.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			this.saveIssue(issue);
		}
	}

	private Date startOfYear() {
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.YEAR, 2024);
		instance.set(Calendar.MONTH, Calendar.JANUARY);
		instance.set(Calendar.DAY_OF_MONTH, 1);
		return instance.getTime();
	}

	private Date endOfYear() {
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.YEAR, 2024);
		instance.set(Calendar.MONTH, Calendar.DECEMBER);
		instance.set(Calendar.DAY_OF_MONTH, 1);
		return instance.getTime();
	}

}
