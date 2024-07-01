package ru.gb.springdemo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

/**
 * Запись о факте выдачи книги (в БД)
 */
@Data
@Entity
@Table(name = "issues")
@AllArgsConstructor
@NoArgsConstructor
public class Issue {

	// public static long sequence = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "book_id")
	private Long bookId;
	@Column(name = "reader_id")
	private Long readerId;
	@Column(name = "reader_name")
	private String readerName;
	@Column(name = "book_name")
	private String bookName;
	@Column(name = "issued_at")
	private LocalDateTime issuedAt;
	@Column(name = "returned_at")
	private LocalDateTime returnedAt;

	public Issue(Long bookId, Long readerId) {
		this.bookId = bookId;
		this.readerId = readerId;
	}
	/*	public Issue(long bookId, long readerId) {
		this.id = sequence++;
		this.bookId = bookId;
		this.readerId = readerId;
		this.issuedAt = LocalDateTime.now();
	}*/

}
