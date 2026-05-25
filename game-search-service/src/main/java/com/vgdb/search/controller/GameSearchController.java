package com.vgdb.search.controller;

import com.vgdb.search.dto.GameResponseDTO;
import com.vgdb.search.service.GameSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/v1/search")
public class GameSearchController {
    @Autowired

    private GameSearchService gameSearchService;

    @GetMapping
    public ResponseEntity<List<GameResponseDTO>> searchGames(
        @RequestParam(required = false) String genre,
        @RequestParam(required = false) String platform
    ) {
        List<GameResponseDTO> results = gameSearchService.searchGames(genre, platform);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/sync")
    public ResponseEntity<Void> syncGame(@RequestBody GameResponseDTO game) {
       gameSearchService.saveGame(game);

       return ResponseEntity.ok().build();
    }

    @DeleteMapping("/sync/{id}")
    public ResponseEntity<Void> unsyncGame(@PathVariable UUID id) {
        gameSearchService.deleteGame(id);

        return ResponseEntity.ok().build();
    }
    
}
