package ru.gb.springlibrary.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Запись о факте выдачи книги (в БД)
 */
@Data
@Entity
@Table(name = "issues")
@Schema(name = "Выдача книги")
@AllArgsConstructor
@NoArgsConstructor
public class Issue {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid")
	private UUID id;
	@Column(name = "book_id")
	private UUID bookId;
	@Column(name = "reader_id")
	private UUID readerId;
	@Column(name = "reader_name")
	private String readerName;
	@Column(name = "book_name")
	private String bookName;
	@Column(name = "issued_at")
	private LocalDate issuedAt;
	@Column(name = "returned_at")
	private LocalDate returnedAt;

	public Issue(UUID bookId, UUID readerId) {
		this.bookId = bookId;
		this.readerId = readerId;
	}

	public Issue(UUID bookId, UUID readerId, LocalDate issuedAt) {
		this.bookId = bookId;
		this.readerId = readerId;
		this.issuedAt = issuedAt;
	}
}
