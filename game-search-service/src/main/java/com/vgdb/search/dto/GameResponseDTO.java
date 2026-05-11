package com.vgdb.search.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameResponseDTO {
    private UUID id;
    private String title;
    private String genre;
    private String platform;
    private Double price;
    private Integer releaseYear;
}