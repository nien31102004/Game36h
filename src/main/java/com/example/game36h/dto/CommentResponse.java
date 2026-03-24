package com.example.game36h.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {
    private Long id;
    private UserDto user;
    private String content;
    private LocalDateTime createdAt;
}
