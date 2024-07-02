package com.example.elearningplatform.resetpassword;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.signup.SignUpService;
import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.UserRepository;
import com.example.elearningplatform.validator.Validator;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;
    private final SignUpService signUpService;
    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;

    /*************************************************************************************************************/

    @PostMapping("/forget-password")
    public Response enterEmail(@RequestBody @Valid EnterEmailRequest email, BindingResult result,
            HttpServletRequest request)
            throws MessagingException, SQLException, IOException {

               if (result.hasErrors()) {
                   return Validator.validate(result);
               }

                    User user = userRepository.findByEmail(email.getEmail()).orElse(null);
        // System.out.println(user);
        if (user == null)
            return new Response(HttpStatus.BAD_REQUEST, "User not found", null);
        if (!user.isEnabled()) {
            return signUpService.sendRegistrationVerificationCode(email.getEmail(), request,
                    tokenUtil.generateToken(email.getEmail(), user.getId(), 900L));
        }
        return resetPasswordService.sendResetpasswordEmail(email.getEmail(), request,
                tokenUtil.generateToken(email.getEmail(), user.getId(), 900L));
    }

    /***************************************************************************************************************/

    @PostMapping("/change-password/{token}")
    public Response changePassword(@PathVariable("token") String token,@RequestBody @Valid ChangePasswordRequest data, BindingResult result,
            HttpServletRequest request)
            throws SQLException, IOException, MessagingException {
        if (result.hasErrors()) {
            return Validator.validate(result);
        }
        String email = tokenUtil.getUserName(token);

        return resetPasswordService.savePassword(email, data.getPassword());
    }

    /***************************************************************************************************************/
    @GetMapping("/check-token/{token}")
    public void savePassword(@PathVariable("token") String token, HttpServletResponse response)
            throws SQLException, IOException {

        String email = tokenUtil.getUserName(token);
        User user = userRepository.findByEmail(email).orElse(null);

        if (tokenUtil.isTokenExpired(token) || user == null) {
            response.sendRedirect("https://zakker.vercel.app/reset-password/?token=invalid");
            return;
            // return new Response(HttpStatus.BAD_REQUEST, "invalid token", null);
        }

        String tkn = tokenUtil.generateToken(email, user.getId(), 900L);
        response.sendRedirect("https://zakker.vercel.app/reset-password/?token=" + tkn);
        // return new Response(HttpStatus.OK, "valid token",
        // tokenUtil.getUserName(token));
    }
    /***************************************************************************************************************/

}
