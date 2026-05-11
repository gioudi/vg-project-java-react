package com.vgdb.search.service;

import java.util.List;
import com.vgdb.search.model.entity.Game;
import com.vgdb.search.dto.GameResponseDTO;


public interface GameSearchService {
    List<GameResponseDTO> searchGames(String genre, String platform);
}
