package ru.gb.springdemo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.repository.ReaderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReaderService {
    private final ReaderRepository readerRepository;
    public Reader getReaderById(long id){
        return readerRepository.getReaderById(id);
    }
    public List<Reader> getAllReaders(){
        return readerRepository.getAllReaders();
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
