package com.example.elearningplatform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.example.elearningplatform.course.category.CategoryRepository;

import jakarta.transaction.Transactional;
import lombok.Setter;

@SpringBootApplication
@Setter
@EnableJpaRepositories
public class ELearningPlatformApplication
        implements ApplicationRunner {
    @Autowired
    CategoryRepository categoryRepository;
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

        // Category category = new Category();
        // category.setName("Programming");
        // category.setDescription("Programming");
        // categoryRepository.save(category);
        // Category category1 = new Category();
        // category1.setName("Design");
        // category1.setDescription("Design");
        // categoryRepository.save(category1);
        // Category category2 = new Category();
        // category2.setName("Marketing");
        // category2.setDescription("Marketing");
        // categoryRepository.save(category2);
        // Category category3 = new Category();
        // category3.setName("Business");
        // category3.setDescription("Business");
        // categoryRepository.save(category3);
        // Category category4 = new Category();
        // category4.setName("Photography");
        // category4.setDescription("Photography");
        // categoryRepository.save(category4); 
        // Category category5 = new Category();
        // category5.setName("Music");
        // category5.setDescription("Music");
        // categoryRepository.save(category5);
        // Category category6 = new Category();
        // category6.setName("Gaming");
        // category6.setDescription("Gaming");
        // categoryRepository.save(category6);
        // Category category7 = new Category();
        // category7.setName("Art");
        // category7.setDescription("Art");
        // categoryRepository.save(category7);
        // Category category8 = new Category();
        // category8.setName("Development");
        // category8.setDescription("Development");
        // categoryRepository.save(category8);
      
    }
}

