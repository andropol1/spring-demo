package ru.gb.springdemo.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "readers")
@AllArgsConstructor
@NoArgsConstructor
public class Reader {

	//public static long sequence = 1L;
	//@NonNull
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	//@NonNull
	@Column(name = "name")
	private String name;

	public Reader(String name) {
		this.name = name;
	}
	/*public Reader(String name) {
		this(sequence++, name);
	}*/

}
