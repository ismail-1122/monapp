package com.example.monapp;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringInitializrApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringInitializrApplication.class, args);
    }

    @Bean
    ApplicationRunner init(UserRepository userRepository) {
        return args -> {

            userRepository.findByEmail("ismail@gmail.com").ifPresentOrElse((user) -> {
                    },
                    () -> {
                        User user = new User();
                        user.setName("ismail");
                        user.setEmail("ismail@gmail.com");
                        userRepository.save(user);
                    });

        };
    }

}
