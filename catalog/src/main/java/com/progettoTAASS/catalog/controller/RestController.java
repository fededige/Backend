package com.progettoTAASS.catalog.controller;

import com.progettoTAASS.catalog.model.Book;
import com.progettoTAASS.catalog.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/")
@org.springframework.web.bind.annotation.RestController
public class RestController {
    @Autowired
    private BookRepository bookRepository;

    /**
     * This method is used to get a book by ID
     * @param id the book id
     * @return Book
     */
    @GetMapping("/books/getBook/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable int id){
        Book book = bookRepository.findById(id);
        return book != null ? ResponseEntity.ok(book) : ResponseEntity.notFound().build();
    }

    /**
     * This method is used to get all books by the content of a string. The result is a UNION of the same search
     * executed on three different column: author, genre and title.
     * @param searchContent the string the needs to be used by the search
     * @return List<Book>
     */
    @GetMapping("/books/search/{searchContent}")
    public ResponseEntity<List<Book>> getBooksBySearchContent(@PathVariable String searchContent){
        List<Book> searchedBooks = new ArrayList<>();
        List<Book> searchedBooksByAuthor = getBooksBySearchByAuthor(searchContent).getBody();
        List<Book> searchedBooksByGenre = getBooksBySearchContentByGenre(searchContent).getBody();
        List<Book> searchedBooksByTitle = getBooksBySearchContentByTitle(searchContent).getBody();

        if (searchedBooksByAuthor != null) {
            searchedBooks.addAll(searchedBooksByAuthor);
        }
        if (searchedBooksByGenre != null) {
            searchedBooks.addAll(searchedBooksByGenre);
        }
        if (searchedBooksByTitle != null) {
            searchedBooks.addAll(searchedBooksByTitle);
        }
        return ResponseEntity.ok(searchedBooks);
    }

    @GetMapping("/books/search/{searchContent}")
    public ResponseEntity<List<Book>> getBooksBySearchByAuthor(@PathVariable String searchContent){
        return null;
    }

    @GetMapping("/books/search/{searchContent}")
    public ResponseEntity<List<Book>> getBooksBySearchContentByGenre(@PathVariable String searchContent){
        return null;
    }

    @GetMapping("/books/search/{searchContent}")
    public ResponseEntity<List<Book>> getBooksBySearchContentByTitle(@PathVariable String searchContent){
        return null;
    }
}



/*
package com.progettoTAASS.book.controller;
import com.progettoTAASS.book.model.Book;
import com.progettoTAASS.book.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200") // per accettare le richieste del frontend
@RestController
@RequestMapping("/")
public class BookController {

    @Autowired
    BookRepository repository;

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        System.out.println("Get all Books...");

        List<Book> books = new ArrayList<>();
        repository.findAll().forEach(books::add);

        return books;
    }

    @GetMapping("/books/{title}")
    public List<Book> findByTitle(@PathVariable String title) {
        System.out.println("Get all book called" + title + "...");

        List<Book> books =  repository.findByTitle(title);

        return books;
    }

    @PostMapping("/books/insert")
    public Book insertBook(@RequestBody Book book) {
        System.out.println("Insert book " + book.getTitle() + "...");

        Book _book = repository.save(new Book(book.getTitle(), book.getNumPages()));
        return _book;
    }

}

 */