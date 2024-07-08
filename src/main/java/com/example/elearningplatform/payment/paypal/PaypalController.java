package com.example.elearningplatform.payment.paypal;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.course.course.CourseService;
import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.payment.coupon.CouponService;
import com.example.elearningplatform.payment.coupon.dto.ApplyCouponRequest;
import com.example.elearningplatform.payment.paypal.payin.PaypalService;
import com.example.elearningplatform.payment.paypal.transactions.TempTransactionUser;
import com.example.elearningplatform.payment.paypal.transactions.TempTransactionUserRepository;
import com.example.elearningplatform.payment.transaction.Transaction;
import com.example.elearningplatform.payment.transaction.TransactionRepository;
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
	@Autowired
	private TransactionRepository transactionRepository;

	private final String prefixHttp = "http://";

	/************************************
	 * CREATE PAYMENT ****************************************
	 */
	@SecurityRequirement(name = "bearerAuth")
	@PostMapping("/enroll-course")
	public Response enrollCourse(@RequestBody ApplyCouponRequest request) {
	try {
		Response response = couponService.applyCoupon(request);
	if (response.getStatus() == HttpStatus.OK) {
	User user = userRepository.findById(tokenUtil.getUserId())
	.orElseThrow(() -> new CustomException("User not found",
	HttpStatus.NOT_FOUND));
	Course course = courseRepository.findById(request.getCourseId())
	.orElseThrow(() -> new CustomException("Course not found",
	HttpStatus.NOT_FOUND));
	courseRepository.enrollCourse(user.getId(), course.getId());

	cartRepository.removeFromCart(user.getId(), course.getId());

	course.incrementNumberOfEnrollments();
	courseRepository.save(course);

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
	@SecurityRequirement(name = "bearerAuth")
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
	@GetMapping("/payment/checkout/{token}")
	public RedirectView checkout(@PathVariable("token") String token) {
		try {
			User user = userRepository.findById(tokenUtil.tokenGetID(token)).orElse(null);
			if (user == null) {
				return new RedirectView("https://zakker.vercel.app/cart");
			}
			Double price = 0.0;
			List<Course> courses = cartRepository.findCartCourses(tokenUtil.tokenGetID(token));
			Iterator<Course> iterator = courses.iterator();
			while (iterator.hasNext()) {
				Course course = iterator.next();
				// System.out.println(course.getPrice());

				if (course.getPrice().equals(0.0)) {
					// System.out.println("zero");
					courseRepository.enrollCourse(tokenUtil.tokenGetID(token), course.getId());
					cartRepository.removeFromCart(tokenUtil.tokenGetID(token), course.getId());
					course.incrementNumberOfEnrollments();
					iterator.remove(); // Use iterator's remove method to safely remove the current element
					courseRepository.save(course);
				} else {
					price += course.getPrice();
				}
			}

			if (price == 0.0) {
				return new RedirectView("https://zakker.vercel.app/mycourses");
			}
			Payment payment = paypalService.checkout(token, courses, price);
			for (Links links : payment.getLinks()) {
				if (links.getRel().equals("approval_url")) {
					
					return new RedirectView(links.getHref());
				}
			}
			return new RedirectView("https://zakker.vercel.app/cart");
		} catch (PayPalRESTException e) {
			log.error(e.getMessage());
			return new RedirectView("https://zakker.vercel.app/cart");
		}

	}

	/*************************************************************************************************/

	@GetMapping("/payment/create/{courseId}/{token}")
	public RedirectView createPayment(@PathVariable("courseId") Integer courseId, @PathVariable("token") String token) {
		try {
			// System.out.println(applyCouponRequest);
			User user = userRepository.findById(tokenUtil.tokenGetID(token)).orElse(null);
			if (user == null) {
				log.error("User not found");
				return new RedirectView("https://zakker.vercel.app/course/" + courseId);
			}

			Course course = courseRepository.findById(courseId).orElseThrow(
					() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));
			if (course.getPrice().equals(0.0)) {
				courseRepository.enrollCourse(tokenUtil.tokenGetID(token), courseId);
				cartRepository.removeFromCart(tokenUtil.tokenGetID(token), courseId);
				course.incrementNumberOfEnrollments();
				courseRepository.save(course);
				return new RedirectView("https://zakker.vercel.app/course/" + courseId);
			}

			Payment payment = paypalService.createPayment(course, token);
			for (Links links : payment.getLinks()) {
				if (links.getRel().equals("approval_url")) {
					return new RedirectView(links.getHref());
				}
			}
			return new RedirectView("https://zakker.vercel.app/course/" + courseId);
		} catch (PayPalRESTException e) {
			log.error(e.getMessage());
			return new RedirectView("https://zakker.vercel.app/course/" + courseId);
		}
	}

	/*********************************************************************************************************/

	@GetMapping("/payment/checkout/success/{token}")
	public RedirectView payment(HttpServletResponse response,
			@RequestParam("paymentId") String paymentId,
			@RequestParam("PayerID") String payerId, @PathVariable("token") String token)
			throws Exception {
		if (paymentSuccess(paymentId, payerId, token)) {
			return new RedirectView("https://zakker.vercel.app/mycourses");
		}

		return new RedirectView("https://zakker.vercel.app/cart");

	}

	/*********************************************************************************************************/
	@GetMapping("/payment/create/success/{token}/{courseId}")
	public RedirectView paymentcreat(HttpServletResponse response,
			@RequestParam("paymentId") String paymentId,
			@RequestParam("PayerID") String payerId,
			@PathVariable("token") String token,
			@PathVariable("courseId") Integer courseId)
			throws Exception {
		if (paymentSuccess(paymentId, payerId, token)) {
			return new RedirectView("https://zakker.vercel.app/course/" + courseId);
		}

		return new RedirectView("https://zakker.vercel.app/course/" + courseId);

	}

	/***************************************************************************************************** */
	public Boolean paymentSuccess(
			String paymentId,
			String payerId, String token) throws Exception {
		try {

			List<TempTransactionUser> tempTransactionUserList = tempTransactionUserRepository
					.findByPaymentIdAndUserId(paymentId, tokenUtil.tokenGetID(token));
					if(tempTransactionUserList.size()==0) {
						log.error("No transaction found");
						return false;
					}
			
			Payment payment = paypalService.executePayment(paymentId, payerId);
			if (payment.getState().equals("approved")) {
				for (TempTransactionUser tempTransactionUser : tempTransactionUserList) {
					// System.out.println(tempTransactionUser);
					tempTransactionUser.setPaymentId(paymentId);
					tempTransactionUser.setConfirmed(true);

					tempTransactionUser.setConfirmDate(LocalDateTime.now());

					Course course = courseRepository.findById(tempTransactionUser.getCourseId())
							.orElseThrow(() -> new CustomException(
									"Course not found", HttpStatus.NOT_FOUND));

					courseRepository.enrollCourse(tempTransactionUser.getUserId(), tempTransactionUser.getCourseId());
					cartRepository.removeFromCart(tokenUtil.tokenGetID(token), course.getId());
					course.incrementNumberOfEnrollments();
					Transaction transaction = new Transaction();
					transaction.setCourse(course);
					transaction.setUser(userRepository.findById(tempTransactionUser.getUserId()).get());
					transaction.setPaymentDate(tempTransactionUser.getConfirmDate());
					transaction.setPaymentMethod(tempTransactionUser.getPaymentMethod());
					transaction.setPrice(tempTransactionUser.getPrice());
					transactionRepository.save(transaction);

					tempTransactionUserRepository.save(tempTransactionUser);
				}

			}
			return true;
			
		} catch (CustomException e) {
			log.error(e.getMessage());
			return false;
		} catch (PayPalRESTException e) {

			log.error(e.getMessage());
			return false;
		}
	}
	

	/******************************
	 * CANCEL PAYMENT ****************************************
	 */
	@GetMapping("/payment/cancel")
	public RedirectView paymentCancel() throws IOException {
		return new RedirectView("https://zakker.vercel.app/cart");

	}

	/***************************************
	 * ERROR ***********************************************************************
	 */

	@GetMapping("/payment/error")
	public RedirectView paymentError() {
		return new RedirectView("https://zakker.vercel.app/cart");
	}
}