package ru.gb.springlibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gb.springlibrary.model.Book;

import java.util.UUID;

/**
 * Репозиторий, отвечающий за взаимодействие с БД по сущности Book
 */
public interface BookRepository extends JpaRepository<Book, UUID> {


}
