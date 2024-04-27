package com.example.elearningplatform.course.course.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.category.Category;
import com.example.elearningplatform.course.review.dto.ReviewDto;
import com.example.elearningplatform.course.section.dto.SectionDto;
import com.example.elearningplatform.user.user.dto.InstructorDto;
import com.example.elearningplatform.user.user.dto.UserDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CourseDto extends SearchCourseDto {

    private String description;
    private LocalDate creationDate;
    private LocalDate lastUpdateDate;
    private String whatYouWillLearn;
    private String prerequisite;
    private Boolean isReviewd = false;
    private Boolean isSubscribed = false;

    private List<SectionDto> sections = new ArrayList<>();
    private List<ReviewDto> reviews = new ArrayList<>();
 

    public void addSection(SectionDto section) {
        if (section == null)
            return;
        this.sections.add(section);
    }

    public void addReview(ReviewDto review) {
        if (review == null)
            return;
        this.reviews.add(review);
    }

    

    public void addReviewinFront(ReviewDto review) {
        if (review == null)
            return;
        this.reviews.addFirst(review);
    }

    // public CourseDto(Course course, Boolean isSubscribed) {
    // super(course);

    // this.isSubscribed = isSubscribed;

    // }

}
