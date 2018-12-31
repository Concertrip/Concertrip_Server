package com.concertrip.server.dao;

import com.concertrip.server.domain.Genre;
import com.concertrip.server.model.CommonListReq;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface GenreRepository extends MongoRepository<Genre, String> {
    List<Genre> findAll();

    Genre save(Genre genre);

    Genre findBy_idEquals(String _id);

    @Query(value = "{ filter : { $regex : ?0 } }", fields = "{ 'name' : 1, 'profileImg' : 1 }")
    List<CommonListReq> findByFilter(String name);

    @Query(value = "{ id : ?0 } ", fields = "{ 'name' : 1, 'profileImg' : 1, 'filter' : 1 }")
    CommonListReq findGenreById(String _id);

    Genre findGenreBy_idEquals(String _id);
}
