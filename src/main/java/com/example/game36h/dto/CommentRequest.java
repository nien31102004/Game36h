package com.example.game36h.dto;

import lombok.Data;

@Data
public class CommentRequest {
    private Long gameId;
    private String content;
}
