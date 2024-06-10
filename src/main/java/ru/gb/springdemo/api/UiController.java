package ru.gb.springdemo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.gb.springdemo.service.BookService;
import ru.gb.springdemo.service.IssueService;
import ru.gb.springdemo.service.ReaderService;

@Controller
@RequestMapping("/ui")
public class UiController {
	private final ReaderService readerService;
	private final BookService bookService;
	private final IssueService issueService;

	@Autowired
	public UiController(ReaderService readerService, BookService bookService, IssueService issueService) {
		this.readerService = readerService;
		this.bookService = bookService;
		this.issueService = issueService;
	}

	@GetMapping("/books")
	public String getBooks(Model model) {
		model.addAttribute("books", bookService.getAllBooks());
		return "books";
	}

	@GetMapping("/readers")
	public String getReaders(Model model) {
		model.addAttribute("readers", readerService.getAllReaders());
		return "readers";
	}

	@GetMapping("/issues")
	public String getIssues(Model model) {
		model.addAttribute("issues", issueService.getAllIssues());
		return "issues";
	}

	@GetMapping("/reader/{id}")
	public String getReaderIssues(@PathVariable long id, Model model) {
		model.addAttribute("readerById", issueService.getAllIssuesByReaderId(id));
		return "readerById";
	}
}
