package com.example.game36h.controller;

import com.example.game36h.dto.RatingRequest;
import com.example.game36h.entity.Rating;
import com.example.game36h.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ratings")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @PostMapping
    public ResponseEntity<Rating> createRating(
            @Valid @RequestBody RatingRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Long userId = getUserIdFromUserDetails(userDetails);
        Rating rating = ratingService.createRating(request, userId);
        return ResponseEntity.ok(rating);
    }

    private Long getUserIdFromUserDetails(UserDetails userDetails) {
        return 1L; // Placeholder - implement proper user ID extraction
    }
}

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
class GameRatingController {

    @Autowired
    private RatingService ratingService;

    @GetMapping("/{id}/ratings")
    public ResponseEntity<Map<String, Object>> getGameRatings(@PathVariable Long id) {
        List<Rating> ratings = ratingService.getRatingsByGameId(id);
        Double averageRating = ratingService.getAverageRating(id);
        Long ratingCount = ratingService.getRatingCount(id);

        Map<String, Object> response = new HashMap<>();
        response.put("ratings", ratings);
        response.put("averageRating", averageRating != null ? averageRating : 0.0);
        response.put("ratingCount", ratingCount != null ? ratingCount : 0);

        return ResponseEntity.ok(response);
    }

    private Long getUserIdFromUserDetails(UserDetails userDetails) {
        return 1L; // Placeholder - implement proper user ID extraction
    }
}
