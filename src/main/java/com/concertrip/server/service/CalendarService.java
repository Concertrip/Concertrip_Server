package com.concertrip.server.service;

import com.concertrip.server.dal.ArtistsDAL;
import com.concertrip.server.dal.EventsDAL;
import com.concertrip.server.dao.ArtistsRepository;
import com.concertrip.server.domain.Artists;
import com.concertrip.server.mapper.ArtistsSubscribeMapper;
import com.concertrip.server.mapper.EventsSubscribeMapper;
import com.concertrip.server.model.*;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private final ArtistsRepository artistsRepository;
    private final ArtistsSubscribeMapper artistsSubscribeMapper;
    private final EventsDAL eventsDAL;
    private final EventsSubscribeMapper eventsSubscribeMapper;

    public CalendarService(ArtistsDAL artistsDAL, ArtistsRepository artistsRepository, ArtistsSubscribeMapper artistsSubscribeMapper, EventsDAL eventsDAL, EventsSubscribeMapper eventsSubscribeMapper) {
        this.artistsDAL = artistsDAL;
        this.artistsRepository = artistsRepository;
        this.artistsSubscribeMapper = artistsSubscribeMapper;
        this.eventsDAL = eventsDAL;
        this.eventsSubscribeMapper = eventsSubscribeMapper;
    }


    /**
     * 사용자의 전체 캘린더 보여주는 서비스
     *
     * @param userIdx
     * @return
     */
    public DefaultRes getTotalCalendar (String userIdx) {
        try {
            CalendarReq totalCalendar = new CalendarReq();

            //사용자가 구독한 이벤트 정보 가져오기 + set
            totalCalendar.setEvents(getSubscribeEventCalendarM(userIdx));

            //사용자가 구독한 아티스트 정보 가져오기
            ArtistsSubscribeReq[] artistSub = artistsSubscribeMapper.findSubscribe(userIdx);
            List<ArtistsCalendarReq> artistsCalendarReqList = new LinkedList<>();

            for (int i = 0; i < artistSub.length; i++) {
                //구독한 아티스트의 이벤트 가져오기
                artistsCalendarReqList.add(getArtistsCalendarM(artistSub[i].getArtistsId()));
            }

            totalCalendar.setArtists(artistsCalendarReqList);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_TOTAL_CALENDAR, totalCalendar);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 사용자가 구독한 이벤트의 일정 가져오는 서비스
     *
     * @param userIdx
     * @return
     */
    public DefaultRes getEventsCalendar(String userIdx) {
        try {
            HashMap<String, List<EventsReq>> eventsCalendar = getSubscribeEventCalendarM(userIdx);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_EVENT_CALENDAR, eventsCalendar);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 특정 아티스트의 캘린더 가져오는 서비스
     *
     * @param _id
     * @return
     */
    public DefaultRes getArtistsCalendar (String _id) {
        try {
            ArtistsCalendarReq artistsCalendar = getArtistsCalendarM(_id);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ARTIST_CALENDAR, artistsCalendar);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }


    /**
     * 구독한 이벤트 캘린더 가져오는 메소드
     * @param userIdx
     * @return
     */
    public HashMap<String, List<EventsReq>> getSubscribeEventCalendarM(String userIdx) {
        try {
            EventsSubscribeReq[] eventSub = eventsSubscribeMapper.findSubscribe(userIdx);
            List<EventsReq> eventsReqs = new LinkedList<>(); //구독학 곤서트의 정보들

            //구독한 콘서트의 정보 가져옴
            for (int i = 0; i < eventSub.length; i++) {
                EventsReq eventsReq = eventsDAL.findEventsForCal(eventSub[i].getEventId());
                eventsReqs.add(eventsReq);
            }

            if (eventsReqs.size() == 0)
                return null;
            HashMap<String, List<EventsReq>> eventsCal = modelingEvents(eventsReqs);

            return eventsCal;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 특정 아티스트 캘린더 가져오는 메소드
     *
     * @param _id
     * @return
     */
    public ArtistsCalendarReq getArtistsCalendarM (String _id) {
        try {

            Artists artists = artistsDAL.findArtists(_id);
            ArtistsCalendarReq artistsCalendarReq = new ArtistsCalendarReq(artists.get_id(), artists.getName());

            List<EventsReq> eventList = eventsDAL.findByTitle(artistsCalendarReq.getName());

            if (eventList.size() == 0)
                return null;

            artistsCalendarReq.setEventsList(modelingEvents(eventList));

            return artistsCalendarReq;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    //캘린더 모델링
    public HashMap<String, List<EventsReq>> modelingEvents(List<EventsReq> eventList) {
        HashMap<String, List<EventsReq>> eventsCal = new HashMap<>();

        for (EventsReq eq : eventList) {
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

        return eventsCal;
    }
}
