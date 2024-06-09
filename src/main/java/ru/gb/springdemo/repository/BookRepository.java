package ru.gb.springdemo.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import ru.gb.springdemo.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class BookRepository {

  private final List<Book> books;

  public BookRepository() {
    this.books = new ArrayList<>();
  }

  @PostConstruct
  public void generateData() {
    books.addAll(List.of(
      new Book("война и мир"),
      new Book("метрвые души"),
      new Book("чистый код")
    ));
  }

  public Book getBookById(long id) {
    return books.stream().filter(it -> Objects.equals(it.getId(), id))
      .findFirst()
      .orElse(null);
  }
  public List<Book> getAllBooks(){
    return books;
  }
  public void deleteBookById(long id){
    books.removeIf(book -> Objects.equals(book.getId(), id));
  }
  public Book addBook(Book book){
    Book newBook = new Book(book.getName());
    books.add(newBook);
    return newBook;
  }
  public Book updateBook(long id, Book book){
    Book updatedBook = getBookById(id);
    updatedBook.setName(book.getName());
    return updatedBook;
  }

}
