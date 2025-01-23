package com.example.practicespring.global.resolver;

import com.example.practicespring.auth.entity.LoginUser;
import com.example.practicespring.auth.entity.Member;
import com.example.practicespring.auth.repository.MemberRepository;
import com.example.practicespring.auth.service.JwtUtil;
import com.example.practicespring.global.exception.customException.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class)
            && parameter.getParameterType().equals(String.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        log.info("리졸버 진입");

        String authHeader = webRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomException("Invalid token format", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        String email = jwtUtil.getEmailFromToken(token);

        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND)).getEmail();
    }
}
