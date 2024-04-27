package com.example.elearningplatform.course.section;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.lesson.LessonRepository;
import com.example.elearningplatform.course.lesson.LessonService;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Service
@Data
@RequiredArgsConstructor
public class SectionService {
    private final LessonService lessonService;

    private final SectionRepository sectionRepository;
    private final LessonRepository lessonRepository;

    /************************************************************************************** */


    public List<Section> getSectionsByCourseId(Integer courseID) {
        return sectionRepository.findByCourseId(courseID);
    }


}
