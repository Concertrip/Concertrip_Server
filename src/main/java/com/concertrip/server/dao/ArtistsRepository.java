package com.concertrip.server.dao;

import com.concertrip.server.domain.Artists;
import com.concertrip.server.model.ArtistDetailReq;
import com.concertrip.server.model.CommonListReq;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by HYEON on 2018-12-26.
 */


public interface ArtistsRepository extends MongoRepository<Artists, String> {
    //아티스트 상세페이지
    @Query(value = "{ id : ?0 }")
    ArtistDetailReq findArtistDetailById(String _id);

    Artists findArtistsBy_id(String _id);

    CommonListReq findArtistsByName(String name);

    Artists insert(Artists artists);

    List<Artists> findAll();

    @Query(value = "{ filter : { $regex : ?0 } }", fields = "{ 'name' : 1, 'profileImg' : 1, 'filter' : 1 }")
    List<CommonListReq> findByFilter(String tag);

    @Query(value = "{ name : ?0 }", fields = "{ 'name' : 1, 'profileImg' : 1}")
    List<CommonListReq> findArtistListByName(String name);

    @Query(value = "{ name : ?0 }", fields = "{ 'name' : 1, 'profileImg' : 1 }")
    CommonListReq getArtistInfo(String name);

    @Query(value = "{ id : ?0 }", fields = "{ 'name' : 1, 'profileImg' : 1, 'filter' : 1 }")
    CommonListReq findArtistById(String _id);

}
