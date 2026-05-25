package com.vgdb.inventory.service.impl;

import com.vgdb.inventory.model.dto.GameRequestDTO;
import com.vgdb.inventory.model.entity.Game;
import com.vgdb.inventory.repository.GameRepository;
import com.vgdb.inventory.service.GameService;
import com.vgdb.inventory.service.SearchClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import java.util.List;


@Service
public class GameServiceImpl implements GameService {
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private SearchClient searchClient;

    @Override
    @Transactional
    public Game createGame(GameRequestDTO request) {
        Game game = Game.builder()
        .title(request.getTitle())
        .genre(request.getGenre())
        .platform(request.getPlatform())
        .price(request.getPrice())
        .releaseYear(request.getReleaseYear())
        .build();

        Game savedGame = gameRepository.save(game);

        try {
             searchClient.syncWithSearch(savedGame);

        } catch (Exception e) {
           System.err.println("Warning: Game saved");
        }
        return savedGame; 
    }

    @Override
    @Transactional
    public Game updateGame(UUID id, GameRequestDTO request) {

        Game existingGame  = gameRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Game not found with id: " + id));    

        existingGame.setTitle(request.getTitle());
        existingGame.setGenre(request.getGenre());
        existingGame.setPlatform(request.getPlatform());
        existingGame.setPrice(request.getPrice());
        existingGame.setReleaseYear(request.getReleaseYear());


        return gameRepository.save(existingGame);
    }


    @Override
    @Transactional
    public void deleteGame(UUID id) {

        if(!gameRepository.existsById(id)){
            throw new RuntimeException("Cannot delete: Game not found with id " + id);
        }
        gameRepository.deleteById(id);
    }

    
    @Override
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }
}
