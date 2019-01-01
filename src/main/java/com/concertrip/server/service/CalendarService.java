package com.concertrip.server.service;

import com.concertrip.server.dao.ArtistsRepository;
import com.concertrip.server.dao.EventsRepository;
import com.concertrip.server.dao.GenreRepository;
import com.concertrip.server.domain.Artists;
import com.concertrip.server.dto.Subscribe;
import com.concertrip.server.mapper.SubscribeMapper;
import com.concertrip.server.model.CalendarTabReq;
import com.concertrip.server.model.CommonListReq;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created hyunjk on 2018-12-27.
 * Github : https://github.com/hyunjkluz
 */
@Slf4j
@Service("calendar")
public class CalendarService {
    private final SubscribeMapper subscribeMapper;
    private final GenreRepository genreRepository;
    private final ArtistsRepository artistsRepository;
    private final EventsRepository eventsRepository;
    private final SubscribeService subscribeService;

    public CalendarService(SubscribeMapper subscribeMapper, GenreRepository genreRepository, ArtistsRepository artistsRepository, EventsRepository eventsRepository, SubscribeService subscribeService) {
        this.subscribeMapper = subscribeMapper;
        this.genreRepository = genreRepository;
        this.artistsRepository = artistsRepository;
        this.eventsRepository = eventsRepository;
        this.subscribeService = subscribeService;
    }


    public DefaultRes getCalendarTab(Integer userIdx) {
        try {
            List<Subscribe> subscribeList = subscribeMapper.getUserAllSubscribe(userIdx);
            List<CalendarTabReq> calendarTabReqList = new ArrayList<>();
            for (Subscribe subscribe : subscribeList) {
                String type = subscribe.getType();
                if (type.equals("genre")) {
                    CalendarTabReq calendarTabReq = new CalendarTabReq();
                    String id = subscribe.getObjIdx();
                    calendarTabReq.set_id(id);
                    calendarTabReq.setType(type);
                    calendarTabReq.setName(genreRepository.findGenreBy_idEquals(id).getCode());
                    calendarTabReqList.add(calendarTabReq);
                } else if (type.equals("artist")) {
                    CalendarTabReq calendarTabReq = new CalendarTabReq();
                    String id = subscribe.getObjIdx();
                    calendarTabReq.set_id(id);
                    calendarTabReq.setType(type);
                    calendarTabReq.setName(artistsRepository.findArtistDetailById(id).getName());
                    calendarTabReqList.add(calendarTabReq);
                }
            }
            return DefaultRes.res(StatusCode.OK, ResponseMessage.TEST_OK, calendarTabReqList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.TEST_FAIL);
        }
    }

    public DefaultRes getArtistCalendar(Integer userIdx, String id, Integer year, Integer month) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            String startDateString = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(1);
            Date startDate = format.parse(startDateString);

            String endDateString = Integer.toString(year) + "-" + Integer.toString(month + 1) + "-" + Integer.toString(0);
            Date endDate = format.parse(endDateString);

            Artists artists = artistsRepository.findArtistsBy_id(id);

            List<CommonListReq> artistCalendar = eventsRepository.findEventForCalendar(artists.getName(), startDate, endDate);

            for (CommonListReq cReq : artistCalendar) {
                cReq.setSubscribe(subscribeService.isSubscribe(userIdx, "artist", cReq.get_id()));
            }

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ARTIST_CALENDAR, artistCalendar);

        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.TEST_FAIL);
        }
    }
}
