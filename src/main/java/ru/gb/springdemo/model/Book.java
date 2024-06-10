package ru.gb.springdemo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Table(name = "books")
//@RequiredArgsConstructor
public class Book {
	//public static long sequence = 1L;
	//@NonNull
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	//@NonNull
	@Column(name = "name")
	private String name;

/*  public Book(String name) {
    this(sequence++, name);
  }*/

}
