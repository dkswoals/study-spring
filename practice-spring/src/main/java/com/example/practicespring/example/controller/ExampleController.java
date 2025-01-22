package com.example.practicespring.example.controller;

import com.example.practicespring.auth.entity.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Slf4j
public class ExampleController {

    @GetMapping("/auth")
    public ResponseEntity<String> authExample(@LoginUser String email) {
        log.info("email: {}", email);
        return ResponseEntity.ok(email);
    }

}
