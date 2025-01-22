package com.example.practicespring.auth.entity;

import com.example.practicespring.global.entity.BaseEntity;
import com.example.practicespring.global.exception.customException.CustomException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Entity
@Getter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String refreshToken;

    @Builder
    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    protected Member() {

    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void validateRefreshToken(String refreshToken) {
        if (!this.refreshToken.equals(refreshToken)) {
            throw new CustomException("Refresh token mismatch", HttpStatus.BAD_REQUEST);
        }
    }

}
