package com.concertrip.server.dal;

import com.concertrip.server.domain.Artists;
import com.concertrip.server.domain.Events;
import com.concertrip.server.domain.Genre;
import jdk.nashorn.internal.objects.annotations.Where;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/**
 * Created hyunjk on 2019-01-11.
 * Github : https://github.com/hyunjkluz
 */
@Slf4j
@Service
public class SubscribeDAL {
    private final MongoTemplate mongoTemplate;

    public SubscribeDAL(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     *
     * @param type 어떤 구독하기 인지 : event, artist, genre
     * @param id
     * @param userIdx
     * @param isSubscribe 구독하기에서 오면 false, 구독 취소에서 오면 true
     */
    public void subscribe(String type, String id, Integer userIdx, boolean isSubscribe) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update();

        if (isSubscribe) {
            update.pull("subscriber", userIdx);
        } else {
            update.push("subscriber", userIdx);
        }

        if (type.equals("event")) {
            mongoTemplate.updateMulti(query, update, Events.class);
        } else if (type.equals("artist")) {
            mongoTemplate.updateMulti(query, update, Artists.class);
        } else {
            mongoTemplate.updateMulti(query, update, Genre.class);
        }
    }
}
