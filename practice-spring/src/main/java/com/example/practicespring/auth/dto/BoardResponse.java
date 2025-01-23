package com.example.practicespring.auth.dto;

public record BoardResponse(String title, String content, String author) {
    public static BoardResponse of(String title, String content, String author) {
        return new BoardResponse(title, content, author);
    }
}
