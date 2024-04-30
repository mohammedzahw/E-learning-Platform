package com.example.elearningplatform.user.user;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.user.user.dto.InstructorDto;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor

@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


    /***************************************************************************************************/

    @GetMapping("/get-user/{id}")
    public Response getUser(@PathVariable("id") Integer id) throws SQLException {
        return userService.getUser(id);

    }

    /***********************************************************************************************/
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/get-all-users")

    public Response getUsers() {

        return new Response(HttpStatus.OK, "success",
                userRepository.findAll().stream().map(user -> new InstructorDto(user)).toList());

    }

    /**********************************************************************************************/
@SecurityRequirement(name = "bearerAuth")
    @GetMapping("/profile")
    public Response getProfile() {

        return new Response(HttpStatus.OK, "success", userService.getProfile());
    }

    /************************************************************************************************/
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/my-learning")
    public Response myLearning() {
        return new Response(HttpStatus.OK, "success", userService.getEnrolledCourses());

    }

    /***************************************************************************************************/





}