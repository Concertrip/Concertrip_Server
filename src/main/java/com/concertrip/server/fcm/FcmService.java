package com.concertrip.server.fcm;

import com.concertrip.server.dao.ArtistsRepository;
import com.concertrip.server.dao.EventsRepository;
import com.concertrip.server.dao.GenreRepository;
import com.concertrip.server.domain.Artists;
import com.concertrip.server.domain.Events;
import com.concertrip.server.domain.Genre;
import com.concertrip.server.dto.Notice;
import com.concertrip.server.dto.Subscribe;
import com.concertrip.server.mapper.NoticeMapper;
import com.concertrip.server.mapper.SubscribeMapper;
import com.concertrip.server.mapper.UserMapper;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.model.FcmReq;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Created by HYEON on 2019-01-05.
 */

@Service
@Slf4j
public class FcmService {
    private static final String FIREBASE_SERVER_KEY = "key=AAAAtk92IRQ:APA91bFKG-STIVRioOlzJdtbMVax8hwFGSpvkhtVHbWUSoNvv6Wc4Gf0V2pR4e1jHCf9H7x9qSgdi9Yqag9SHbxXTUlFZaADUtG8OzHSSZTA3mRDwwcn_aLuvAZvXDs_P9vVf80UyEb_";
    private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";

    private final UserMapper userMapper;
    private final SubscribeMapper subscribeMapper;
    private final NoticeMapper noticeMapper;
    private final ArtistsRepository artistsRepository;
    private final EventsRepository eventsRepository;
    private final GenreRepository genreRepository;

    public FcmService(UserMapper userMapper, SubscribeMapper subscribeMapper, NoticeMapper noticeMapper, ArtistsRepository artistsRepository, EventsRepository eventsRepository, GenreRepository genreRepository) {
        this.userMapper = userMapper;
        this.subscribeMapper = subscribeMapper;
        this.noticeMapper = noticeMapper;
        this.artistsRepository = artistsRepository;
        this.eventsRepository = eventsRepository;
        this.genreRepository = genreRepository;
    }


    public DefaultRes sendsave(final FcmReq fcmReq) {
        try {
            // get fcm token list by user subscibe
            List<Subscribe> subscribeList = subscribeMapper.getSubscribeTypeObj(fcmReq.getType(), fcmReq.getObjIdx());
            log.info(subscribeList.toString());
            if (subscribeList.size() == 0) return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NO_SUBSCRIBE);
            else {
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", FIREBASE_SERVER_KEY);
                headers.set("Content-Type", "application/json");
                Date today = new Date();
                //get noticeImg
                Notice notice = new Notice();
                if (fcmReq.getType().equals("artist")) {
                    Artists artists = artistsRepository.findArtistsBy_id(fcmReq.getObjIdx());
                    log.info(artists.toString());
                    notice.setNoticeImg(artists.getProfileImg());
                } else if (fcmReq.getType().equals("event")) {
                    Events events = eventsRepository.findEventsBy_id(fcmReq.getObjIdx());
                    log.info(events.toString());
                    notice.setNoticeImg(events.getProfileImg());
                } else {
                    Genre genre = genreRepository.findBy_idEquals(fcmReq.getObjIdx());
                    notice.setNoticeImg(genre.getProfileImg());
                }
                // send
                Map<String, String> notification = new HashMap<>();
                notification.put("title", fcmReq.getTitle());
                notification.put("body", fcmReq.getBody());

                for (Subscribe subscribe : subscribeList) {
                    int userIdx = subscribe.getUserIdx();
                    String token = userMapper.findUserFcmToken(userIdx);
                    log.info(token);
                    Fcm fcm = new Fcm();
                    fcm.setTo(token);
                    fcm.setNotification(notification);


                    HttpEntity<Fcm> request = new HttpEntity<Fcm>(fcm, headers);
                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.postForObject(FIREBASE_API_URL, request, String.class);
                    //save
                    notice.setUserIdx(userIdx);
                    notice.setTitle(fcmReq.getTitle());
                    notice.setBody(fcmReq.getBody());
                    notice.setCreatedAt(today);

                    noticeMapper.save(notice);
                    log.info(notice.toString());
                }
            }
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_NOTICE, subscribeList);
        } catch (Exception e) {
            //Rollback
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}
