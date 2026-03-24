package com.example.game36h.dto;

import lombok.Data;

@Data
public class RatingRequest {
    private Long gameId;
    private Integer score; // 1-5
}
