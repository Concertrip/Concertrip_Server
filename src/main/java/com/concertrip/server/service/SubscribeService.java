package com.concertrip.server.service;

import com.concertrip.server.dto.Subscribe;
import com.concertrip.server.mapper.SubscribeMapper;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created hyunjk on 2018-12-29.
 * Github : https://github.com/hyunjkluz
 */
@Slf4j
@Service
public class SubscribeService {
    private final SubscribeMapper subscribeMapper;

    public SubscribeService(SubscribeMapper subscribeMapper) {
        this.subscribeMapper = subscribeMapper;
    }

    /**
     * 구독하기 / 구독취소
     *
     * @param token
     * @param type
     * @param objIdx
     * @return
     */
    public DefaultRes subscribe(final int token, final String type, final String objIdx) {
        try {
            log.info("Here1");
            if (isSubscribe(token, type, objIdx)) {
                log.info("Here2");
                subscribeMapper.unSubscribe(token, type, objIdx);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.UNSUBSCRIBE);
            } else {
                log.info("Here3");
                subscribeMapper.subscribe(token, type, objIdx);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.SUBSCRIBE);
            }
        } catch (Exception e) {
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 구독 여부 확인
     *
     * @param token
     * @param type
     * @param objIdx
     * @return
     */
    public boolean isSubscribe(final int token, final String type, final String objIdx) {
        try {
            Subscribe subscribe = subscribeMapper.isSubscribe(token, type, objIdx);
            if (subscribe == null) return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 구독자수 알려주기
     *
     * @param type
     * @param objIdx
     * @return
     */
    public int subscribeNum(final String type, final String objIdx) {
        try {
            return subscribeMapper.subscribeNum(type, objIdx).size();
        } catch (Exception e) {
            return -1;
        }
    }


}
