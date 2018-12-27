package com.concertrip.server.dao;

import com.concertrip.server.domain.Artists;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by HYEON on 2018-12-26.
 */


public interface ArtistsRepository extends MongoRepository<Artists, Integer> {
    List<Artists> findAll();

}
