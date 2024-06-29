package com.example.elearningplatform.course.section.dto;

import java.util.ArrayList; // Import the ArrayList class
import java.util.List; // Import the List class

import com.example.elearningplatform.course.lesson.dto.LessonDto;
import com.example.elearningplatform.course.section.Section;

import lombok.Data;

@Data
public class SectionDto {

    private Integer id;
    private String title;
    private String description;
    private Integer numberOfLessons;
    List<LessonDto> lessons =new ArrayList<>();
    private Integer duration;
    public void setNumberOfLessons(){
        this.numberOfLessons=lessons.size();
    }

    public SectionDto(Section section) {
        this.id = section.getId();
        this.title = section.getTitle();
        this.description = section.getDescription();
    }
    
}