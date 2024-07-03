package com.example.elearningplatform.login;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.validator.Validator;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor

public class LoginController implements ErrorController {

    private final LoginService loginService;


    /***************************************************************************************************************/
    @PostMapping("/login/custom")
    public Response loginCustom(@RequestBody @Valid LoginRequest loginRequest, BindingResult result,
            HttpServletRequest request)
            throws MessagingException, SQLException, IOException {

        if (result.hasErrors()) {

            return Validator.validate(result);
        }

        return loginService.verifyLogin(loginRequest, request);
    }

    /**
     * @throws IOException
     ***************************************************************************************************************/
    @GetMapping("/login/google")
    public RedirectView loginWithGoogle(HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    RedirectView redirectView = new RedirectView();
    redirectView.setUrl(baseUrl + "/oauth2/authorization/google");
    return redirectView;

    }

    /*****************************************************************************************************************/
    @GetMapping("/login/github")
    public RedirectView loginWithGithub(HttpServletRequest request, HttpServletRequest response) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    RedirectView redirectView = new RedirectView();
        redirectView.setUrl(baseUrl + "/oauth2/authorization/github");
        return redirectView;
       
    }

    /*****************************************************************************************************************/

    @GetMapping("/login/oauth2/success")
    public RedirectView loginOuth2(@AuthenticationPrincipal OAuth2User oAuth2User)
            throws SerialException, IOException, SQLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication instanceof OAuth2AuthenticationToken) {
                OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                String registrationId = oauthToken.getAuthorizedClientRegistrationId();
                Response res = loginService.loginOuth2(oAuth2User.getAttributes(), registrationId);
                if (res.getStatus() == HttpStatus.OK) {
                    String token = res.getData().toString();
                    return new RedirectView("https://zakker.vercel.app/?token=" + token);
                } else {
                    return new RedirectView("/login/error");
                }
            } else {
                return new RedirectView("/login/error");
            }
        }

/************************************************************************************************************** */


    @GetMapping("/login-error")
    public String loginSuccess() {
        return "error";
    }

}


/*****************************************************************************************************************/
