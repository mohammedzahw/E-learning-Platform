package com.example.elearningplatform.payment.paypal.payin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.course.Course;
import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.payment.coupon.CouponRepository;
import com.example.elearningplatform.payment.coupon.CouponService;
import com.example.elearningplatform.payment.paypal.transactions.TempTransactionUser;
import com.example.elearningplatform.payment.paypal.transactions.TempTransactionUserRepository;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.cart.CartRepository;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

@Service
@Data
public class PaypalService {

      @Autowired
      private APIContext apiContext;
      @Autowired
      private CouponService couponService;
      @Autowired
      private HttpServletRequest request;
      @Autowired
      private TempTransactionUserRepository tempTransactionUserRepository;
      @Autowired
      private CouponRepository couponRepository;
      @Autowired
      private TokenUtil tokenUtil;
      @Autowired
      private CartRepository cartRepository;
      @Autowired
      private CourseRepository courseRepository;


      /****************************************************************************************/
      public Payment createPayment(Course course, String token) throws PayPalRESTException {
         
           
            String successUrl = "http://" + request.getServerName() + ":" + request.getServerPort()
                        + request.getContextPath()
                        + "/payment/create/success/" + token + "/" + course.getId();
            String cancelUrl = "http://" + request.getServerName() + ":" + request.getServerPort()
                        + request.getContextPath()
                        + "/payment/cancel";
            Integer userId = tokenUtil.tokenGetID(token);

            Double price = course.getPrice();

            Amount amount = new Amount();
            amount.setCurrency("USD");
            amount.setTotal(String.format(Locale.forLanguageTag("en_US"), "%.2f", price * 0.021));

            Transaction transaction = new Transaction();
            transaction.setDescription(null);
            transaction.setAmount(amount);

            List<Transaction> transactions = new ArrayList<>();
            transactions.add(transaction);

            Payer payer = new Payer();
            payer.setPaymentMethod("paypal");
        

            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setCancelUrl(cancelUrl);
            redirectUrls.setReturnUrl(successUrl);

            Payment payment = new Payment();
            payment.setIntent("sale");
            payment.setPayer(payer);
            payment.setTransactions(transactions);
            payment.setRedirectUrls(redirectUrls);

            payment = payment.create(apiContext);

            TempTransactionUser tempTransactionUser = new TempTransactionUser();
            tempTransactionUser.setCourseId(course.getId());
            tempTransactionUser.setUserId(userId);
   
            tempTransactionUser.setPrice(((int) (price * 100)));
            tempTransactionUser.setConfirmed(false);
      
            tempTransactionUser.setPaymentId(payment.getId());
            tempTransactionUser.setCurrency("USD");
            tempTransactionUser.setPaymentMethod("paypal");
            tempTransactionUserRepository.save(tempTransactionUser);
            // System.out.println("Created Payment ID: " + payment.toString());
            return payment;
      
     
}

      /**************************************************** */
      public Payment checkout(String token,List<Course> courses,Double price) throws PayPalRESTException {

            String successUrl = "http://" + request.getServerName() + ":" + request.getServerPort()
                        + request.getContextPath()
                        + "/payment/checkout/success/" + token;
            String cancelUrl = "http://" + request.getServerName() + ":" + request.getServerPort()
                        + request.getContextPath()
                        + "/payment/cancel";
            
            Amount amount = new Amount();
            amount.setCurrency("USD");
            amount.setTotal(String.format(Locale.forLanguageTag("en_US"), "%.2f", price));

            Transaction transaction = new Transaction();
            transaction.setDescription(null);
            transaction.setAmount(amount);

            List<Transaction> transactions = new ArrayList<>();
            transactions.add(transaction);

            Payer payer = new Payer();
            payer.setPaymentMethod("paypal");

            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setCancelUrl(cancelUrl);
            redirectUrls.setReturnUrl(successUrl);

            Payment payment = new Payment();
            payment.setIntent("sale");
            payment.setPayer(payer);
            payment.setTransactions(transactions);
            payment.setRedirectUrls(redirectUrls);

            payment = payment.create(apiContext);

            for (Course course : courses) {
                  TempTransactionUser tempTransactionUser = new TempTransactionUser();
                  tempTransactionUser.setCourseId(course.getId());
                  tempTransactionUser.setUserId(tokenUtil.tokenGetID(token));

                  tempTransactionUser.setPrice((int) (course.getPrice() * 100));
                  tempTransactionUser.setConfirmed(false);

                  tempTransactionUser.setPaymentId(payment.getId());
                  tempTransactionUser.setCurrency("USD");
                  tempTransactionUser.setPaymentMethod("paypal");
                  tempTransactionUserRepository.save(tempTransactionUser);
            }
                  return payment;
            }

/********************************************** PayPal Payment Execution ************************************************/

      public Payment executePayment(
                  String paymentId,
                  String payerId) throws PayPalRESTException {
            Payment payment = new Payment();

            payment.setId(paymentId);

            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payerId);

            return payment.execute(apiContext, paymentExecution);
      }
}