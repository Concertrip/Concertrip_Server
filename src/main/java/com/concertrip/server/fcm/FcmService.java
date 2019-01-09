package com.concertrip.server.fcm;

import com.concertrip.server.dao.ArtistsRepository;
import com.concertrip.server.dto.Notice;
import com.concertrip.server.dto.Subscribe;
import com.concertrip.server.dto.User;
import com.concertrip.server.mapper.NoticeMapper;
import com.concertrip.server.mapper.SubscribeMapper;
import com.concertrip.server.mapper.UserMapper;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.model.FcmReq;
import com.concertrip.server.service.NoticeService;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import com.sun.nio.sctp.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Created by HYEON on 2019-01-05.
 */

@Service
@Slf4j
public class FcmService {
    private static final String FIREBASE_SERVER_KEY = "key=AAAAP2OleBU:APA91bEw1R4qFNJRi0GrXu8wp0N9w17a1mKaqpESA-jsAuVlV0yVbYRMhOweZG-F7l6Ml8Hfn_oMu4w1zVLop_Hgo6Hy_13hq0yl7gWSXi_gHQAfz79YUwKMefLrGv2ERk6Yz0Ay7dkU";
    private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";

    private final UserMapper userMapper;
    private final SubscribeMapper subscribeMapper;
    private final ArtistsRepository artistsRepository;
    private final NoticeService noticeService;
    private final NoticeMapper noticeMapper;

    public FcmService(UserMapper userMapper, SubscribeMapper subscribeMapper, ArtistsRepository artistsRepository, NoticeService noticeService, NoticeMapper noticeMapper) {
        this.userMapper = userMapper;
        this.subscribeMapper = subscribeMapper;
        this.artistsRepository = artistsRepository;
        this.noticeService = noticeService;
        this.noticeMapper = noticeMapper;
    }


    public DefaultRes send2(final FcmReq fcmReq) {
        try {
            // get fcm token list by user subscibe
            List<Subscribe> subscribeList = subscribeMapper.getSubscribeTypeObj(fcmReq.getType(),fcmReq.getObjIdx());
            if (subscribeList.size() ==0) return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NO_SUBSCRIBE);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", FIREBASE_SERVER_KEY);
            headers.set("Content-Type", "application/json");
            Date today = new Date();
            // send
            Map<String, String> notification = new HashMap<>();
            notification.put("title", fcmReq.getTitle());
            notification.put("body", fcmReq.getBody());

            for (Subscribe subscribe : subscribeList) {
                int userIdx = subscribe.getUserIdx();
                String token = userMapper.findUserFcmToken(userIdx);

                Fcm fcm = new Fcm();
                fcm.setTo(token);
                fcm.setNotification(notification);
                log.info(token);

                HttpEntity<Fcm> request = new HttpEntity<Fcm>(fcm, headers);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.postForObject(FIREBASE_API_URL, request, String.class);
                //save
                Notice notice = new Notice();
                notice.setUserIdx(userIdx);
                notice.setTitle(fcmReq.getTitle());
                notice.setBody(fcmReq.getBody());
                notice.setCreatedAt(today);
                noticeMapper.save(notice);
                log.info(notice.toString());
            }
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_NOTICE, subscribeList);
        } catch (Exception e) {
            //Rollback
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}
