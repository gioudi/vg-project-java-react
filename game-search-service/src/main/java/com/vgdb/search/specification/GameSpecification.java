package com.vgdb.search.specification;


import com.vgdb.search.model.entity.Game;
import org.springframework.data.jpa.domain.Specification;


public class GameSpecification {
    public static Specification<Game> hasGenre(String genre) {
        return (root, query, cb) ->
            genre == null ? cb.conjunction() : cb.equal(root.get("genre"), genre);
    }

      public static Specification<Game> hasPlatform(String platform) {
        return (root, query, cb) ->
            platform == null ? cb.conjunction() : cb.equal(root.get("platform"), platform);
    }
}
