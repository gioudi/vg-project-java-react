package com.vgdb.inventory.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.vgdb.inventory.model.entity.Game;





@FeignClient(name = "game-search-service")
public interface SearchClient {
    @PostMapping("/api/v1/search/sync")
    void syncWithSearch(@RequestBody Game game);
}
