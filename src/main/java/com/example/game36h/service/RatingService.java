package com.example.game36h.service;

import com.example.game36h.dto.RatingRequest;
import com.example.game36h.entity.Rating;
import com.example.game36h.entity.Game;
import com.example.game36h.entity.User;
import com.example.game36h.repository.RatingRepository;
import com.example.game36h.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private GameRepository gameRepository;

    public Rating createRating(RatingRequest request, Long userId) {
        // Validate game exists
        Game game = gameRepository.findById(request.getGameId())
                .orElseThrow(() -> new RuntimeException("Game not found"));

        // Check if user already rated this game
        Optional<Rating> existingRating = ratingRepository.findByUserIdAndGameId(userId, request.getGameId());
        if (existingRating.isPresent()) {
            // Update existing rating
            Rating rating = existingRating.get();
            rating.setScore(request.getScore());
            return ratingRepository.save(rating);
        } else {
            // Create new rating
            Rating rating = new Rating();
            rating.setScore(request.getScore());

            User user = new User();
            user.setId(userId);
            rating.setUser(user);

            rating.setGame(game);

            return ratingRepository.save(rating);
        }
    }

    public List<Rating> getRatingsByGameId(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));
        return ratingRepository.findAll();
    }

    public Double getAverageRating(Long gameId) {
        return ratingRepository.getAverageRatingByGameId(gameId);
    }

    public Long getRatingCount(Long gameId) {
        return ratingRepository.getRatingCountByGameId(gameId);
    }
}
