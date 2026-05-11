package com.vgdb.inventory.repository;

import com.vgdb.inventory.model.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;


@Repository

public interface GameRepository extends JpaRepository<Game, UUID> {

    
}
