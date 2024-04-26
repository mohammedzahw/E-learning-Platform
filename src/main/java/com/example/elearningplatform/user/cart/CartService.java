package com.example.elearningplatform.user.cart;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.Course;
import com.example.elearningplatform.course.CourseRepository;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    // private final HttpServletRequest request;
    private final TokenUtil tokenUtil;
    private final CourseRepository courseRepository;

    /***************************************************************************** */

    // public CartDto getCart(String token) {
    public CartDto getCart() {
    // System.out.println("token : " + tokenUtil.getUserId());

        CartDto cartDto = cartRepository.findByUserId(
            tokenUtil.getUserId())
            .map(cart -> new CartDto(cart)).orElse(null);

        return cartDto;
    }

    /***************************************************************************** */

    public Response addCourse(Integer courseId) {
        try {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            Cart cart = cartRepository.findByUserId(tokenUtil.getUserId())
                    .orElseThrow(() -> new RuntimeException("Cart not found"));
            List<Course> courses = cart.getCourses();
            if (courses.contains(course))
                return new Response(HttpStatus.BAD_REQUEST, "Course already in cart", null);
            cart.addCourse(course);
            return new Response(HttpStatus.OK, "Course added to cart", cartRepository.save(cart));

        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }
    /***************************************************************************** */

}
