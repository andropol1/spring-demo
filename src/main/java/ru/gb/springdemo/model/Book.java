package ru.gb.springdemo.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Book {
  public static long sequence = 1L;
  @NonNull
  private long id;
  @NonNull
  private String name;

  public Book(String name) {
    this(sequence++, name);
  }

}