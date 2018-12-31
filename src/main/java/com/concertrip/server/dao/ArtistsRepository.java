package com.concertrip.server.dao;

import com.concertrip.server.domain.Artists;
import com.concertrip.server.model.ArtistsReq;
import com.concertrip.server.model.CommonListReq;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by HYEON on 2018-12-26.
 */


public interface ArtistsRepository extends MongoRepository<Artists, String> {
    List<Artists> findAll();

    //DB 접근 쿼리 방식 ver 2 by hj
    @Query(value = "{ name : ?0 }", fields = "{ 'name' : 1, 'profileImg' : 1, 'filter' : 1 }")
    List<CommonListReq> findByName(String name);

    @Query(value = "{ filter : { $regex : ?0 } }", fields = "{ 'name' : 1, 'profileImg' : 1, 'filter' : 1 }")
    List<CommonListReq> findByFilter(String tag);

    @Query(value = "{ member : { $regex : ?0 } }", fields = "{ 'name' : 1, 'profileImg' : 1, 'filter' : 1 }")
    List<CommonListReq> findByMember(String name);

    @Query(value = "{ name : ?0 }", fields = "{ 'name' : 1, 'profileImg' : 1 }")
    CommonListReq getArtistInfo(String name);

    @Query(value = "{ _id : ?0 }", fields = "{ 'name' : 1, 'profileImg' : 1, 'filter' : 1 }")
    CommonListReq findArtistById(String _id);

    @Query(value = "{ name : ?0 }")
    Artists findOneByName(String name);
}
