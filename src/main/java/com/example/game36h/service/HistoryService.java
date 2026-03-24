package com.example.game36h.service;

import com.example.game36h.entity.History;
import com.example.game36h.entity.Game;
import com.example.game36h.entity.User;
import com.example.game36h.repository.HistoryRepository;
import com.example.game36h.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private GameRepository gameRepository;

    public History addToHistory(Long gameId, Long userId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        History history = new History();
        
        User user = new User();
        user.setId(userId);
        history.setUser(user);
        
        history.setGame(game);
        
        return historyRepository.save(history);
    }

    public Page<History> getUserHistory(Long userId, Pageable pageable) {
        return historyRepository.findByUserIdOrderByPlayedAtDesc(userId, pageable);
    }
}
