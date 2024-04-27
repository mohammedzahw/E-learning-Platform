package com.example.elearningplatform.user.lists;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.course.course.dto.CourseDtoService;
import com.example.elearningplatform.course.course.dto.SearchCourseDto;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.user.lists.dto.UpdateUserList;
import com.example.elearningplatform.user.lists.dto.UserListDtoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserListService {
    private final UserListRepository userListRepository;
    private final CourseRepository courseRepository;
    private final UserListDtoService userListDtoService;
    private final CourseDtoService courseDtoService;

    /************************************************************************************************** */

    // public UserListDto mapUserListToDto(UserList UserList) {

    // UserListDto UserListDto = new UserListDto();
    // UserListDto.setId(UserList.getId());
    // UserListDto.setName(UserList.getName());
    // UserListDto.setCourses(UserList.getCourses().stream().map(course -> {
    // return courseDtoService.mapCourseToSearchDto(course);
    // }).toList());
    // return UserListDto;

    // }

    /************************************************************************************************** */

    public List<SearchCourseDto> getlist(Integer listId) {
        try { // User user = userRepository.findById(tokenUtil.getUserId()).orElse(null);
            UserList list = userListRepository.findById(listId)
                    .orElseThrow(() -> new RuntimeException("List not found"));
            List<SearchCourseDto> courses = list.getCourses().stream()
                    .map(course -> courseDtoService.mapCourseToSearchDto(course))
                    .toList();
            return courses;
        } catch (Exception e) {
            return null;
        }
    }

    /************************************************************************************************** */

    public Response addTolist(Integer listId, Integer courseId) {
        try {
            UserList list = userListRepository.findById(listId)
                    .orElseThrow(() -> new RuntimeException("List not found"));

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            list.addCourse(course);
            userListRepository.save(list);
            return new Response(HttpStatus.OK, "Success", null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }

    }

    /************************************************************************************************** */
    public Response deleteList(Integer listId) {
        try {
            UserList list = userListRepository.findById(listId)
                    .orElseThrow(() -> new RuntimeException("List not found"));

            userListRepository.delete(list);
            return new Response(HttpStatus.OK, "Success", null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /************************************************************************************************** */

    public Response updateList(UpdateUserList newlist) {

        try {
            UserList list = userListRepository.findById(newlist.getListId())
                    .orElseThrow(() -> new RuntimeException("List not found"));

            if (newlist.getName() != null)
                list.setName(newlist.getName());
            if (newlist.getDescription() != null)
                list.setDescription(newlist.getDescription());
            userListRepository.save(list);

            return new Response(HttpStatus.OK, "Success", null);
        } catch (Exception e) {

            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /************************************************************************************************** */

    public Response deleteCourseFromList(Integer listId, Integer courseId) {
        try {
            UserList list = userListRepository.findById(listId)
                    .orElseThrow(() -> new RuntimeException("List not found"));

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            list.removeCourse(course);
            userListRepository.save(list);
            return new Response(HttpStatus.OK, "Success", userListDtoService.mapUserListToDto(list));
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }
    /************************************************************************************************** */

}
