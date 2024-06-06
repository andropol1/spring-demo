package ru.gb.springdemo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.springdemo.model.Reader;
import ru.gb.springdemo.service.ReaderService;

@RestController
@RequestMapping("/reader")
public class ReaderController {
    @Autowired
    private ReaderService readerService;
    @GetMapping("/{id}")
    public ResponseEntity<Reader> getReaderById(@PathVariable long id){
        Reader reader = readerService.getReaderById(id);
        if (reader != null){
            return new ResponseEntity<>(reader, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping
    public ResponseEntity<Reader> addReader(@RequestBody Reader reader){
        if (reader != null){
            return new ResponseEntity<>(readerService.addReader(reader), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Reader> updateReader(@PathVariable long id, @RequestBody Reader reader){
        if (reader != null){
            Reader updatedReader = readerService.getReaderById(id);
            if (updatedReader != null){
                return new ResponseEntity<>(readerService.updateReader(id, reader), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReader(@PathVariable long id){
        Reader deletedReader = readerService.getReaderById(id);
        if (deletedReader != null){
            readerService.deleteReader(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
