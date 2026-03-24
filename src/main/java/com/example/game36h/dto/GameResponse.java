package com.example.game36h.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GameResponse {
    private Long id;
    private String title;
    private String description;
    private String thumbnail;
    private String gameUrl;
    private CategoryDto category;
    private UserDto user;
    private String status;
    private Long views;
    private LocalDateTime createdAt;
    private Double averageRating;
}
