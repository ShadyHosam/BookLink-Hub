package com.shady.book.book;

import com.shady.book.common.PageResponse;
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

import static com.shady.book.book.BookSpecification.withOwnerId;

@Service
@RequiredArgsConstructor
public class BookService {

         /**
         let's create bookMapper class to convert the request to book object and build it in the service class.
          **/
        private final BookMapper bookMapper;
        private final BookRepository bookRepository;

        public Integer save(BookRequest request, Authentication connectedUser){
            // first lets extract the user.
            User user = ((User)connectedUser.getPrincipal());
            // second thing lets get the book object from the request.
            Book book = bookMapper.toBook(request);
            // third thing let's set the real owner to this book.
            // which who is the connected user

            book.setOwner(user);

            // now we can save the book.
            return bookRepository.save(book).getId();
        }


        public BookResponse findById(Integer bookId) {
            return bookRepository.findById(bookId).map(bookMapper::toBookResponse).orElseThrow(()->
                    new EntityNotFoundException("Book with id "+bookId+" not found"));
    }


        public PageResponse<BookResponse> findAllBooks(int page ,
                                                       int size ,
                                                       Authentication connectedUser) {

            User user = ((User)connectedUser.getPrincipal());
            Pageable pageable = PageRequest.of(page , size , Sort.by("createdDate").descending());

            Page<Book> books = bookRepository.findAllDisplayBooks(pageable,user.getId());

            // store the response on a list
            List<BookResponse>bookResponse = books.stream()
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
        User user = ((User)connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page , size , Sort.by("CreatedDate").descending());
        Page<Book> books = bookRepository.findAll(withOwnerId(user.getId()),pageable);
        List<BookResponse>bookResponse = books.stream()
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
}
