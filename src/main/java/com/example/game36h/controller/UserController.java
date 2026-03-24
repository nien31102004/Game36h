package com.example.game36h.controller;

import com.example.game36h.dto.UserDto;
import com.example.game36h.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserIdFromUserDetails(userDetails);
        UserDto user = userService.getUserProfile(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateCurrentUser(
            @Valid @RequestBody UserDto userDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Long userId = getUserIdFromUserDetails(userDetails);
        UserDto updatedUser = userService.updateUserProfile(userId, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    private Long getUserIdFromUserDetails(UserDetails userDetails) {
        return 1L; // Placeholder - implement proper user ID extraction
    }
}
