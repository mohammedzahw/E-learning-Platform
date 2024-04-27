package com.example.elearningplatform.course.course;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.course.dto.CourseDto;
import com.example.elearningplatform.course.course.dto.CourseDtoService;
import com.example.elearningplatform.course.course.dto.SearchCourseDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {
        @Autowired 
        private CourseRepository courseRepository;

        @Autowired 
        private CourseDtoService courseDtoService;

        /***************************************************************************************** */
        public List<CourseDto> getAllCourses() {

                List<CourseDto> courseDtos = courseRepository.findAll().stream()
                                .map(course -> {
                                        CourseDto courseDto = courseDtoService.mapCourseToDto(course);
                                        return courseDto;
                                }).toList();
                // courses.forEach(course -> {
                // courseDtos.add(courseDtoService.mapCourseToDto(course));
                // });
                return courseDtos;
        }

        /****************************************************************************************/
        public List<SearchCourseDto> findByCategory(Integer categoryId, Integer pageNumber) {

                Pageable pageable = PageRequest.of(pageNumber, 8);

                List<SearchCourseDto> courses = courseRepository.findByCategoryId(categoryId, pageable).stream()
                                .map(course -> courseDtoService.mapCourseToSearchDto(course)).toList();
                return courses;
        }

        /****************************************************************************************/
        public List<SearchCourseDto> findBysearchkey(String searchKey, Integer pageNumber) {
                if (searchKey == null) {
                        return new ArrayList<>();
                }

                Pageable pageable = PageRequest.of(pageNumber, 8);

                List<SearchCourseDto> courses = courseRepository.findBySearchKey(searchKey, pageable).stream()
                                .map(course -> courseDtoService.mapCourseToSearchDto(course)).toList();

                return courses;

        }

        /****************************************************************************************/
        public List<SearchCourseDto> findByTitle(String title, Integer pageNumber) {

                Pageable pageable = PageRequest.of(pageNumber, 8);

                List<SearchCourseDto> courses = courseRepository.findByTitle(title, pageable).stream()
                                .map(course -> courseDtoService.mapCourseToSearchDto(course)).toList();
                return courses;
        }

        /**************************************************************************************** */
        public List<SearchCourseDto> findByInstructorName(Integer instructorId, Integer pageNumber) {

                Pageable pageable = PageRequest.of(pageNumber, 8);

                List<SearchCourseDto> courses = courseRepository.findByInstructorId(instructorId, pageable).stream()
                                .map(course -> courseDtoService.mapCourseToSearchDto(course)).toList();
                return courses;
        }

        /**************************************************************************************** */
        public CourseDto getCourse(Integer courseId) {
                Course course = courseRepository.findById(courseId).orElse(null);

                if (course == null)
                        return null;
                CourseDto courseDto = courseDtoService.mapCourseToDto(course);

                return courseDto;

        }

        /**************************************************************************************** */

        /**************************************************************************************** */
        // public CourseDto mapCourseToDto(Course course) {
        // CourseDto courseDto = new CourseDto(course,
        // ckeckCourseSubscribe(course.getId()));
        // courseDto.setDescription(course.getDescription());
        // courseDto.setCreationDate(course.getCreationDate());
        // courseDto.setLastUpdateDate(course.getLastUpdateDate());
        // courseDto.setWhatYouWillLearn(course.getWhatYouWillLearn());
        // courseDto.setPrerequisite(course.getPrerequisite());
        // List<SectionDto> sectionDtos =
        // sectionRepository.findByCourseId(course.getId()).stream()
        // .map(section -> sectionService.mapSectionToDto(section)).toList();

        // courseDto.setSections(sectionDtos);

        // List<ReviewDto> courseReviewDtos =
        // reviewRepository.findByCourseId(course.getId()).stream().map(
        // review -> {
        // ReviewDto reviewDto = new ReviewDto(review);
        // if (review.getUser().getId().equals(tokenUtil.getUserId())) {
        // courseDto.setIsReviewd(true);
        // courseDto.addReviewinFront(reviewDto);
        // }
        // return reviewDto;
        // })
        // .toList();
        // courseDto.setReviews(courseReviewDtos);

        // course.getInstructors().forEach(instructor -> {
        // UserDto user = userService.mapUerDto(instructor);
        // courseDto.addInstructor(user);
        // });

        // course.getCategories().forEach(category -> {
        // courseDto.getCategories().add(category);
        // });
        // return courseDto;

        // }
        /****************************************************************************************/

}
