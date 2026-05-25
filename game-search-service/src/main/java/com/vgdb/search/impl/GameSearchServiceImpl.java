package com.vgdb.search.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

import com.vgdb.search.dto.GameResponseDTO;
import com.vgdb.search.repository.GameRepository;
import com.vgdb.search.specification.GameSpecification;

import com.vgdb.search.model.entity.Game;
import com.vgdb.search.service.GameSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class GameSearchServiceImpl implements GameSearchService {
    @Autowired

    private GameRepository gameRepository;

    @Override
    public List<GameResponseDTO> searchGames(String genre, String platform) {

        Specification<Game> spec = Specification
                .where(GameSpecification.hasGenre(genre))
                .and(GameSpecification.hasPlatform(platform));

        List<Game> games = gameRepository.findAll(spec);

        return games.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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

    @Override
    public void saveGame(GameResponseDTO dto) {
        Game game = new Game();
        game.setId(dto.getId());
        game.setTitle(dto.getTitle());
        game.setGenre(dto.getGenre());
        game.setPlatform(dto.getPlatform());
        game.setPrice(dto.getPrice());
        game.setReleaseYear(dto.getReleaseYear());

        gameRepository.save(game);
    }

    @Override

    public void deleteGame(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("The given id must not be a null!");
        }
        gameRepository.deleteById(id);
    }
}
