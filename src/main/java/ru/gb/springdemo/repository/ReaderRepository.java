package ru.gb.springdemo.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gb.springdemo.model.Reader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long> {

/*  private final List<Reader> readers;

  public ReaderRepository() {
    this.readers = new ArrayList<>();
  }

  @PostConstruct
  public void generateData() {
    readers.addAll(List.of(
      new Reader("Игорь"),
            new Reader("Андрей")
    ));
  }

  public Reader getReaderById(long id) {
    return readers.stream().filter(it -> Objects.equals(it.getId(), id))
      .findFirst()
      .orElse(null);
  }
  public List<Reader> getAllReaders(){
    return readers;
  }
  public Reader addReader(Reader reader){
    Reader newReader = new Reader(reader.getName());
    readers.add(newReader);
    return newReader;
  }
  public Reader updateReader(long id, Reader reader){
    Reader updatedReader = getReaderById(id);
    updatedReader.setName(reader.getName());
    return updatedReader;
  }
  public void deleteReader(long id){
    readers.removeIf(reader -> Objects.equals(reader.getId(), id));
  }*/

}
