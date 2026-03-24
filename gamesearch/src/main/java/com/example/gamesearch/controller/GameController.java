package com.example.gamesearch.controller;

import com.example.gamesearch.entity.Game;
import com.example.gamesearch.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "*") // Cho phép gọi API từ frontend
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // GET /api/games - Lấy tất cả game
    @GetMapping
    public ResponseEntity<List<Game>> getAllGames() {
        List<Game> games = gameService.getAllGames();
        return ResponseEntity.ok(games);
    }

    // GET /api/games/{id} - Lấy game theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable Long id) {
        Optional<Game> game = gameService.getGameById(id);
        return game.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/games/search?name=xxx - Tìm game theo tên
    @GetMapping("/search")
    public ResponseEntity<List<Game>> searchGames(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String genre) {
        List<Game> games = gameService.searchGames(name, genre);
        return ResponseEntity.ok(games);
    }

    // GET /api/games/genre/{genre} - Lọc game theo thể loại
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Game>> getGamesByGenre(@PathVariable String genre) {
        List<Game> games = gameService.filterByGenre(genre);
        return ResponseEntity.ok(games);
    }

    // GET /api/games/genres - Lấy danh sách tất cả thể loại
    @GetMapping("/genres")
    public ResponseEntity<List<String>> getAllGenres() {
        List<String> genres = gameService.getAllGenres();
        return ResponseEntity.ok(genres);
    }

    // GET /api/games/top - Lấy game sắp xếp theo lượt chơi
    @GetMapping("/top")
    public ResponseEntity<List<Game>> getTopGames() {
        List<Game> games = gameService.getGamesSortedByPlayCount();
        return ResponseEntity.ok(games);
    }

    // POST /api/games - Thêm game mới
    @PostMapping
    public ResponseEntity<Game> addGame(@RequestBody Game game) {
        Game savedGame = gameService.addGame(game);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGame);
    }

    // PUT /api/games/{id} - Cập nhật game
    @PutMapping("/{id}")
    public ResponseEntity<Game> updateGame(@PathVariable Long id, @RequestBody Game game) {
        Game updatedGame = gameService.updateGame(id, game);
        if (updatedGame != null) {
            return ResponseEntity.ok(updatedGame);
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE /api/games/{id} - Xóa game
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteGame(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        if (gameService.deleteGame(id)) {
            response.put("message", "Game deleted successfully");
            return ResponseEntity.ok(response);
        }
        response.put("message", "Game not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // POST /api/games/{id}/play - Tăng lượt chơi
    @PostMapping("/{id}/play")
    public ResponseEntity<Game> playGame(@PathVariable Long id) {
        Game game = gameService.incrementPlayCount(id);
        if (game != null) {
            return ResponseEntity.ok(game);
        }
        return ResponseEntity.notFound().build();
    }
}
