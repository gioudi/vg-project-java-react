package com.vgdb.inventory.service;

import com.vgdb.inventory.model.dto.GameRequestDTO;
import com.vgdb.inventory.model.entity.Game;
import java.util.UUID;
import java.util.List;



public interface GameService {
    Game createGame(GameRequestDTO request);
    Game updateGame(UUID id, GameRequestDTO request);
    void deleteGame(UUID id);
    List<Game> getAllGames();
}
