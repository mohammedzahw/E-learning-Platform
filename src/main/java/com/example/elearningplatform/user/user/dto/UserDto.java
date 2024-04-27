package com.example.elearningplatform.user.user.dto;

import java.util.ArrayList;
import java.util.List;
import com.example.elearningplatform.course.course.dto.CourseDto;
import lombok.Data;

@Data
public class UserDto {
    
    private Integer id;
    private String firstName;
    private String lastName;
    private String about;
    private String imageUrl;
    List<CourseDto> instructoredCourses = new ArrayList<>();

    // private String email;


}
