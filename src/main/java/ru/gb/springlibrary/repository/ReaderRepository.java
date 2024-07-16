package ru.gb.springlibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gb.springlibrary.model.Reader;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий, отвечающий за взаимодействие с БД по сущности Issue
 */
public interface ReaderRepository extends JpaRepository<Reader, UUID> {
	Optional<Reader> findReaderByName(String name);
}
