package com.skillsync.feedback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.skillsync.feedback",          // Main application package
    "com.skillsync.controller",        // Controller package
    "com.skillsync.repository",        // Repository package
    "com.skillsync.model"              // Model package
})
@EntityScan(basePackages = "com.skillsync.model") // JPA entities
@EnableJpaRepositories(basePackages = "com.skillsync.repository") // JPA repositories
public class SkillsyncFeedbackApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkillsyncFeedbackApplication.class, args);
    }
}