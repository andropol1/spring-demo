package ru.gb.springdemo.repository;

import org.springframework.stereotype.Repository;
import ru.gb.springdemo.model.Issue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class IssueRepository {

  private List<Issue> issues;
  public Issue save(Issue issue) {
    issues.add(issue);
    return issue;
  }
  public Issue update(Long id) {
    Issue updatedIssue = getByID(id);
    if (updatedIssue != null) {
      updatedIssue.setReturnedAt(LocalDateTime.now());
    }
    return updatedIssue;
  }

  public void deleteById(Long id) {
    issues.remove(id);
  }
  public Issue getByID(Long id) {
    return issues.stream().filter(it -> Objects.equals(it.getId(), id))
            .findFirst()
            .orElse(null);
  }

  public List<Issue> getIssuesByReader(Long readerId) {
    return issues.stream()
            .filter(x -> Objects.equals(x.getReaderId(), readerId))
            .filter(i -> i.getReturnedAt() == null)
            .toList();
  }

}
