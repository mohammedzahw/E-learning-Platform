package com.example.elearningplatform.course.lesson.dto;

import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.lesson.Lesson;

import lombok.RequiredArgsConstructor;

/**
 * LessonDtoService
 */
@Service
@RequiredArgsConstructor
public class LessonDtoService {
   public LessonDto mapLessonToDto(Lesson lesson) {

        LessonDto lessonDto = new LessonDto();
        lessonDto.setId(lesson.getId());
        lessonDto.setTitle(lesson.getTitle());
        lessonDto.setDuration(lesson.getDuration());
        lessonDto.setVideoUrl(lesson.getVideoUrl());

        return lessonDto;
    }
}