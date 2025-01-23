package com.example.practicespring.auth.dto;

import com.example.practicespring.auth.entity.Member;

public record MemberDataResponse(String name, String email) {
    public static MemberDataResponse from(Member member) {
        return new MemberDataResponse(member.getName(), member.getEmail());
    }
}
