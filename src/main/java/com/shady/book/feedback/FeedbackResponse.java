package com.shady.book.feedback;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter


public class FeedbackResponse {
   private Double note;
    private String comment;
    private boolean ownFeedback;

}

