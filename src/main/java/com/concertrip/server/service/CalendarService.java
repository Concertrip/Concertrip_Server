package com.concertrip.server.service;

import com.concertrip.server.dal.ArtistsDAL;
import com.concertrip.server.dal.EventsDAL;
import com.concertrip.server.mapper.EventsSubscribeMapper;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.model.EventsReq;
import com.concertrip.server.model.EventsSubscribeReq;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created hyunjk on 2018-12-27.
 * Github : https://github.com/hyunjkluz
 */
@Slf4j
@Service("calendar")
public class CalendarService {
    private final ArtistsDAL artistsDAL;
    private final EventsDAL eventsDAL;
    private final EventsSubscribeMapper eventsSubscribeMapper;

    public CalendarService(ArtistsDAL artistsDAL, EventsDAL eventsDAL, EventsSubscribeMapper eventsSubscribeMapper) {
        this.artistsDAL = artistsDAL;
        this.eventsDAL = eventsDAL;
        this.eventsSubscribeMapper = eventsSubscribeMapper;
    }

    public DefaultRes findSubscribeEventCalendar(String userIdx) {
        try {
            EventsSubscribeReq[] eventSub = eventsSubscribeMapper.findSubscribe(userIdx);
            EventsReq[] eventsReqs = new EventsReq[eventSub.length];    //구독한 곤서트의 정보들

            //구독한 콘서트의 정보 가져옴
            for (int i = 0; i < eventSub.length; i++) {
                EventsReq eventsReq = eventsDAL.findEventsForCal(eventSub[i].getEventId());
                eventsReqs[i] = eventsReq;
            }

            if (eventsReqs.length == 0)
                return new DefaultRes(StatusCode.NO_CONTENT , ResponseMessage.NO_CALENDAR);

            HashMap<String, List<EventsReq>> eventsCal = new HashMap<>();

            for (EventsReq eq : eventsReqs) {
                for (int i = 0; i < eq.getDate().size(); i++) {
                    List<EventsReq> eventListTmp;
                    Date eventDate = eq.getDate().get(i);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");

                    String date = format.format(eventDate);

                    if (eventsCal.containsKey(date)) {      //해당 날짜인 키값이 있으면
                        eventListTmp = eventsCal.get(date); //원래 있던 리스트 가져와서
                    } else {                                //키 값이 없으면
                        eventListTmp = new LinkedList<>();  //리스트 새로 만들어서
                    }
                    eventListTmp.add(eq);               //뒤에다가 추가하기
                    eventsCal.put(date, eventListTmp);
                }
            }
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_EVENT_CALENDAR, eventsCal);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }




}
