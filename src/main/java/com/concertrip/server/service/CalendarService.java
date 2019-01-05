package com.concertrip.server.service;

import com.concertrip.server.dao.ArtistsRepository;
import com.concertrip.server.dao.EventsRepository;
import com.concertrip.server.dao.GenreRepository;
import com.concertrip.server.domain.Artists;
import com.concertrip.server.domain.Genre;
import com.concertrip.server.dto.Subscribe;
import com.concertrip.server.mapper.SubscribeMapper;
import com.concertrip.server.model.CalendarReq;
import com.concertrip.server.model.CalendarTabReq;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
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
            CalendarTabReq all = new CalendarTabReq();
            all.set_id("all");
            all.setType("all");
            all.setName("모두");
            calendarTabReqList.add(all);
            CalendarTabReq mvp = new CalendarTabReq();
            mvp.set_id("mvp");
            mvp.setType("mvp");
            mvp.setName("내 공연");
            calendarTabReqList.add(mvp);
            for (Subscribe subscribe : subscribeList) {
                String type = subscribe.getType();
                if (type.equals("genre")) {
                    CalendarTabReq calendarTabReq = new CalendarTabReq();
                    String id = subscribe.getObjIdx();
                    calendarTabReq.set_id(id);
                    calendarTabReq.setType(type);
                    String code = genreRepository.findGenreBy_idEquals(id).getCode();
                    calendarTabReq.setName(code);
                    calendarTabReqList.add(calendarTabReq);
                } else if (type.equals("artist")) {
                    CalendarTabReq calendarTabReq = new CalendarTabReq();
                    String id = subscribe.getObjIdx();
                    calendarTabReq.set_id(id);
                    calendarTabReq.setType(type);
                    String name = artistsRepository.findArtistDetailById(id).getName();
                    calendarTabReq.setName(name);
                    calendarTabReqList.add(calendarTabReq);
                }
            }
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_SUCCESS, calendarTabReqList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.TEST_FAIL);
        }
    }

    public DefaultRes getAllCalendar(Integer userIdx, Integer year, Integer month, Integer day) {
        try {
            Date[] standardDate = new Date[] {null, null};
            if (day == -1) {
                standardDate = translateDate(year, month, 1, "month");
            } else {
                standardDate =translateDate(year, month, day, "day");
            }

            //구독한 이벤트 불러오기
            List<Subscribe> subscribeList = subscribeMapper.getUserAllSubscribe(userIdx);
            List<CalendarReq> allCalendar = new LinkedList<>();
            CalendarReq calendarReq = new CalendarReq();


            for (Subscribe s : subscribeList) {
                if (s.getType().equals("event")) {
                    calendarReq = eventsRepository.findEventForEventCalendar(s.getObjIdx(), standardDate[0], standardDate[1]);
                    log.info(calendarReq.getName());
                    calendarReq.setTabId("mvp");

                    if (calendarReq == null) {
                        continue;
                    }
                    calendarReq.setSubscribe(subscribeService.isSubscribe(userIdx, "event", calendarReq.get_id()));
                    allCalendar.add(calendarReq);
                } else if (s.getType().equals("artist")) {
                    Artists artists = artistsRepository.findArtistsBy_id(s.getObjIdx());
                    log.info(artists.getName());
                    List<CalendarReq> artistCalendar  = eventsRepository.findEventForArtistCalendar(artists.getName(), standardDate[0], standardDate[1]);

                    for (CalendarReq cReq : artistCalendar) {
                        if (calendarReq == null) {
                            continue;
                        }
                        cReq.setTabId(artists.getName());
                        cReq.setSubscribe(subscribeService.isSubscribe(userIdx, "event", cReq.get_id()));
                        allCalendar.add(cReq);
                    }
                } else {
                    Genre genre = genreRepository.findGenreBy_idEquals(s.getObjIdx());
                    List<CalendarReq> genreCalendar = eventsRepository.findEventForGenreCalendar(genre.getCode(), standardDate[0], standardDate[1]);

                    for (CalendarReq cReq : genreCalendar) {
                        if (calendarReq == null) {
                            continue;
                        }
                        cReq.setTabId(genre.getCode());
                        cReq.setSubscribe(subscribeService.isSubscribe(userIdx, "evnet", cReq.get_id()));
                        allCalendar.add(cReq);
                    }
                }
            }

            List<CalendarReq> allCalendarDuplicate = new LinkedList<>();

            for (CalendarReq cq : allCalendar) {
                if (!allCalendarDuplicate.contains(cq))
                    allCalendarDuplicate.add(cq);
            }

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ALL_CALENDAR, allCalendarDuplicate);

        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.TEST_FAIL);
        }

    }

    public DefaultRes getMvpCalendar(Integer userIdx, Integer year, Integer month, Integer day) {
        try {
            Date[] standardDate = new Date[] {null, null};
            if (day == -1) {
                standardDate = translateDate(year, month, 1, "month");
            } else {
                standardDate =translateDate(year, month, day, "day");
            }

            List<Subscribe> eventSubscribeList = subscribeMapper.getUserSubscribe(userIdx, "event");
            List<CalendarReq> eventCalendar = new LinkedList<>();

            for (Subscribe s : eventSubscribeList) {
                CalendarReq calendarReq = eventsRepository.findEventForEventCalendar(s.getObjIdx(), standardDate[0], standardDate[1]);
                if (ObjectUtils.isEmpty(calendarReq)) {
                    continue;
                }
                eventCalendar.add(calendarReq);
            }

            for (CalendarReq cReq : eventCalendar) {
                if (cReq == null) {
                    continue;
                }
                cReq.setSubscribe(subscribeService.isSubscribe(userIdx, "event", cReq.get_id()));
            }

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_EVENT_CALENDAR, eventCalendar);

        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.TEST_FAIL);
        }
    }

    public DefaultRes getArtistCalendar(Integer userIdx, String id, Integer year, Integer month, Integer day) {
        try {
            Date[] standardDate = new Date[] {null, null};
            if (day == -1) {
                standardDate = translateDate(year, month, 1, "month");
            } else {
                standardDate =translateDate(year, month, day, "day");
            }

            Artists artists = artistsRepository.findArtistsBy_id(id);

            List<CalendarReq> artistCalendar = eventsRepository.findEventForArtistCalendar(artists.getName(), standardDate[0], standardDate[1]);


            for (CalendarReq cReq : artistCalendar) {
                if (cReq == null)    continue;
                log.info(cReq.getName());
                cReq.setSubscribe(subscribeService.isSubscribe(userIdx, "event", cReq.get_id()));
            }

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ARTIST_CALENDAR, artistCalendar);

        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.TEST_FAIL);
        }
    }

    public DefaultRes getGenreCalendar(Integer userIdx, String id, Integer year, Integer month, Integer day) {
        try {
            Date[] standardDate = new Date[] {null, null};
            if (day == -1) {
                standardDate = translateDate(year, month, 1, "month");
            } else {
                standardDate =translateDate(year, month, day, "day");
            }

            Genre genre = genreRepository.findGenreBy_idEquals(id);

            List<CalendarReq> genreCalendar = eventsRepository.findEventForGenreCalendar(genre.getCode(), standardDate[0], standardDate[1]);

            for (CalendarReq cReq : genreCalendar) {
                if (cReq == null)    continue;
                cReq.setSubscribe(subscribeService.isSubscribe(userIdx, "event", cReq.get_id()));
            }

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_GENRE_CALENDAR, genreCalendar);

        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.TEST_FAIL);
        }
    }

    public Date[] translateDate(Integer year, Integer month, Integer day, String type) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String startDateString = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day) + " 00:00:00";
            Date startDate = format.parse(startDateString);

            if (type.equals("month")) {
                month += 1;
                day = 0;
            }

            String endDateString = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day) + " 23:59:59";
            Date endDate = format.parse(endDateString);

            return new Date[]{startDate, endDate};
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
