package com.vgdb.inventory.model.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameRequestDTO {
    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Genre is mandatory")
    private String genre;

    private String platform;

    @Positive(message = "Price must be positive")
    private Double price;

    private Integer releaseYear;
}
