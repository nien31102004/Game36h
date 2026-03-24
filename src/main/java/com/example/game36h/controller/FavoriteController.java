package com.example.game36h.controller;

import com.example.game36h.entity.Favorite;
import com.example.game36h.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/{gameId}")
    public ResponseEntity<Favorite> toggleFavorite(
            @PathVariable Long gameId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Long userId = getUserIdFromUserDetails(userDetails);
        Favorite favorite = favoriteService.toggleFavorite(gameId, userId);
        
        if (favorite == null) {
            // Game was unfavorited
            return ResponseEntity.noContent().build();
        } else {
            // Game was favorited
            return ResponseEntity.ok(favorite);
        }
    }

    @GetMapping
    public ResponseEntity<Page<Favorite>> getUserFavorites(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Long userId = getUserIdFromUserDetails(userDetails);
        Pageable pageable = PageRequest.of(page, size);
        Page<Favorite> favorites = favoriteService.getUserFavorites(userId, pageable);
        return ResponseEntity.ok(favorites);
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> removeFavorite(
            @PathVariable Long gameId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Long userId = getUserIdFromUserDetails(userDetails);
        favoriteService.toggleFavorite(gameId, userId); // This will remove it
        return ResponseEntity.noContent().build();
    }

    private Long getUserIdFromUserDetails(UserDetails userDetails) {
        return 1L; // Placeholder - implement proper user ID extraction
    }
}
