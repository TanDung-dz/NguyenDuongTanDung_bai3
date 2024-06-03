package Bai3.NguyenDuongTanDung_bai3.controllers;

import Bai3.NguyenDuongTanDung_bai3.entities.Book;
import Bai3.NguyenDuongTanDung_bai3.services.BookService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public String showAllBooks(@NotNull Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "book/list";
    }

    @GetMapping("/add")
    public String addBookForm(@NotNull Model model) {
        model.addAttribute("book", new Book());
        return "book/add";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute("book") Book book, Model model) {
        if (!isValidPrice(book.getPrice())) {
            model.addAttribute("errorMessage", "Giá không hợp lệ");
            return "book/add";
        }

        if (book.getId() == null ||
                book.getTitle() == null || book.getTitle().isEmpty() ||
                book.getAuthor() == null || book.getAuthor().isEmpty() ||
                book.getPrice() == null ||
                book.getCategory() == null || book.getCategory().isEmpty()) {
            model.addAttribute("errorMessage", "Vui lòng nhập đầy đủ thông tin!");
            return "book/add";
        }

        if (bookService.getBookById(book.getId()).isEmpty()) {
            bookService.addBook(book);
        }
        return "redirect:/books";
    }


    @GetMapping("/edit/{id}")
    public String editBookForm(@NotNull Model model, @PathVariable long id) {
        var book = bookService.getBookById(id).orElse(null);
        model.addAttribute("book", book != null ? book : new Book());
        return "book/edit";
    }

    @PostMapping("/edit")
    public String editBook(@ModelAttribute("book") Book book, Model model) {
        if (!isValidPrice(book.getPrice())) {
            model.addAttribute("errorMessage", "Giá không hợp lệ");
            return "book/edit";
        }

        if (book.getTitle() == null || book.getTitle().isEmpty() ||
                book.getAuthor() == null || book.getAuthor().isEmpty() ||
                book.getPrice() == null ||
                book.getCategory() == null || book.getCategory().isEmpty()) {
            model.addAttribute("errorMessage", "Vui lòng nhập đầy đủ thông tin!");
            return "book/edit";
        }

        bookService.updateBook(book);
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable long id) {
        if (bookService.getBookById(id).isPresent())
            bookService.deleteBookById(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String searchBooks(@RequestParam("title") String title, Model model) {
        List<Book> foundBooks = bookService.searchBooksByTitle(title);
        model.addAttribute("books", foundBooks);
        return "book/list";
    }
    private boolean isValidPrice(Double price) {
        return price != null && price > 0;
    }
}