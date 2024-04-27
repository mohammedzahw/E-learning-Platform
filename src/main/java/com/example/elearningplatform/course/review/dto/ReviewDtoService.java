package com.example.elearningplatform.course.review.dto;

import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.review.Review;
import com.example.elearningplatform.user.user.dto.UserDtoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewDtoService {
    private final UserDtoService userDtoService;

    public ReviewDto mapReviewToDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setContent(review.getContent());
        reviewDto.setCreationDate(review.getCreationDate());
        reviewDto.setId(review.getId());
        reviewDto.setModificationDate(review.getModificationDate());
        reviewDto.setRating(review.getRating());
        reviewDto.setUser(userDtoService.mapInstructorToDto(review.getUser()));
        return reviewDto;

    }

}
