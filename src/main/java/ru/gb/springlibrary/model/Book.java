package ru.gb.springlibrary.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

/**
 * Сущность книги (в БД)
 */
@Data
@Entity
@Table(name = "books")
@Schema(name = "Книга")
@NoArgsConstructor
@AllArgsConstructor
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Schema(name = "ID")
	@Column(columnDefinition = "uuid")
	private UUID id;
	@Column(name = "name")
	@Schema(name = "Название")
	@NotEmpty
	@Size(min = 2, max = 100, message = "Название не должно быть короче 2 символов или содержать более 100 символов")
	private String name;

	public Book(String name) {
		this.name = name;
	}

}
