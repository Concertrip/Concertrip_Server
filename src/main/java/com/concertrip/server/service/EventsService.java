package com.concertrip.server.service;

import com.concertrip.server.dal.EventsDAL;
import com.concertrip.server.domain.Events;
import com.concertrip.server.mapper.EventsMapper;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import netscape.javascript.JSObject;
import org.json.simple.JSONObject;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * Created hyunjk on 2018-12-25.
 * Github : https://github.com/hyunjkluz
 */
@Slf4j
@Service
public class EventsService {
    private final EventsDAL eventsDal;
    private final EventsMapper eventsMapper;

    public EventsService(EventsDAL eventsDal, EventsMapper eventsMapper) {
        this.eventsDal = eventsDal;
        this.eventsMapper = eventsMapper;
    }


    /**
     * 전체 이벤트 정보 가져오기
     *
     * @return DefaultRes
     */
    public DefaultRes selectAll() {
        try {
            List<Events> eventsList = eventsDal.selectAll();
            if (eventsList.size() == 0) {
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_EVENT);
            } else {
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_EVENTS, eventsList);
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    public DefaultRes findEventsById(String _id) {
        try {
            Events events = eventsDal.findEvents(_id);

            if (events == null) {
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_EVENT);
            } else {
                for (int i = 0; i < events.getCast().length; i++) {
                    JSONObject cast = new JSONObject();


                }

                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_EVENTS, events);
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }


    /**
     * 이벤트 추가
     *
     * @param events 이벤트 데이터
     * @return DafaultRes
     */
    public DefaultRes insert(Events events) {
        try {
            eventsDal.insert(events);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_EVENT);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 이벤트 내용 수정
     *
     * @param events 수정된 이벤트 객체
     * @return
     */
    public DefaultRes update(Events events) {
        try {
            eventsDal.update(events);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_EVENT);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 이벤트 삭제
     *
     * @param _id 이벤트 객체의 id
     * @return
     */
    public DefaultRes delete(String _id) {
        try {
            eventsDal.delete(_id);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_EVENT);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    public DefaultRes subscribe(final String eventIdx, final String token) {
        try {
            eventsMapper.subscribe(eventIdx, token);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.SUBSCRIBE_EVENT);
        } catch (Exception e) {
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}
