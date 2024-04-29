package com.example.elearningplatform.response;

import org.springframework.http.HttpStatus;

public class ReviewResponse extends Response {
    Boolean isReviewd = false;

    public ReviewResponse(HttpStatus status, String message, Object data, Boolean isReviewd) {
        super(status, message, data);
        this.isReviewd = isReviewd;
    }

}
