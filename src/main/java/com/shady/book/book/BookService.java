package com.shady.book.book;

import com.shady.book.common.PageResponse;
import com.shady.book.exception.OperationNotPermittedException;
import com.shady.book.history.BookTransactionHistory;
import com.shady.book.history.BookTransactionHistoryRepository;
import com.shady.book.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.shady.book.book.BookSpecification.withOwnerId;

@Service
@RequiredArgsConstructor
public class BookService {

    /**
     * let's create bookMapper class to convert the request to book object and build it in the service class.
     **/
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;

    public Integer save(BookRequest request, Authentication connectedUser) {
        // first lets extract the user.
        User user = ((User) connectedUser.getPrincipal());
        // second thing lets get the book object from the request.
        Book book = bookMapper.toBook(request);
        // third thing let's set the real owner to this book.
        // which who is the connected user

        book.setOwner(user);

        // now we can save the book.
        return bookRepository.save(book).getId();
    }


    public BookResponse findById(Integer bookId) {
        return bookRepository.findById(bookId).map(bookMapper::toBookResponse).orElseThrow(() ->
                new EntityNotFoundException("Book with id " + bookId + " not found"));
    }


    public PageResponse<BookResponse> findAllBooks(int page,
                                                   int size,
                                                   Authentication connectedUser) {

        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        Page<Book> books = bookRepository.findAllDisplayBooks(pageable, user.getId());

        // store the response on a list
        List<BookResponse> bookResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );


    }

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("CreatedDate").descending());
        Page<Book> books = bookRepository.findAll(withOwnerId(user.getId()), pageable);
        List<BookResponse> bookResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );

    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("CreatedDate").descending());
        // get all borrowed books
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.findAllBorrowedBooks(
                pageable, user.getId()
        );
        // convert the response to a list of borrowed book response

        List<BorrowedBookResponse> bookResponses = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );


    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("CreatedDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.findAllReturnedBooks(
                pageable, user.getId()
        );
        List<BorrowedBookResponse> bookResponses = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );

    }

    public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
        // to update the shareable status for a specific book we need to fetch to book first thing.

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with this id is not found"));
        User user = ((User) connectedUser.getPrincipal());
        // we need to check if the owner of the book is the one who is gonna make the changes

        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You are not allowed to make this change");
        }
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return bookId;

    }

    public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException(("Book with this id is not found")));
        User user = ((User) connectedUser.getPrincipal());

        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You are not allowed to make this change");
        }
        book.setArchived((!book.isArchived()));
        bookRepository.save(book);
        return bookId;
    }

    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with this id is not found"));
        User user = ((User) connectedUser.getPrincipal());
            /*
            check if the book shareable or not and the owner not the same as the connected user
            and the book is borrowed or not borrowed
            and the book is archived or not archived
            Yeah.. that's it!
            */
        if (!book.isShareable() || book.isArchived() || Objects.equals(book.getOwner().getBooks(), user.getId())) {
            throw new OperationNotPermittedException("You are not allowed to borrow this book since it was archived or not shareable or you are trying to borrow your book");
        }
        // book borrowed or not

        boolean isBorrowed = bookTransactionHistoryRepository.isAlreadyBorrowedByUser(bookId, user.getId());
        if (isBorrowed) {
            throw new OperationNotPermittedException("You are not allowed to borrow this book is already borrowed by someone");
        }
        // now we can create a new transaction history to store the borrowed book

        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returnApproved(false)
                .returned(false)
                .build();
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Integer returnBorrowedBook(Integer bookId, Authentication connectedUser) {
        // 1. get the book
        // 2. get the user
        // 3. check if the book is borrowed or not by this user
        // 4. if the book is borrowed by this user then we can return it
        // 5. if the book is not borrowed by this user then we can't return it
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with this id is not found"));
        User user = ((User) connectedUser.getPrincipal());
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("You are not allowed to return this book");
        }
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You can't borrow or return your own book");
        }
        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository.findByBookIdAndUserId
                        (bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("You didn't borrow this book"));
        bookTransactionHistory.setReturned(true);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }
}
