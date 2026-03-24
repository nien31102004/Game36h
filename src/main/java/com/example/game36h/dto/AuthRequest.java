package com.example.game36h.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
    private String email; // For registration only
}
