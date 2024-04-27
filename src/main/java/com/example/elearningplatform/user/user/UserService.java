package com.example.elearningplatform.user.user;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.course.course.dto.CourseDto;
import com.example.elearningplatform.course.course.dto.CourseDtoService;
import com.example.elearningplatform.course.course.dto.SearchCourseDto;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.signup.SignUpRequest;
import com.example.elearningplatform.user.address.Address;
import com.example.elearningplatform.user.address.AddressDto;
import com.example.elearningplatform.user.address.AddressRepository;
import com.example.elearningplatform.user.user.dto.ProfileDto;
import com.example.elearningplatform.user.user.dto.UserDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final TokenUtil tokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final CourseRepository courseRepository;
    private final CourseDtoService courseDtoService;

    /************************************************************************************************************/
    public Response getUser(Integer userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            userDto.setAbout(user.getAbout());
            userDto.setImageUrl("https://via.placeholder.com/300x150");
            userDto.setInstructoredCourses(
                    user.getEnrolledCourses().stream().map(course -> courseDtoService.mapCourseToDto(course)).toList());
            return new Response(HttpStatus.OK, "Success", userDto);
        } catch (Exception e) {
            return new Response(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }
    }
    // public List<UserDto> findBySearchKey(String searchKey, Integer pageNumber) {
    // Pageable pageable = PageRequest.of(pageNumber, 8);
    // return userRepository.findBySearchKey(searchKey, pageable).stream().map(user
    // -> {
    // UserDto instructor = new UserDto(user);
    // return instructor;
    // }).toList();

    // }

    /************************************************************************************************************/

    public User saveUser(SignUpRequest request) throws SQLException, IOException {

        User user = User.builder().email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
                .age(request.getAge())
                .about(request.getBio()).firstName(request.getFirstName()).lastName(request.getLastName())
                .enabled(false)
                .phoneNumber(request.getPhoneNumber()).registrationDate(LocalDateTime.now()).build();
        if (request.getProfilePicture() != null)
            user.setProfilePicture(request.getProfilePicture().getBytes());
        userRepository.save(user);
        user.setRoles(List.of(Role.ROLE_USER));
        userRepository.save(user);
        Address address = Address.builder().user(user).city(request.getCity()).country(request.getCountry())
                .street(request.getStreet()).state(request.getState()).zipCode(request.getZipCode()).build();
        addressRepository.save(address);
        return user;
    }

    /***************************************************************************************************************/
    public Response addToWishlist(Integer courseId) {

        try {
            Course course = courseRepository.findById(courseId).orElseThrow(() -> new Exception("Course not found"));
            User user = userRepository.findById(tokenUtil.getUserId())
                    .orElseThrow(() -> new Exception("User not found"));

            user.addTowhishlist(course);
            userRepository.save(user);
            return new Response(HttpStatus.OK, "Course added to wishlist", null);
        } catch (Exception e) {
            return new Response(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }

    }

    /********************************************************************************************************* */
    public Response deleteFromoWishlist(Integer courseId) {

        try {
            Course course = courseRepository.findById(courseId).orElseThrow(() -> new Exception("Course not found"));
            User user = userRepository.findById(tokenUtil.getUserId())
                    .orElseThrow(() -> new Exception("User not found"));

            user.deleteFromoWishlist(course);
            userRepository.save(user);
            return new Response(HttpStatus.OK, "Course deleted from wishlist", null);
        } catch (Exception e) {
            return new Response(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }

    }

    /********************************************************************************************************* */

    public Response addToArchived(Integer courseId) {
        try {
            Course course = courseRepository.findById(courseId).orElseThrow(() -> new Exception("Course not found"));
            User user = userRepository.findById(tokenUtil.getUserId())
                    .orElseThrow(() -> new Exception("User not found"));
            user.addToArchived(course);
            userRepository.save(user);
            return new Response(HttpStatus.OK, "Course added to archived", null);
        } catch (Exception e) {
            return new Response(HttpStatus.NOT_FOUND, e.getMessage(), null);

        }

    }

    /********************************************************************************************************* */
    public Response deleteFromArchived(Integer courseId) {

        try {
            Course course = courseRepository.findById(courseId).orElseThrow(() -> new Exception("Course not found"));
            User user = userRepository.findById(tokenUtil.getUserId())
                    .orElseThrow(() -> new Exception("User not found"));

            user.deleteFromArchived(course);
            userRepository.save(user);
            return new Response(HttpStatus.OK, "Course deleted from archived", null);
        } catch (Exception e) {
            return new Response(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }

    }

    /********************************************************************************************************* */
    public Response getProfile() {
        try {
            User user = userRepository.findById(tokenUtil.getUserId())
                    .orElseThrow(() -> new Exception("User not found"));
            ProfileDto profileDto = new ProfileDto();
            profileDto.setId(user.getId());
            profileDto.setEmail(user.getEmail());
            profileDto.setFirstName(user.getFirstName());
            profileDto.setImageUrl("https://via.placeholder.com/300x150");
            profileDto.setLastName(user.getLastName());
            profileDto.setPhoneNumber(user.getPhoneNumber());
            profileDto.setEnabled(user.isEnabled());
            profileDto.setRegistrationDate(user.getRegistrationDate());
            profileDto.setBio(user.getAbout());
            profileDto.setLastLogin(user.getLastLogin());
            profileDto.setAge(user.getAge());
            profileDto.setAddress(new AddressDto(user.getAddress()));
            return new Response(HttpStatus.OK, "Success", profileDto);
        } catch (Exception e) {
            return new Response(HttpStatus.NOT_FOUND, e.getMessage(), null);

        }
    }

    /********************************************************************************************************* */
    public UserDto mapUserDto(User user) {
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

    /********************************************************************************************************* */
    public Response getEnrolledCourses() {
        try {
            User user = userRepository.findById(tokenUtil.getUserId())
                    .orElseThrow(() -> new Exception("User not found"));
            List<CourseDto> courses = user.getEnrolledCourses().stream()
                    .map(course -> courseDtoService.mapCourseToDto(course))
                    .toList();
            return new Response(HttpStatus.OK, "Success", courses);
        } catch (Exception e) {
            return new Response(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    public Response getArchived() {
        try {
            User user = userRepository.findById(tokenUtil.getUserId())
                    .orElseThrow(() -> new Exception("User not found"));
            List<SearchCourseDto> courses = user.getArchivedCourses().stream()
                    .map(course -> courseDtoService.mapCourseToSearchDto(course))
                    .toList();
            return new Response(HttpStatus.OK, "Success", courses);
        } catch (Exception e) {
            return new Response(HttpStatus.NOT_FOUND, e.getMessage(), null);

        }
    }

    public Response getWishlist() {
        try {
            User user = userRepository.findById(tokenUtil.getUserId())
                    .orElseThrow(() -> new Exception("User not found"));
            List<SearchCourseDto> courses = user.getWhishlist().stream()
                    .map(course -> courseDtoService.mapCourseToSearchDto(course))
                    .toList();
            return new Response(HttpStatus.OK, "Success", courses);
        } catch (Exception e) {
            return new Response(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }
    }
}
