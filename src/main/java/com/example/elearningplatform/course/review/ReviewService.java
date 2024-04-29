 package com.example.elearningplatform.course.review;

 import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.course.review.dto.ReviewDto;
import com.example.elearningplatform.response.ReviewResponse;
import com.example.elearningplatform.security.TokenUtil;

import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Data
@Service
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;
    private final TokenUtil tokenUtil;

    /************************************************************************************************* */
    public ReviewResponse getReviewsByCourseId(Integer courseId) {
        try {

            List<ReviewDto> reviews = new ArrayList<>();
            ReviewResponse reviewResponse = new ReviewResponse();


            courseRepository.findCourseReviews(courseId).forEach(review -> {
                ReviewDto reviewDto = new ReviewDto(review);
                System.out.println(review.getUser().getId());
                System.out.println(review.getUser().getId());
                System.out.println(review.getUser().getId());
                System.out.println(review.getUser().getId());
                if (reviewDto.getUser().getId() == tokenUtil.getUserId()) {
                    reviewResponse.setIsReviewd(true);
                    reviews.addFirst(reviewDto);
                }
                reviews.add(reviewDto);
            });
            return new ReviewResponse(HttpStatus.OK, "Success", reviews);
        } catch (Exception e) {
            return new ReviewResponse(HttpStatus.NOT_FOUND, "Reviews not found", null);
        }
    }

    /************************************************************************************************* */

    
}