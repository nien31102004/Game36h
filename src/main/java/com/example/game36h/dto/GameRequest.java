package com.example.game36h.dto;

import lombok.Data;

@Data
public class GameRequest {
    private String title;
    private String description;
    private String thumbnail;
    private String gameUrl;
    private Long categoryId;
}
