package com.vgdb.search.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;


@Entity
@Table(name = "games")
@Getter
@Setter

public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private UUID id;

    private String title;
    private String genre;
    private String platform;
    private Double price;

    @Column(name = "release_year")
    private Integer releaseYear;
}
