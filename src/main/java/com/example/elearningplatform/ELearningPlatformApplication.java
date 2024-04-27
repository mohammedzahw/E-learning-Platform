package com.example.elearningplatform;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.UserRepository;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class ELearningPlatformApplication
        implements ApplicationRunner {
    // private final GenerateData generateData;

    // private final ReviewRepository reviewRepository;
    // private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    // private final CartRepository cartRepository;
    /*********************************************************************************** */
    public static void main(String[] args) {
        SpringApplication.run(ELearningPlatformApplication.class, args);

    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Hello World from Application Runner");
 
        // System.out.println(user.);

        // generateData.truncateDtabase();
        // generateData.createData();
        // generateData.setRelationships();
    }
}

