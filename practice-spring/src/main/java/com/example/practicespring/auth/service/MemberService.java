package com.example.practicespring.auth.service;

import com.example.practicespring.auth.dto.MemberDataResponse;
import com.example.practicespring.auth.entity.Member;
import com.example.practicespring.auth.repository.MemberRepository;
import com.example.practicespring.global.exception.customException.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberDataResponse getMemberData(String email) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException("해당 맴버는 존재하지 않습니다.", HttpStatus.NOT_FOUND));
        return MemberDataResponse.from(member);
    }

}
