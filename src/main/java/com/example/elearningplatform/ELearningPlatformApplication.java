package com.example.elearningplatform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.example.elearningplatform.course.course.CourseRepository;
import com.example.elearningplatform.course.review.Review;
import com.example.elearningplatform.course.review.ReviewRepository;
import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.Setter;

@SpringBootApplication
@Setter
@EnableJpaRepositories
public class ELearningPlatformApplication
        implements ApplicationRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ReviewRepository reviewRepository;



    /*********************************************************************************** */
    public static void main(String[] args) {
        SpringApplication.run(ELearningPlatformApplication.class, args);

    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("running");
        System.out.println("running");
        System.out.println("running");
        // User user = userRepository.findById(2002).get();
        // Review review = new Review();
        // review.setContent("very good");
        // review.setRating(5.0);
        // review.setUser(user);
        // review.setCreationDate(java.time.LocalDate.now());
        // reviewRepository.save(review);

        // User user2 = userRepository.findById(1902).get();
        // Review review2 = new Review();
        // review2.setContent("very very good");
        // review2.setRating(5.0);
        // review2.setUser(user2);
        // review2.setCreationDate(java.time.LocalDate.now());
        // reviewRepository.save(review);

        // User user3 = userRepository.findById(1702).get();
        // Review review3 = new Review();
        // review3.setContent("very very good");
        // review3.setRating(5.0);
        // review3.setUser(user3);
        // review3.setCreationDate(java.time.LocalDate.now());
        // reviewRepository.save(review3);


        // generateData.truncateDtabase();
        // generateData.createData();
        // generateData.setRelationships();
    }
}

