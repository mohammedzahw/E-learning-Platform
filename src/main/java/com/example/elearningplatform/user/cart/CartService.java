package com.example.elearningplatform.user.cart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.course.course.CourseService;
import com.example.elearningplatform.course.course.dto.SearchCourseDto;
import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;
    /************************************************************************************************************/

    public Response deleteFromCart(Integer courseId) {
        try {
            if (!cartRepository.findCourseInCart(courseId, tokenUtil.getUserId()).isPresent()) {
                return new Response(HttpStatus.BAD_REQUEST, "Course is not in cart", null);
            }

            cartRepository.removeFromCart(tokenUtil.getUserId(), courseId);
            return new Response(HttpStatus.OK, "course deleted from cart", null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /***************************************************************************** */

    public Response getCart() {
        try {

            List<SearchCourseDto> courses = cartRepository.findCartCourses(tokenUtil.getUserId()).stream()
                    .map(course -> new SearchCourseDto(
                            course, courseRepository.findCourseInstructors(course.getId()),
                            courseRepository.findCourseCategory(course.getId()),
                            courseRepository.findCourseTags(course.getId())))
                    .toList();

            return new Response(HttpStatus.OK, "Success", courses);

        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /***************************************************************************** */
    @Transactional
    public Response addToCart(Integer courseId) {
        try {
            if (cartRepository.findCourseInCart(courseId, tokenUtil.getUserId()).isPresent()) {
                return new Response(HttpStatus.BAD_REQUEST, "Course already in cart", null);
            }
            Course course = courseRepository.findById(courseId).orElseThrow(
                    () -> new CustomException("Course not found", HttpStatus.NOT_FOUND));
            if (course.getOwner().getId().equals(tokenUtil.getUserId())) {
                return new Response(HttpStatus.OK, "You cannot add your own course to cart", null);
            }
            if(courseService.ckeckCourseSubscribe(courseId))
                return new Response(HttpStatus.OK, "Course already enrolled", null);

            cartRepository.addToCart(tokenUtil.getUserId(), courseId);
            return new Response(HttpStatus.OK, "Course added to cart", null);

        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /***************************************************************************** */

}
