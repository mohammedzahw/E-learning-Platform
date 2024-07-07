package com.example.elearningplatform.payment.paypal;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.course.course.CourseService;
import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.payment.coupon.CouponService;
import com.example.elearningplatform.payment.coupon.dto.ApplyCouponRequest;
import com.example.elearningplatform.payment.coupon.dto.ApplyCouponRequestList;
import com.example.elearningplatform.payment.paypal.payin.PaypalService;
import com.example.elearningplatform.payment.paypal.transactions.TempTransactionUser;
import com.example.elearningplatform.payment.paypal.transactions.TempTransactionUserRepository;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.cart.CartRepository;
import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.UserRepository;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@RestController
@Data
@Slf4j
@SecurityRequirement(name = "bearerAuth")
@Transactional
public class PaypalController {

	@Autowired
	private PaypalService paypalService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TokenUtil tokenUtil;
	@Autowired
	private TempTransactionUserRepository tempTransactionUserRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private CourseService courseService;
	@Autowired
	private CouponService couponService;
	@Autowired
	private CartRepository cartRepository;

	private final String prefixHttp = "http://";

	/************************************
	 * CREATE PAYMENT ****************************************
	 */
	@GetMapping("/enroll-course")
	public Response enrollCourse(@RequestBody ApplyCouponRequest coupon) {
	try {
	Response response = couponService.applyCoupon(coupon);
	if (response.getStatus() == HttpStatus.OK) {
	User user = userRepository.findById(tokenUtil.getUserId())
	.orElseThrow(() -> new CustomException("User not found",
	HttpStatus.NOT_FOUND));
	Course course = courseRepository.findById(coupon.getCourseId())
	.orElseThrow(() -> new CustomException("Course not found",
	HttpStatus.NOT_FOUND));
	courseRepository.enrollCourse(user.getId(), course.getId());
		

	cartRepository.removeFromCart(user.getId(), course.getId());

	course.incrementNumberOfEnrollments();

	return new Response(HttpStatus.OK, "Course enrolled successfully", null);

	}
	return response;
	} catch (CustomException e) {
	return new Response(e.getStatus(), e.getMessage(), null);
	} catch (Exception e) {
	return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
	}
	}

	/************************************************************************************************** */
	@GetMapping("checkout")
	public Response checkouta() {

		List<Course> courses = cartRepository.findCartCourses(tokenUtil.getUserId());
		for (Course course : courses) {

		courseRepository.enrollCourse(tokenUtil.getUserId(), course.getId());
		cartRepository.removeFromCart(tokenUtil.getUserId(), course.getId());
		course.incrementNumberOfEnrollments();
	}
	return new Response(HttpStatus.OK, "Checkout successfully", null);

}

	/************************************************************************************************** */
	@GetMapping("/paypal")
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("paypal");
		modelAndView.addObject("applyCouponRequest", new ApplyCouponRequest());
		return modelAndView;
	}

	/************************************************************************************************** */

	@PostMapping("/payment/create")
	public RedirectView createPayment(@RequestBody ApplyCouponRequest applyCouponRequest,HttpServletResponse response) {
		try {
			// System.out.println(applyCouponRequest);
			User user = userRepository.findById(tokenUtil.getUserId()).orElse(null);
			if (user == null) {
				log.error("User not found");
				return new RedirectView("/payment/error");
			}

			if(courseService.ckeckCourseSubscribe(applyCouponRequest.getCourseId())) 
				return new RedirectView("/payment/error");

			// if(courseService.ckeckCourseSubscribe(applyCouponRequest.getCourseId())) {
			// return new RedirectView("/course/public/get-course/" +
			// applyCouponRequest.getCourseId());
			// }
			Payment payment = paypalService.createPayment(applyCouponRequest);
			for (Links links : payment.getLinks()) {
				if (links.getRel().equals("approval_url")) {
					return new RedirectView(links.getHref());
				}
			}
		} catch (PayPalRESTException e) {
			log.error(e.getMessage());

			return new RedirectView("/payment/error");
		}
		return new RedirectView("/payment/error");
	}

	/***************************************************************************************** */
	/************************************************************************************************** */
	@GetMapping("/payment/checkout")
	public RedirectView checkout() {
		try {
			User user = userRepository.findById(tokenUtil.getUserId()).orElse(null);
			if (user == null) {
				return new RedirectView("https://zakker.vercel.app/cart");
			}
			Payment payment = paypalService.checkout();
			for (Links links : payment.getLinks()) {
				if (links.getRel().equals("approval_url")) {
					return new RedirectView(links.getHref());
				}
			}
		} catch (PayPalRESTException e) {
			log.error(e.getMessage());
			return new RedirectView("https://zakker.vercel.app/cart");
		}
		return new RedirectView("https://zakker.vercel.app/cart");
	}

	/*********************************************
	 * SUCCESS
	 * *
	 * 
	 * @throws Exception
	 *                   **********************************************************************
	 */

	@GetMapping("/payment/success")
	public RedirectView paymentSuccess(HttpServletResponse response,
			@RequestParam("paymentId") String paymentId,
			@RequestParam("PayerID") String payerId) throws Exception {
		try {

			List<TempTransactionUser> tempTransactionUserList = tempTransactionUserRepository
					.findByPaymentIdAndUserId(paymentId, tokenUtil.getUserId());
					if(tempTransactionUserList.size()==0) {
						log.error("No transaction found");
						return new RedirectView("https://zakker.vercel.app/cart");
					}
			
			Payment payment = paypalService.executePayment(paymentId, payerId);
			if (payment.getState().equals("approved")) {
				for (TempTransactionUser tempTransactionUser : tempTransactionUserList) {
					tempTransactionUser.setPaymentId(paymentId);
					tempTransactionUser.setConfirmed(true);

					tempTransactionUser.setConfirmDate(LocalDateTime.now());
					if (tempTransactionUser.getCouponId() != null) {

						couponService.decrementCoupon(tempTransactionUser.getCouponId());
					}

					Course course = courseRepository.findById(tempTransactionUser.getCourseId())
							.orElseThrow(() -> new CustomException(
									"Course not found", HttpStatus.NOT_FOUND));

					courseRepository.enrollCourse(tempTransactionUser.getUserId(), tempTransactionUser.getCourseId());
					cartRepository.removeFromCart(tokenUtil.getUserId(), course.getId());
					course.incrementNumberOfEnrollments();
					tempTransactionUserRepository.save(tempTransactionUser);
				}
				
				return new RedirectView("https://zakker.vercel.app/mycourses");
			// 	tempTransactionUser.setConfirmed(true);

			// 	tempTransactionUser.setConfirmDate(LocalDateTime.now());
			// 	if (tempTransactionUser.getCouponId() != null) {

			// 		couponService.decrementCoupon(tempTransactionUser.getCouponId());
			// 	}

			// 	Course course = courseRepository.findById(tempTransactionUser.getCourseId())
			// 			.orElseThrow(() -> new CustomException(
			// 					"Course not found", HttpStatus.NOT_FOUND));

			// 	courseRepository.enrollCourse(tempTransactionUser.getUserId(), tempTransactionUser.getCourseId());
			// 	course.incrementNumberOfEnrollments();
			// 	tempTransactionUserRepository.save(tempTransactionUser);

			// 	response.sendRedirect("/course/public/get-course/" + tempTransactionUser.getCourseId());
			// 	return;
			}
			
		} catch (CustomException e) {
			log.error(e.getMessage());
			return new RedirectView("https://zakker.vercel.app/cart");
		} catch (PayPalRESTException e) {

			log.error(e.getMessage());
			return new RedirectView("https://zakker.vercel.app/cart");
		}
		return new RedirectView("https://zakker.vercel.app/cart");

	}

	/******************************
	 * CANCEL PAYMENT ****************************************
	 */
	@GetMapping("/payment/cancel")
	public Response paymentCancel() throws IOException {
		return new Response(HttpStatus.BAD_REQUEST, "Payment cancelled", null);

	}

	/***************************************
	 * ERROR ***********************************************************************
	 */

	@GetMapping("/payment/error")
	public Response paymentError() {
		return new Response(HttpStatus.BAD_REQUEST, "Payment error", null);
	}
}