package ru.gb.springdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gb.springdemo.model.Issue;

import java.util.List;

//@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
	List<Issue> findIssuesByReaderId(Long id);

/*	private List<Issue> issues;

	public IssueRepository(List<Issue> issues) {
		this.issues = issues;
	}

	@PostConstruct
	public void generateData() {
		issues.addAll(List.of(
				new Issue(1, 1),
				new Issue(2, 1)));
	}

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

	public List<Issue> getAllIssues() {
		return issues;
	}

	public List<Issue> getIssuesByReader(Long readerId) {
		return issues.stream()
				.filter(x -> Objects.equals(x.getReaderId(), readerId))
				.filter(i -> i.getReturnedAt() == null)
				.toList();
	}*/

}
