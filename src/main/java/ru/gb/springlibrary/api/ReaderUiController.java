package ru.gb.springlibrary.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.gb.springlibrary.model.Reader;
import ru.gb.springlibrary.service.ReaderService;
import ru.gb.springlibrary.util.ReaderValidator;

import java.util.Optional;
import java.util.UUID;

/**
 * Контроллер для обработки запросов на управление читателями. Ответ HTML-страницы
 */
@Controller
@RequestMapping("/ui/reader")
@Tag(name = "ReadersUI", description = "HTML pages to manage readers.")
public class ReaderUiController {
	private final ReaderService readerService;
	private final ReaderValidator readerValidator;

	@Autowired
	public ReaderUiController(ReaderService readerService, ReaderValidator readerValidator) {
		this.readerService = readerService;
		this.readerValidator = readerValidator;
	}

	@GetMapping("/all")
	@Operation(
			summary = "Get all readers",
			description = "Get the list of all readers"
	)
	public String getReaders(Model model) {
		model.addAttribute("readers", readerService.getAllReaders());
		return "readers";
	}

	@GetMapping("/new")
	@Operation(
			summary = "Page to add reader",
			description = "You'll get the page, where you can send post-request"
	)
	public String addReader(@ModelAttribute("reader") Reader reader) {
		return "reader_add";
	}

	@PostMapping()
	@Operation(
			summary = "Creates and adds a new reader to DB",
			description = "Creates a new reader and adds it to the DB. Requires a right filled form"
	)
	public String create(@ModelAttribute("reader") @Valid Reader reader,
						 BindingResult bindingResult) {

		readerValidator.validate(reader, bindingResult);

		if (bindingResult.hasErrors()) {
			return "reader_add";
		}

		readerService.addReader(reader);
		return "redirect:/ui/reader/all";
	}

	@GetMapping("/{id}/edit")
	public String edit(Model model, @PathVariable UUID id) {
		Optional<Reader> reader = readerService.getReaderById(id);
		if (reader.isPresent()) {
			model.addAttribute("reader", reader.get());
			return "reader_edit";
		} else {
			return "redirect:/ui/reader/all";
		}
	}

	@PutMapping("/{id}")
	public String update(@ModelAttribute("book") @Valid Reader reader,
						 BindingResult bindingResult,
						 @PathVariable UUID id) {

		readerValidator.validate(reader, bindingResult);

		if (bindingResult.hasErrors()) {
			return "reader_edit";
		}
		reader.setId(id);
		readerService.updateReader(reader);
		return "redirect:/ui/reader/all";
	}
}
