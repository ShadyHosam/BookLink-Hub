package com.shady.book.book;

import com.shady.book.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("books")
@Tag(name="Book")
public class BookController {
    private final BookService bookService;


    @PostMapping("/save")
    public ResponseEntity<Integer>saveBook(
            @Valid @RequestBody BookRequest request,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.save(request,connectedUser));

    }

    @GetMapping("{book-id}")
    public ResponseEntity<BookResponse>findBookById(
        @PathVariable("book-id") Integer bookId
    ){
        return ResponseEntity.ok(bookService.findById(bookId));
    }

    // we need to return all books

    @GetMapping
    // since the response will contains a lot of data so we need to create our own page response
    public ResponseEntity<PageResponse<BookResponse>>findAllBooks(
            @RequestParam(name="page" , defaultValue = "0" , required = false) int page ,
            @RequestParam(name="size" , defaultValue = "10" , required = false) int size ,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.findAllBooks(page ,size ,connectedUser));
    }
    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>>findAllBooksByOwner(@RequestParam(name="page" , defaultValue = "0" , required = false) int page ,
                                                                         @RequestParam(name="size" , defaultValue = "10" , required = false) int size ,
                                                                         Authentication connectedUser){

    return ResponseEntity.ok(bookService.findAllBooksByOwner(page , size , connectedUser));
    }

}
