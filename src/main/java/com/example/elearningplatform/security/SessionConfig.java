// package com.example.elearningplatform.security;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.session.web.http.CookieSerializer;
// import org.springframework.session.web.http.DefaultCookieSerializer;

// @Configuration
// public class SessionConfig {

//     @Bean
//     public CookieSerializer cookieSerializer() {
//         DefaultCookieSerializer serializer = new DefaultCookieSerializer();
//         serializer.setSameSite("Strict");
//         serializer.setUseSecureCookie(true);
//         return serializer;
//     }
// }
