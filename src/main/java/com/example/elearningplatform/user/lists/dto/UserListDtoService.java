package com.example.elearningplatform.user.lists.dto;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.course.dto.CourseDtoService;
import com.example.elearningplatform.user.lists.UserList;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserListDtoService {
    private final CourseDtoService courseDtoService;

    public UserListDto mapUserListToDto(UserList userList) {

        UserListDto UserListDto = new UserListDto();
        UserListDto.setId(userList.getId());
        UserListDto.setName(userList.getName());
        UserListDto.setCourses(new ArrayList<>());
        UserListDto.getCourses().addAll(userList.getCourses().stream().map(
                course -> courseDtoService.mapCourseToSearchDto(course)).toList());
        return UserListDto;
    }

}
