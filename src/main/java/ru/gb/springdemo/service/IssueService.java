package ru.gb.springdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.repository.BookRepository;
import ru.gb.springdemo.repository.IssueRepository;
import ru.gb.springdemo.repository.ReaderRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
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
  public Issue getByID(Long id) {
    return issueRepository.getByID(id);
  }
  public List<Issue> getAllIssues(){
    return issueRepository.getAllIssues();
  }
  public List<Issue> getAllIssuesByReaderId(long id){
    List<Issue> list = issueRepository.getIssuesByReader(id);
    for (Issue issue:
         list) {
        issue.setReaderName(readerRepository.getReaderById(id).getName());
        issue.setBookName(bookRepository.getBookById(issue.getBookId()).getName());
    }
    return list;
  }

  public Issue saveIssue(Issue issue) {
    if (bookRepository.getBookById(issue.getBookId()) == null) {
      throw new NoSuchElementException("Не найдена книга с идентификатором \"" + issue.getBookId() + "\"");
    }
    if (readerRepository.getReaderById(issue.getReaderId()) == null) {
      throw new NoSuchElementException("Не найден читатель с идентификатором \"" + issue.getReaderId() + "\"");
    }
    if (maxAllowedBooks <= issueRepository.getIssuesByReader(issue.getReaderId()).size()) {
      throw new RuntimeException("Читателю \"" + issue.getReaderId() + "\" отказано в выдаче по причине " +
              "максимального количества книг на руках. Необходимо осуществить предварительный возврат книг.");
    }
    return issueRepository.save(issue);
  }

  public Issue update(Long id) {
    return issueRepository.update(id);
  }

  public void deleteById(Long id) {
    issueRepository.deleteById(id);
  }

}
