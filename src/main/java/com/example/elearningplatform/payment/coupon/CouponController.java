package com.example.elearningplatform.payment.coupon;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.payment.coupon.dto.ApplyCouponRequest;
import com.example.elearningplatform.payment.coupon.dto.CreateRequest;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.UserRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CouponController {

    private final CouponService couponService;
    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;
    private final CourseRepository courseRepository;

    /****************************** *************************************************** */
    @GetMapping("/create-coupon")
    public Response createCoupon(CreateRequest request) {
        return couponService.createCoupon(request);
    }

    /********************************************************************************* */
    @DeleteMapping("/delete-coupon/{couponId}")
    public Response deleteCoupon(@PathVariable Integer couponId) {
        return couponService.deleteCoupon(couponId);
    }

    @GetMapping("/apply-coupon")
    public Response applyCoupon(ApplyCouponRequest coupon) {
        return couponService.applyCoupon(coupon);
    }

   

 
 
    
    
}