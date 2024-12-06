package com.shady.book.feedback;

import com.shady.book.book.Book;
import com.shady.book.book.BookRepository;
import com.shady.book.common.PageResponse;
import com.shady.book.exception.OperationNotPermittedException;
import com.shady.book.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedBackService {
    private final BookRepository bookRepository;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackRepository feedbackRepository;

    public Integer save(FeedBackRequest request, Authentication connectedUser) {
        Book book = bookRepository.findById(request.bookId()).orElseThrow(() ->
                new EntityNotFoundException(("Book not found" + request.bookId())));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("this book isn't shareable or archived");
        }
        User user = ((User) connectedUser.getPrincipal());
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("you can't give feedback to your own book");
        }
        // now we can do the feedback
        // now we are building the feedback to save it in the database..


        Feedback feedback = feedbackMapper.toFeedback(request);
        return feedbackRepository.save(feedback).getId();
    }

    public PageResponse<FeedbackResponse> findAllFeedbackByBook(Integer bookId, int page, int size, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId).orElseThrow(() ->
                new EntityNotFoundException("This book cannot be found"));

        User user = ((User) connectedUser.getPrincipal());

        Pageable pageable = PageRequest.of(page, size);
        Page<Feedback> feedbacks = feedbackRepository.findAllByBookId(bookId, pageable);
        //sure the result is gonna be a list of feedbacks!~

        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(fb -> feedbackMapper.toFeedbackResponse(fb, user.getId()))
                .toList();


        val totalPages = feedbacks.getTotalPages();
        return new PageResponse<>(feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast());
    }
}
