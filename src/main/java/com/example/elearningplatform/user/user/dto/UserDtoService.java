package com.example.elearningplatform.user.user.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.course.dto.CourseDtoService;
import com.example.elearningplatform.user.user.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Service
@Getter
@Setter
@ToString
public class UserDtoService {
    @Autowired
    @Lazy
    private  CourseDtoService courseDtoService;

    // private void setCourseDtoService(CourseDtoService courseDtoService) {
    // this.courseDtoService = courseDtoService;
    // }

    public UserDto mapUserToDto(User user) {
        if (user == null)
            return null;
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setAbout(user.getAbout());
        userDto.setImageUrl("https://via.placeholder.com/300x150");
        userDto.setInstructoredCourses(
                user.getEnrolledCourses().stream().map(course -> courseDtoService.mapCourseToDto(course)).toList());
        return userDto;
    }

    public ProfileDto mapProfileToDto(User profile) {
        if (profile == null)
            return null;
        ProfileDto profileDto = new ProfileDto();
        profileDto.setId(profile.getId());
        profileDto.setEmail(profile.getEmail());
        profileDto.setFirstName(profile.getFirstName());
        profileDto.setLastName(profile.getLastName());
        profileDto.setImageUrl("https://via.placeholder.com/300x150");
        profileDto.setPhoneNumber(profile.getPhoneNumber());
        profileDto.setEnabled(profile.getEnabled());
        profileDto.setRegistrationDate(profile.getRegistrationDate());
        profileDto.setBio(profile.getAbout());
        profileDto.setLastLogin(profile.getLastLogin());
        profileDto.setAge(profile.getAge());

        return profileDto;
    }

    public InstructorDto mapInstructorToDto(User instructor) {
        if (instructor == null)
            return null;
        InstructorDto instructorDto = new InstructorDto();
        instructorDto.setId(instructor.getId());
        instructorDto.setFirstName(instructor.getFirstName());
        instructorDto.setLastName(instructor.getLastName());
        instructorDto.setImageUrl("https://via.placeholder.com/300x150");
        return instructorDto;
    }

}
