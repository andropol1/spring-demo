package ru.gb.springlibrary.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Конфигурация максимального количества книг на руках
 */
@ConfigurationProperties("application.issue")
@Data
public class IssueProperties {
	private int maxAllowedBooks = 2;
}
