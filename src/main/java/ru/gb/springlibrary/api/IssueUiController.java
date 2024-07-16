package ru.gb.springlibrary.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.springlibrary.model.Reader;
import ru.gb.springlibrary.service.IssueService;
import ru.gb.springlibrary.service.ReaderService;

import java.util.Optional;
import java.util.UUID;

/**
 * Контроллер для обработки запросов на управление выдачами книг. Ответ HTML-страницы
 */
@Controller
@RequestMapping("/ui/issue")
@Tag(name = "IssuesUI", description = "HTML pages to manage issues.")
public class IssueUiController {
	private final ReaderService readerService;
	private final IssueService issueService;

	@Autowired
	public IssueUiController(ReaderService readerService, IssueService issueService) {
		this.readerService = readerService;
		this.issueService = issueService;
	}

	@GetMapping("/all")
	@Operation(
			summary = "Get all issues",
			description = "You'll get the list of all issues"
	)
	public String getIssues(Model model) {
		model.addAttribute("issues", issueService.getAllIssues());
		return "issues";
	}

	@GetMapping("/reader/{id}")
	@Operation(
			summary = "Get all issues of the specific reader",
			description = "You'll get the list of all issues of specific reader. You need to write reader's id."
	)
	public String getReaderIssues(@PathVariable UUID id, Model model) {
		Optional<Reader> reader = readerService.getReaderById(id);
		if (reader.isPresent()) {
			model.addAttribute("reader", reader.get());
			model.addAttribute("issues", issueService.getAllIssuesByReaderId(id));
			return "issuesByReaderId";
		} else {
			return "redirect:/ui/reader/all";
		}
	}

}
