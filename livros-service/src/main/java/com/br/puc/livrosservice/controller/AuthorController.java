package com.br.puc.livrosservice.controller;

import com.br.puc.livrosservice.dto.AuthorDTO;
import com.br.puc.livrosservice.model.Author;
import com.br.puc.livrosservice.service.AuthorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/author")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public Author createAuthor(@RequestBody AuthorDTO authorDTO) {
        return authorService.saveAuthor(authorDTO);
    }

    @GetMapping
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/{id}")
    public Author findBookById(@PathVariable("id") long id) {
        return authorService.getAuthorById(id);
    }
}
