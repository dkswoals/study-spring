package com.example.practicespring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PracticeSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(PracticeSpringApplication.class, args);
    }

}
