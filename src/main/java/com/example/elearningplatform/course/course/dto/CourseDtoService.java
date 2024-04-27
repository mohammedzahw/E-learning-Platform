package com.example.elearningplatform.course.course.dto;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.review.Review;
import com.example.elearningplatform.course.review.ReviewRepository;
import com.example.elearningplatform.course.review.dto.ReviewDto;
import com.example.elearningplatform.course.review.dto.ReviewDtoService;
import com.example.elearningplatform.course.section.SectionRepository;
import com.example.elearningplatform.course.section.dto.SectionDto;
import com.example.elearningplatform.course.section.dto.SectionDtoService;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.UserRepository;
import com.example.elearningplatform.user.user.dto.UserDtoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseDtoService {

    private final SectionRepository sectionRepository;
    private final ReviewRepository reviewRepository;

    private final UserDtoService userDtoService;
    private final SectionDtoService sectionDtoService;
    private final ReviewDtoService reviewDtoService;
    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;

    /****************************************************************************************************** */

    public Boolean ckeckCourseSubscribe(Integer courseId) {
        // Authentication authentication =
        // SecurityContextHolder.getContext().getAuthentication();
        // if (authentication == null) {
        // return false;
        // }

        User user = userRepository.findById(tokenUtil.getUserId()).orElse(null);
        if (user == null) {
            return false;
        }
        for (Course c : user.getEnrolledCourses()) {
            if (c.getId() == courseId)
                return true;
        }
        return false;

    }

    /*************************************************************************** */

    public CourseDto mapCourseToDto(Course course) {
        CourseDto courseDto = new CourseDto();
        courseDto.setId(course.getId());
        courseDto.setCategories(course.getCategories());
        courseDto.setTitle(course.getTitle());
        courseDto.setPrice(course.getPrice());
        courseDto.setDuration(course.getDuration());
        courseDto.setLanguage(course.getLanguage());
        courseDto.setLevel(course.getLevel());

        courseDto.setNumberOfRatings(course.getNumberOfRatings());
        courseDto.setNumberOfEnrollments(course.getNumberOfEnrollments());
        courseDto.setImageUrl("https://via.placeholder.com/300x150");
        if (course.getTotalRatings() == 0)
            courseDto.setAverageRating(0.0);
        else if (course.getNumberOfRatings() != null && course.getTotalRatings() != null
                && course.getNumberOfRatings() != 0) {
            courseDto.setAverageRating(course.getTotalRatings() / course.getNumberOfRatings());
        }
        courseDto.setDescription(course.getDescription());
        courseDto.setCreationDate(course.getCreationDate());
        courseDto.setIsSubscribed(ckeckCourseSubscribe(course.getId()));
        courseDto.setLastUpdateDate(course.getLastUpdateDate());

        courseDto.setWhatYouWillLearn(course.getWhatYouWillLearn());
        courseDto.setPrerequisite(course.getPrerequisite());
        List<SectionDto> sectionDtos = sectionRepository.findByCourseId(course.getId()).stream()
                .map(section -> sectionDtoService.mapSectionToDto(section)).toList();

        courseDto.setSections(sectionDtos);

        reviewRepository.findByCourseId(course.getId()).forEach(review -> {
            ReviewDto reviewDto = reviewDtoService.mapReviewToDto(review);
            if (review.getUser().getId().equals(tokenUtil.getUserId())) {
                courseDto.setIsReviewd(true);
                courseDto.addReviewinFront(reviewDto);
            } else
                courseDto.addReview(reviewDto);

        });
        System.out.println(course.getInstructors());

        courseDto.setInstructors(
                course.getInstructors().stream().map(instructor -> userDtoService.mapInstructorToDto(instructor)).toList());

        
        return courseDto;
    }

    /************************************************************************************************************************** */

    public SearchCourseDto mapCourseToSearchDto(Course course) {
        SearchCourseDto searchCourseDto = new SearchCourseDto();
        searchCourseDto.setId(course.getId());
        searchCourseDto.setTitle(course.getTitle());
        searchCourseDto.setPrice(course.getPrice());

        searchCourseDto.setDuration(course.getDuration());

        searchCourseDto.setLanguage(course.getLanguage());
        searchCourseDto.setLevel(course.getLevel());
        searchCourseDto.setNumberOfRatings(course.getNumberOfRatings());
        searchCourseDto.setNumberOfEnrollments(course.getNumberOfEnrollments());
        searchCourseDto.setImageUrl("https://via.placeholder.com/300x150");
        if (course.getTotalRatings() == 0)
            searchCourseDto.setAverageRating(0.0);
        else if (course.getNumberOfRatings() != null && course.getTotalRatings() != null
                && course.getNumberOfRatings() != 0) {
            searchCourseDto.setAverageRating(course.getTotalRatings() / course.getNumberOfRatings());
        }

        searchCourseDto.setInstructors(
                course.getInstructors().stream().map(instructor -> userDtoService.mapInstructorToDto(instructor)).toList());

        return searchCourseDto;

    }

}
