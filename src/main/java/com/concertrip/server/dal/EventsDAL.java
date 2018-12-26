package com.concertrip.server.dal;

import com.concertrip.server.dao.EventsRepository;
import com.concertrip.server.domain.Events;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created hyunjk on 2018-12-26.
 * Github : https://github.com/hyunjkluz
 */
@Service
public class EventsDAL {
    private final EventsRepository eventsRepository;
    private final MongoTemplate mongoTemplate;

    /**
     * 생성자 의존성 주입
     * @param eventsRepository
     * @param mongoTemplate
     */
    public EventsDAL(EventsRepository eventsRepository, MongoTemplate mongoTemplate) {
        this.eventsRepository = eventsRepository;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 전체 이벤트 정보 가져오기
     *
     * @return DefaultRes
     */
    public List<Events> selectAll() {
        return eventsRepository.findAll();
    }


    public Events findEvents(String _id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(_id));
        Events events = mongoTemplate.findOne(query, Events.class);
        return events;
    }


    /**
     * 이벤트 추가
     * @param events
     */
    public void insert(Events events) {
        mongoTemplate.insert(events, "events");
    }

    /**
     * 이벤트 내용 수정
     *
     * @param events 수정된 이벤트 객체
     * @return
     */
    public void update(Events events) {
        mongoTemplate.save(events, "events");
    }

    /**
     * 이벤트 삭제
     *
     * @param _id 이벤트 객체의 id
     */
    public void delete(String _id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(_id));
        mongoTemplate.remove(query, "events");
    }
}
