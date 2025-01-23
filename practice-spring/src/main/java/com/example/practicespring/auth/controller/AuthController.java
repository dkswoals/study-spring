package com.example.practicespring.auth.controller;

import com.example.practicespring.auth.dto.LoginRequest;
import com.example.practicespring.auth.dto.RefreshRequest;
import com.example.practicespring.auth.dto.RegisterRequest;
import com.example.practicespring.auth.dto.TokenResponse;
import com.example.practicespring.auth.service.AuthService;
import com.example.practicespring.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "사용자 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "이메일과 비밀번호로 사용자를 등록합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "생성 성공"),
        @ApiResponse(responseCode = "400", description = "이메일 중복", content = @Content(schema = @Schema(implementation = com.example.practicespring.global.exception.ErrorResponse.class))),
    })
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "로그인")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "로그인 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 이메일", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "비밀번호 불일치", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest,
        HttpServletResponse response)
        throws NotFoundException {
        TokenResponse tokenResponse = authService.login(loginRequest);

        Cookie refreshTokenCookie = new Cookie("refreshToken", tokenResponse.refreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(new TokenResponse(tokenResponse.accessToken(), null));
    }

    @Operation(summary = "토큰 리프레시")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "갱신 성공"),
        @ApiResponse(responseCode = "401", description = "리프레시 토큰 만료", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(
        @CookieValue(name = "refreshToken") String refreshToken)
        throws NotFoundException {
        return ResponseEntity.ok(authService.refresh(new RefreshRequest(refreshToken)));
    }
}
