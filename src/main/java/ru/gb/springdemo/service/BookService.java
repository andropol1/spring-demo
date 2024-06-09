package ru.gb.springdemo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.springdemo.model.Book;
import ru.gb.springdemo.repository.BookRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    public Book getBookById(long id){
        return bookRepository.getBookById(id);
    }
    public List<Book> getAllBooks(){
        return bookRepository.getAllBooks();
    }
    public void deleteBookById(long id){
        bookRepository.deleteBookById(id);
    }
    public Book addBook(Book book){
        return bookRepository.addBook(book);
    }
    public Book updateBook(long id, Book book){
        return bookRepository.updateBook(id, book);
    }
}
