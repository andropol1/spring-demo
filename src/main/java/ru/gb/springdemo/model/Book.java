package ru.gb.springdemo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "books")
@Schema(name = "Книга")
@NoArgsConstructor
@AllArgsConstructor
public class Book {
	//public static long sequence = 1L;
	//@NonNull
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(name = "ID")
	private Long id;
	//@NonNull
	@Column(name = "name")
	@Schema(name = "Имя")
	private String name;

	public Book(String name) {
		this.name = name;
	}

}
