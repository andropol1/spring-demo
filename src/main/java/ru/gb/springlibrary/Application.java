package ru.gb.springlibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.gb.springlibrary.util.IssueProperties;

/**
 * Входная точка в приложение
 */
@SpringBootApplication
@EnableConfigurationProperties(IssueProperties.class)
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
