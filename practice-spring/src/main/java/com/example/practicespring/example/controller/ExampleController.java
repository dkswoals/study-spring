package com.example.practicespring.example.controller;

import com.example.practicespring.auth.entity.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Test", description = "토큰 기능 확인 API")
@RestController
@RequestMapping("/api/test")
@Slf4j
public class ExampleController {

    @Operation(summary = "accessToken를 헤더에 넣고 요청")
    @GetMapping("/auth")
    public ResponseEntity<String> authExample(@LoginUser String email) {
        log.info("email: {}", email);
        return ResponseEntity.ok(email);
    }

}
