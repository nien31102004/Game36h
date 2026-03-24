package com.example.game36h.controller;

import com.example.game36h.entity.History;
import com.example.game36h.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/history")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping
    public ResponseEntity<Page<History>> getUserHistory(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Long userId = getUserIdFromUserDetails(userDetails);
        Pageable pageable = PageRequest.of(page, size);
        Page<History> history = historyService.getUserHistory(userId, pageable);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/{gameId}")
    public ResponseEntity<History> addToHistory(
            @PathVariable Long gameId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        Long userId = getUserIdFromUserDetails(userDetails);
        History history = historyService.addToHistory(gameId, userId);
        return ResponseEntity.ok(history);
    }

    private Long getUserIdFromUserDetails(UserDetails userDetails) {
        return 1L; // Placeholder - implement proper user ID extraction
    }
}
