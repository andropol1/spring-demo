package ru.gb.springlibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gb.springlibrary.model.Issue;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий, отвечающий за взаимодействие с БД по сущности Issue
 */
public interface IssueRepository extends JpaRepository<Issue, UUID> {
	List<Issue> findIssuesByReaderId(UUID id);

}
