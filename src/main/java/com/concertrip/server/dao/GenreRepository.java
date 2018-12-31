package com.concertrip.server.dao;

import com.concertrip.server.domain.Genre;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GenreRepository extends MongoRepository<Genre, Integer> {
    List<Genre> findAll();

    Genre save(Genre genre);

    Genre findBy_idEquals(String _id);

    Genre findGenreBy_idEquals(String _id);
}
