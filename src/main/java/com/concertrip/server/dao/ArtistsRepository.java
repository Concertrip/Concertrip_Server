package com.concertrip.server.dao;

import com.concertrip.server.domain.Artists;
import com.concertrip.server.model.ArtistsReq;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by HYEON on 2018-12-26.
 */


public interface ArtistsRepository extends MongoRepository<Artists, Integer> {
    List<Artists> findAll();

    //DB 접근 쿼리 방식 ver 2 by hj
    @Query(value = "{ name : ?0 }", fields = "{ 'name' : 1, 'profileImg' : 1, 'tag' : 1 }")
    List<ArtistsReq> findByName(String name);

    @Query(value = "{ tag : { $regex : ?0 } }", fields = "{ 'name' : 1, 'profileImg' : 1, 'tag' : 1 }")
    List<ArtistsReq> findByTag(String tag);
}
