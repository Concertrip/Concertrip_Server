package com.concertrip.server.Service;

import com.concertrip.server.dal.EventsDAL;
import com.concertrip.server.domain.Events;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
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

    public EventsService(EventsDAL eventsDal) {
        this.eventsDal = eventsDal;
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

}
