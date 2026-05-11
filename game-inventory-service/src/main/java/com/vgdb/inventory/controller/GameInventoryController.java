package com.vgdb.inventory.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.vgdb.inventory.model.dto.GameRequestDTO;
import com.vgdb.inventory.model.dto.GameResponseDTO;
import com.vgdb.inventory.service.GameService;

import com.vgdb.inventory.model.entity.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/inventory")

public class GameInventoryController {

    @Autowired
    private GameService gameService;

    @PostMapping
    public ResponseEntity<GameResponseDTO> create(@Valid @RequestBody GameRequestDTO request) {
        Game game = gameService.createGame(request);
        return new ResponseEntity<>(convertToDTO(game), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody GameRequestDTO request) {
        Game game = gameService.updateGame(id, request);
        return ResponseEntity.ok(convertToDTO(game));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<GameResponseDTO>> getAll(){
        List<GameResponseDTO> list = gameService.getAllGames().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    private GameResponseDTO convertToDTO(Game game) {
        GameResponseDTO dto = new GameResponseDTO();

        dto.setId(game.getId());
        dto.setTitle(game.getTitle());
        dto.setGenre(game.getGenre());
        dto.setPlatform(game.getPlatform());
        dto.setPrice(game.getPrice());
        dto.setReleaseYear(game.getReleaseYear());
        return dto;
    }

}