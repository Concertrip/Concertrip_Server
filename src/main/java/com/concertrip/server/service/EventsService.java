package com.concertrip.server.service;

import com.concertrip.server.dal.ArtistsDAL;
import com.concertrip.server.dal.EventsDAL;
import com.concertrip.server.domain.Events;
import com.concertrip.server.mapper.EventsSubscribeMapper;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.model.EventsDetailReq;
import com.concertrip.server.model.EventsSubscribeReq;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
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
    private final EventsDAL eventsDAL;
    private final ArtistsDAL artistsDAL;
    private final EventsSubscribeMapper eventsSubscribeMapper;

    public EventsService(EventsDAL eventsDAL, ArtistsDAL artistsDAL, EventsSubscribeMapper eventsSubscribeMapper) {
        this.eventsDAL = eventsDAL;
        this.artistsDAL = artistsDAL;
        this.eventsSubscribeMapper = eventsSubscribeMapper;
    }


    /**
     * 전체 이벤트 정보 가져오기
     *
     * @return DefaultRes
     */
    public DefaultRes selectAll() {
        try {
            List<Events> eventsList = eventsDAL.selectAll();
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
            EventsDetailReq eventsDetail = eventsDAL.getEvents(_id);

            if (eventsDetail == null) {
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_EVENT);
            } else {
                String[] castImg = new String[eventsDetail.getCast().length];
                String[] casts = eventsDetail.getCast();

                for (int i = 0; i < casts.length; i++) {
                    String img = artistsDAL.getArtistsImgByName(casts[i]);
                    castImg[i] = img;
                }

                eventsDetail.setCastImg(castImg);

                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_EVENTS, eventsDetail);
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
            eventsDAL.insert(events);
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
            eventsDAL.update(events);
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
            eventsDAL.delete(_id);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_EVENT);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 이벤트 구독하기 및 취소
     *
     * @param eventId
     * @param token
     * @return
     */
    public DefaultRes subscribe(final String eventId, final String token) {
        try {
            EventsSubscribeReq esReq = eventsSubscribeMapper.isSubscribe(token, eventId);
            if (esReq == null) {
                eventsSubscribeMapper.subscribe(eventId, token);
            } else {
                eventsSubscribeMapper.unSubscribe(eventId, token);
            }
            return DefaultRes.res(StatusCode.OK, ResponseMessage.SUBSCRIBE_EVENT);
        } catch (Exception e) {
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}
