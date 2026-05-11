package com.vgdb.inventory.model.dto;

import lombok.Data;
import java.util.UUID;


@Data
public class GameResponseDTO {
    private UUID id;
    private String title;
    private String genre;
    private String platform;
    private Double price;
    private Integer releaseYear;
}
