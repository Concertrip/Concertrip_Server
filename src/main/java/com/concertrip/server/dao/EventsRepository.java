package com.concertrip.server.dao;

import com.concertrip.server.domain.Events;
import com.concertrip.server.model.CommonListReq;
import com.concertrip.server.model.DefaultReq;
import com.concertrip.server.model.EventsDetailReq;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created hyunjk on 2018-12-25.
 * Github : https://github.com/hyunjkluz
 */
//조건이 매우 단순한 경우 쉽게 데이터 베이스에 접근함
public interface EventsRepository extends MongoRepository<Events, String> {
    List<Events> findAll();

    //DB 접근 쿼리 방식 ver 2 by hj
    @Query(value = "{ name : ?0 }", fields = "{ 'name' : 1, 'profileImg' : 1, 'filter' : 1 }")
    List<CommonListReq> findByName(String name);

    @Query(value = "{ filter : { $regex : ?0 } }", fields = "{ 'name' : 1, 'profileImg' : 1, 'filter' : 1 }")
    List<CommonListReq> findByFilter(String tag);

    @Query(value = "{ $and : [ { member : { $regex : ?0 } } , { date : { $elemMatch : { $gte :  ?1, $lte :  ?2} } } ] }",
            fields = "{ 'name' : 1, 'profileImg' : 1, 'filter' : 1 }")
    List<CommonListReq> findEventForCalendar(String name, Date startDate, Date endDate);

    @Query(value = "{ _id : ?0 }", fields = "{ 'precaution' : 1, 'id' : 0 }")
    DefaultReq getPrecaution(String _id);

    @Query(value = "{ _id : ?0 }", fields = "{ 'member' : 1, 'id' : 0 }")
    DefaultReq getMember(String _id);

    @Query(value = "{ _id : ?0 } ")
    EventsDetailReq findEvent(String _id);

    @Query(value = "{ _id : ?0 } ", fields = "{ 'name' : 1, 'profileImg' : 1, 'filter' : 1 }")
    CommonListReq findEventList(String _id);

    List<CommonListReq> findAllByFilterIn(String filter);



}


