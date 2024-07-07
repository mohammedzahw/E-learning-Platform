package com.example.elearningplatform.signup;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.UserRepository;
import com.example.elearningplatform.validator.Validator;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@RestController
@AllArgsConstructor
@NoArgsConstructor
public class SignUpController {

    @Autowired
    private SignUpService signUpService;

   @Autowired
   private UserRepository userRepository;
   @Autowired
   private TokenUtil tokenUtil;

    /******************************************************************************************************************/

    /******************************************************************************************************************/

    @PostMapping(value = "/signup")

    public Response signUp(@Valid @RequestBody SignUpRequest signUpRequest, BindingResult result,
            HttpServletRequest request) throws MessagingException, IOException, SQLException {
        // return new Response(HttpStatus.OK, "ok", signUpRequest.getEmail());
        if (result.hasErrors()) {
            return Validator.validate(result);
        }

        User user = userRepository.findByEmail(signUpRequest.getEmail()).orElse(null);
        if (user != null) {
            return new Response(HttpStatus.BAD_REQUEST, "Email already exists , Please login", null);

        }

        Response response = signUpService.registerUser(signUpRequest);

        if (response.getStatus() != HttpStatus.OK) {
            return response;
        }
        user = (User) response.getData();

        String token = tokenUtil.generateToken(signUpRequest.getEmail(),1000, 1000L);

        response = signUpService.sendRegistrationVerificationCode(signUpRequest.getEmail(), request,
                token);
        if (response.getStatus() != HttpStatus.OK) {
            return response;
        }
        response.setMessage("Registration successful, please check your email to verify your account");
        return response;

    }

    /******************************************************************************************************************/

    @GetMapping("/verifyEmail/{token}")

    public void verifyEmail(@PathVariable("token") String verficationToken, HttpServletResponse response)
            throws SQLException, IOException {
        // System.out.println(verficationToken);

        signUpService.verifyEmail(verficationToken, response);
    }

}