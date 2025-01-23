package com.example.practicespring.auth.controller;

import com.example.practicespring.auth.dto.MemberDataResponse;
import com.example.practicespring.auth.entity.LoginUser;
import com.example.practicespring.auth.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "맴버 서비스")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "현재 로그인한 유저의 정보를 반환")
    @GetMapping("/info")
    public ResponseEntity<MemberDataResponse> getMemberInfo(@LoginUser String email) {
        return ResponseEntity.ok(memberService.getMemberData(email));
    }
}
