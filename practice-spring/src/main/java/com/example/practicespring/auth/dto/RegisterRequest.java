package com.example.practicespring.auth.dto;

public record RegisterRequest(String name, Long age, String email, String password) {

}
