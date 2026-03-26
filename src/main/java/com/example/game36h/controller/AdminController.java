package com.example.game36h.controller;

import com.example.game36h.dto.GameResponse;
import com.example.game36h.entity.Game;
import com.example.game36h.service.GameService;
import com.example.game36h.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameService gameService;

    // Lấy danh sách game chờ duyệt
    @GetMapping("/games/pending")
    public ResponseEntity<Page<Game>> getPendingGames(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Game> games = gameRepository.findByStatus(Game.GameStatus.PENDING, pageable);
        return ResponseEntity.ok(games);
    }

    // Duyệt game
    @PutMapping("/games/{gameId}/approve")
    public ResponseEntity<GameResponse> approveGame(@PathVariable Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));
        game.setStatus(Game.GameStatus.APPROVED);
        gameRepository.save(game);
        return ResponseEntity.ok(gameService.getGameById(gameId));
    }

    // Từ chối game
    @PutMapping("/games/{gameId}/reject")
    public ResponseEntity<GameResponse> rejectGame(@PathVariable Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));
        game.setStatus(Game.GameStatus.REJECTED);
        gameRepository.save(game);
        return ResponseEntity.ok(gameService.getGameById(gameId));
    }

    // Lấy tất cả game (không phân biệt status)
    @GetMapping("/games")
    public ResponseEntity<Page<Game>> getAllGames(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Game> games = gameRepository.findAll(pageable);
        return ResponseEntity.ok(games);
    }
}
