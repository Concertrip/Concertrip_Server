package com.concertrip.server.service;

import com.concertrip.server.dao.ArtistsRepository;
import com.concertrip.server.dao.EventsRepository;
import com.concertrip.server.dao.GenreRepository;
import com.concertrip.server.dto.Subscribe;
import com.concertrip.server.mapper.SubscribeMapper;
import com.concertrip.server.model.CommonListReq;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Created hyunjk on 2018-12-29.
 * Github : https://github.com/hyunjkluz
 */
@Slf4j
@Service
public class SubscribeService {
    private final EventsRepository eventsRepository;
    private final ArtistsRepository artistsRepository;
    private final GenreRepository genreRepository;
    private final SubscribeMapper subscribeMapper;

    public SubscribeService(EventsRepository eventsRepository, ArtistsRepository artistsRepository, GenreRepository genreRepository, SubscribeMapper subscribeMapper) {
        this.eventsRepository = eventsRepository;
        this.artistsRepository = artistsRepository;
        this.genreRepository = genreRepository;
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
            if (isSubscribe(token, type, objIdx)) {
                subscribeMapper.unSubscribe(token, type, objIdx);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.UNSUBSCRIBE);
            } else {
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
            log.info("isSub Mapper");
            log.info(type + objIdx);
            Subscribe subscribe = subscribeMapper.isSubscribe(token, type, objIdx);
            log.info("-----------------");
            log.info(subscribe.getId());
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

    public DefaultRes subscribeList(final int idx, final String type) {
        try {
            List<Subscribe> subIdList = subscribeMapper.getUserSubscribe(idx, type);
            log.info("--------" + subIdList.size());
            List<CommonListReq> subList = new LinkedList<>();

            if (type.equals("artist")) {
                log.info("artist here!!");
                for (Subscribe s : subIdList) {
                    CommonListReq cReq = artistsRepository.findArtistById(s.getId());
                    cReq.setSubscribe(true);
                    subList.add(cReq);
                }
            } else if (type.equals("event")) {
                for (Subscribe s : subIdList) {
                    CommonListReq cReq = eventsRepository.findEventList(s.getId());
                    cReq.setSubscribe(true);
                    subList.add(cReq);
                }
            } else {
                for (Subscribe s : subIdList) {
                    CommonListReq cReq = genreRepository.findGenreById(s.getId());
                    cReq.setSubscribe(true);
                    subList.add(cReq);
                }
            }
            return DefaultRes.res(StatusCode.OK, ResponseMessage.SEARCH_SUCCESS, subList);
        } catch (Exception e) {
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }


}
