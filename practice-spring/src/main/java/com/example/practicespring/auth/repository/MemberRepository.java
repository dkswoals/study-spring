package com.example.practicespring.auth.repository;

import com.example.practicespring.auth.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
