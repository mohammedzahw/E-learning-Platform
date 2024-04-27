package com.example.elearningplatform.user.lists.dto;

import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.course.dto.SearchCourseDto;

import lombok.Data;

@Data
public class UserListDto {

    private Integer id;
    private String name;
    private List<SearchCourseDto> courses = new ArrayList<>();

}
