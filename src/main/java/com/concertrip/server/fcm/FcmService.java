package com.concertrip.server.fcm;

import com.concertrip.server.dto.Subscribe;
import com.concertrip.server.dto.User;
import com.concertrip.server.mapper.SubscribeMapper;
import com.concertrip.server.mapper.UserMapper;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
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
    private static final String FIREBASE_USER_TOKEN = "cCwY9X4zosA:APA91bFwdNoZnRxzg_q5Ioucc2sVNW6X_uE73xNLL_TDA20S0II8ez4mmZsU4E3OqLOCikC2vkfWpjXveIMA9vsA0zK5lTKDHr7zAsLObJKIjaFEdBIDZa4d34QD4JkFsj03M9NWB3EZ";

    private final UserMapper userMapper;
    private final SubscribeMapper subscribeMapper;

    public FcmService(UserMapper userMapper, SubscribeMapper subscribeMapper) {
        this.userMapper = userMapper;
        this.subscribeMapper = subscribeMapper;
    }

    public CompletableFuture<String> send(final String type, final String objIdx) {
        try {
            List<Subscribe> subscribeList = subscribeMapper.getSubscribeTypeObj(type, objIdx);
                List<Integer> userIdxList = new ArrayList<>();

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", FIREBASE_SERVER_KEY);
                headers.set("Content-Type", "application/json");

                List<Fcm> fcmList = new ArrayList<>();
                Fcm fcm = new Fcm();
                User user = new User();

                log.info(subscribeList.toString());
                Map<String, String> notification = new HashMap<>();

                for (Subscribe subscribe : subscribeList) {
                    int userIdx = subscribe.getUserIdx();
                    String token = userMapper.findUserToken(userIdx);
                    fcm.setTo(token);
                    userIdxList.add(userIdx);
                    if (type.equals("artist")) {
                        notification.put("title", "아티스트");
                        notification.put("body", "아티스트를 구독했습니다.");
                        fcm.setNotification(notification);
                    } else if (type.equals("event")) {
                        notification.put("title", "이벤트");
                        notification.put("body", "이벤트를 구독했습니다");
                        fcm.setNotification(notification);
                    } else {
                        notification.put("title", "장르");
                        notification.put("body", "장르를 구독했습니다");
                        fcm.setNotification(notification);
                    }
                    fcmList.add(fcm);
                    log.info(fcmList.toString());
                }

                HttpEntity<Fcm> request = new HttpEntity<Fcm>(fcm, headers);
                RestTemplate restTemplate = new RestTemplate();
                String firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, request, String.class);
                return CompletableFuture.completedFuture(firebaseResponse);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return CompletableFuture.supplyAsync(String::new);
        }
    }

}
