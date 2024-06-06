package ru.gb.springdemo.service;

import org.springframework.stereotype.Service;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.repository.ReaderRepository;

@Service
public class ReaderService {
    private ReaderRepository readerRepository;
    public Reader getReaderById(long id){
        return readerRepository.getReaderById(id);
    }
    public Reader addReader(Reader reader){
        return readerRepository.addReader(reader);
    }
    public Reader updateReader(long id, Reader reader){
        return readerRepository.updateReader(id, reader);
    }
    public void deleteReader(long id){
        readerRepository.deleteReader(id);
    }
}
