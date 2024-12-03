package com.shady.book.feedback;

import com.shady.book.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("feedbacks")
@RequiredArgsConstructor
@Tag(name = "Feedback")
public class FeedBackController {
    private final FeedBackService feedBackService;


    @PostMapping("/saveFeedBack")
    public ResponseEntity<Integer> saveFeedBack(
            @Valid @RequestBody FeedBackRequest feedBackRequest,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(feedBackService.save(feedBackRequest, connectedUser));
    }
    // add new endpoint to get all the feedbacks for a specific book ~~~

    @GetMapping("/book/{book-id}")
    public ResponseEntity<PageResponse<FeedbackResponse>> findAllFeedbackByBook(
            @PathVariable("book-id") Integer bookId,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "page", defaultValue = "0", required = false) int size,
            Authentication connectedUser
    ) {

            return ResponseEntity.ok(feedBackService.findAllFeedbackByBook(
                    bookId, page, size, connectedUser
            )
            );

    }

}
