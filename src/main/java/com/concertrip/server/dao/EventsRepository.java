package com.concertrip.server.dao;

import com.concertrip.server.domain.Events;
import com.concertrip.server.model.DefaultReq;
import com.concertrip.server.model.EventsDetailReq;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created hyunjk on 2018-12-25.
 * Github : https://github.com/hyunjkluz
 */
//조건이 매우 단순한 경우 쉽게 데이터 베이스에 접근함
public interface EventsRepository extends MongoRepository<Events, Integer> {
    List<Events> findAll();

 //   @Query(value = "{ name : ?0 }", fields = "{ 'name' : 1, 'tag' : 1 }")
 //  List<ArtistsReq> findByName(String name);

 //   @Query(value = "{ tag : { $regex : ?0 } }", fields = "{ 'name' : 1, 'tag' : 1 }")
  //  List<ArtistsReq> findByTag(String tag);

  //  @Query(value = "{ title : ?0}", fields = "{")
   // List<EventsReq> findByTitle(String title)


    @Query(value = "{ _id : ?0 }", fields = "{ 'precaution' : 1, '_id' : 0 }")
    DefaultReq getPrecaution(String _id);

    @Query(value = "{ _id : ?0 } ")
    EventsDetailReq findEvent(String _id);


}


