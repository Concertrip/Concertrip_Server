package com.concertrip.server.fcm;

import com.concertrip.server.dto.Subscribe;
import com.concertrip.server.mapper.SubscribeMapper;
import com.concertrip.server.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Created by HYEON on 2019-01-05.
 */

@Service
@Slf4j
public class FcmService {
    private static final String FIREBASE_SERVER_KEY = "key=AAAAP2OleBU:APA91bEw1R4qFNJRi0GrXu8wp0N9w17a1mKaqpESA-jsAuVlV0yVbYRMhOweZG-F7l6Ml8Hfn_oMu4w1zVLop_Hgo6Hy_13hq0yl7gWSXi_gHQAfz79YUwKMefLrGv2ERk6Yz0Ay7dkU";
    private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String FIREBASE_USER_TOKEN = "fuYtNF9Lxcw:APA91bEDMBoHuWYXrZDWfo0aYBWaju3gV4nU9SF9uSLEelJvgmGxpC-YtPUYuVK-cWhN3uPpW3DjCLcQA6bMh_xj1nM3Tv0dVfItWcy0PzaEZoG0qLpULP9YFHWsewoQz7Uo9IVjNvwL";
    //"dQjbpmATqVM:APA91bFzHUyEccx9bHXIT-Mdifb4Qsy1qj5aRIyHoc92IJnEbgthPpETZYdiCQsgdx1SBt28Ow6VaLV64uaT8U_ed3SZEJOJXfZEI5xlOcQ3PwxeBuxrWDANiFNOhvp8dtQcngi19NHI";

    private final UserMapper userMapper;
    private final SubscribeMapper subscribeMapper;

    public FcmService(UserMapper userMapper, SubscribeMapper subscribeMapper) {
        this.userMapper = userMapper;
        this.subscribeMapper = subscribeMapper;
    }

    public CompletableFuture<String> send(final String type, final String objIdx) {

        List<Subscribe> subscribeList = subscribeMapper.getSubscribeTypeObj(type, objIdx);
        List<Integer> userIdxList = new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", FIREBASE_SERVER_KEY);
        headers.set("Content-Type", "application/json");
        Fcm fcm = new Fcm();
        fcm.setTo(FIREBASE_USER_TOKEN);
        Map<String, String> notification = new HashMap<>();

        for (Subscribe subscribe : subscribeList) {
            userIdxList.add(subscribe.getUserIdx());
            if (type.equals("artist")) {
                notification.put("title", "아티스트");
                notification.put("body", "아티스트를 구독했습니다.");
                fcm.setNotification(notification);
                log.info(userIdxList.toString());
            } else if (type.equals("event")){
                notification.put("title", "이벤트");
                notification.put("body", "이벤트를 구독했습니다");
                fcm.setNotification(notification);
            } else {
                notification.put("title", "장르");
                notification.put("body", "장르를 구독했습니다");
                fcm.setNotification(notification);
            }
        }


        HttpEntity<Fcm> request = new HttpEntity<Fcm>(fcm, headers);

        RestTemplate restTemplate = new RestTemplate();

        String firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, request, String.class);

        return CompletableFuture.completedFuture(firebaseResponse);
    }

}
