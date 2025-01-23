package com.example.practicespring.auth.service;

import com.example.practicespring.auth.dto.LoginRequest;
import com.example.practicespring.auth.dto.RefreshRequest;
import com.example.practicespring.auth.dto.RegisterRequest;
import com.example.practicespring.auth.dto.TokenResponse;
import com.example.practicespring.auth.entity.Member;
import com.example.practicespring.auth.repository.MemberRepository;
import com.example.practicespring.global.exception.customException.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public void register(RegisterRequest registerRequest) {
        if (memberRepository.findByEmail(registerRequest.email()).isPresent()) {
            throw new DuplicateKeyException("중복되는 이메일입니다.");
        }
        Member newMember = Member.builder()
            .email(registerRequest.email())
            .password(registerRequest.password())
            .name(registerRequest.name())
            .age(registerRequest.age())
            .build();
        memberRepository.save(newMember);
    }

    @Transactional
    public TokenResponse login(LoginRequest request) throws NotFoundException {
        Member member = memberRepository.findByEmail(request.email())
            .orElseThrow(() -> new CustomException("Invalid credentials", HttpStatus.NOT_FOUND));
        if (!member.getPassword().equals(request.password())) {
            throw new CustomException("Invalid credentials", HttpStatus.BAD_REQUEST);
        }
        return createToken(member);
    }

    private TokenResponse createToken(Member member) {
        String accessToken = jwtUtil.generateAccessToken(member.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(member.getEmail());
        member.updateRefreshToken(refreshToken);
        memberRepository.save(member);
        return new TokenResponse(accessToken, refreshToken);
    }

    @Transactional
    public TokenResponse refresh(RefreshRequest request) throws NotFoundException {
        String refreshToken = request.refreshToken();

        if (!jwtUtil.validateToken(refreshToken)) {
            throw new CustomException("Invalid refresh token", HttpStatus.UNAUTHORIZED);
        }

        String email = jwtUtil.getEmailFromToken(refreshToken);
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        member.validateRefreshToken(refreshToken);

        String newAccessToken = jwtUtil.generateAccessToken(email);
        return new TokenResponse(newAccessToken, null);
    }

}
