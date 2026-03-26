package com.br.puc.livrosservice.service;

import com.br.puc.livrosservice.dto.BookDTO;
import com.br.puc.livrosservice.model.Author;
import com.br.puc.livrosservice.model.Book;
import com.br.puc.livrosservice.model.Genre;
import com.br.puc.livrosservice.repository.BookRepository;
import com.br.puc.livrosservice.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BookService {

    private final BookRepository bookRepository;

    private final AuthorService authorService;

    private final GenreRepository genreRepository;

    public BookService(BookRepository bookRepository, AuthorService authorService, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.genreRepository = genreRepository;
    }

    public Book saveBook(BookDTO book) {

        Author author = authorService.getAuthorById(book.author());

        if(author == null) {
            throw new RuntimeException();
        }

        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(book.genres()));

        if(genres.size() != book.genres().size()) {
            throw new RuntimeException();
        }

        Book newBook = new Book(
                book.title(),
                author,
                book.isbn(),
                genres,
                book.pages()
        );

        return bookRepository.save(newBook);
    }

    public List<Book> findBooks() {
        return bookRepository.findAll();
    }

    public Book findBookById(long id) {
        return bookRepository.findById(id)
                .orElse(null);
    }
}
