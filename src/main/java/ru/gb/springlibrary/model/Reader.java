package ru.gb.springlibrary.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

/**
 * Сущность читателя (в БД)
 */
@Data
@Entity
@Table(name = "readers")
@Schema(name = "Читатель")
@AllArgsConstructor
@NoArgsConstructor
public class Reader {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid")
	private UUID id;
	@Column(name = "name")
	@Schema(name = "Имя")
	@NotEmpty
	@Size(min = 2, max = 100, message = "Имя не должно быть короче 2 символов или содержать более 100 символов")
	private String name;

	public Reader(String name) {
		this.name = name;
	}

}
