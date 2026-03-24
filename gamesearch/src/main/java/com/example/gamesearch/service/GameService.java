package com.example.gamesearch.service;

import com.example.gamesearch.entity.Game;
import com.example.gamesearch.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    // Lấy tất cả game
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    // Lấy game theo ID
    public Optional<Game> getGameById(Long id) {
        return gameRepository.findById(id);
    }

    // Tìm game theo tên
    public List<Game> searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return gameRepository.findAll();
        }
        return gameRepository.findByNameContainingIgnoreCase(name.trim());
    }

    // Lọc game theo thể loại
    public List<Game> filterByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            return gameRepository.findAll();
        }
        return gameRepository.findByGenreIgnoreCase(genre.trim());
    }

    // Tìm kiếm kết hợp (theo tên và/hoặc thể loại)
    public List<Game> searchGames(String name, String genre) {
        // Xử lý tham số rỗng
        String searchName = (name != null && !name.trim().isEmpty()) ? name.trim() : null;
        String searchGenre = (genre != null && !genre.trim().isEmpty()) ? genre.trim() : null;

        return gameRepository.searchGames(searchName, searchGenre);
    }

    // Lấy game sắp xếp theo lượt chơi
    public List<Game> getGamesSortedByPlayCount() {
        return gameRepository.findAllByOrderByPlayCountDesc();
    }

    // Lấy danh sách các thể loại
    public List<String> getAllGenres() {
        return gameRepository.findAllGenres();
    }

    // Thêm game mới
    public Game addGame(Game game) {
        return gameRepository.save(game);
    }

    // Cập nhật game
    public Game updateGame(Long id, Game gameDetails) {
        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
            game.setName(gameDetails.getName());
            game.setGenre(gameDetails.getGenre());
            game.setPlayCount(gameDetails.getPlayCount());
            game.setDescription(gameDetails.getDescription());
            game.setImageUrl(gameDetails.getImageUrl());
            return gameRepository.save(game);
        }
        return null;
    }

    // Xóa game
    public boolean deleteGame(Long id) {
        if (gameRepository.existsById(id)) {
            gameRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Tăng lượt chơi
    public Game incrementPlayCount(Long id) {
        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
            game.setPlayCount(game.getPlayCount() + 1);
            return gameRepository.save(game);
        }
        return null;
    }
}
