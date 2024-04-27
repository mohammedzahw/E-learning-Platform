package com.example.elearningplatform.course.section.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.lesson.Lesson;
import com.example.elearningplatform.course.lesson.LessonRepository;
import com.example.elearningplatform.course.lesson.dto.LessonDto;
import com.example.elearningplatform.course.lesson.dto.LessonDtoService;
import com.example.elearningplatform.course.section.Section;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SectionDtoService {
    private final LessonDtoService lessonDtoService;
    private final LessonRepository lessonRepository;

    public SectionDto mapSectionToDto(Section section) {
        if (section == null)
            return null;
        SectionDto sectionDto = new SectionDto();
        sectionDto.setId(section.getId());
        sectionDto.setTitle(section.getTitle());
        sectionDto.setDescription(section.getDescription());
        sectionDto.setDuration(section.getDuration());

        List<Lesson> sectionLessons = lessonRepository.findBySectionId(section.getId());
        sectionDto.setNumberOfLessons(sectionLessons.size());
        List<LessonDto> sectionLessonDtos = new ArrayList<>();
        for (Lesson lesson : sectionLessons) {
            sectionLessonDtos.add(lessonDtoService.mapLessonToDto(lesson));
            sectionDto.setLessons(sectionLessonDtos);
        }

        return sectionDto;

    }

}
