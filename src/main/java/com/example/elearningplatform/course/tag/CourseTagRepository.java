package com.example.elearningplatform.course.tag;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CourseTagRepository extends JpaRepository<CourseTag, Integer> {
 void deleteByCourseId(Integer courseId);

Optional<CourseTag> findByTagAndCourseId(String tag, Integer id);
}
