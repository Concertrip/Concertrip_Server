package com.concertrip.server.dao;

import com.concertrip.server.domain.Events;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created hyunjk on 2018-12-25.
 * Github : https://github.com/hyunjkluz
 */
//조건이 매우 단순한 경우 쉽게 데이터 베이스에 접근함
public interface EventsRepository extends MongoRepository<Events, Integer> {
    List<Events> findAll();
}
