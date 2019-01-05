
package com.concertrip.server.service;

import com.concertrip.server.dao.ArtistsRepository;
import com.concertrip.server.dao.EventsRepository;
import com.concertrip.server.dao.GenreRepository;
import com.concertrip.server.domain.Artists;
import com.concertrip.server.domain.Events;
import com.concertrip.server.domain.Genre;
import com.concertrip.server.dto.Subscribe;
import com.concertrip.server.mapper.SubscribeMapper;
import com.concertrip.server.model.CommonListReq;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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


    public Boolean isSubscribe(final Integer userIdx, final String type, final String objIdx) {
        return subscribeMapper.isSubscribe(userIdx, type, objIdx) > 0;
    }

    public Integer subscribeNum(final String type, final String objIdx) {
        return subscribeMapper.subscribeNum(type, objIdx);
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
            if (ObjectUtils.isEmpty(token)) {
                return DefaultRes.res(401, ResponseMessage.EMPTY_TOKEN);
            }
            if (isSubscribe(token, type, objIdx)) {
                subscribeMapper.unSubscribe(token, type, objIdx);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.UNSUBSCRIBE);
            } else {
                if (isRealObj(type, objIdx)) {
                    subscribeMapper.subscribe(token, type, objIdx);
                    return DefaultRes.res(StatusCode.OK, ResponseMessage.SUBSCRIBE);
                } else {
                    return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.DISMATCH_TYPE_OBJ);
                }
            }
        } catch (Exception e) {
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    public DefaultRes subscribeList(final int idx, final String type) {
        try {
            if (ObjectUtils.isEmpty(idx)) {
                return DefaultRes.res(401, ResponseMessage.EMPTY_TOKEN);
            }
            List<Subscribe> subIdList = subscribeMapper.getUserSubscribe(idx, type);
            List<CommonListReq> subList = new LinkedList<>();

            if (type.equals("artist")) {
                log.info(subIdList.toString());
                for (Subscribe s : subIdList) {
                    CommonListReq cReq = artistsRepository.findArtistById(s.getObjIdx());
                    if (ObjectUtils.isEmpty(cReq)) {
                        continue;
                    }
                    Artists artists = artistsRepository.findArtistsBy_id(cReq.get_id());
                    cReq.setGroup(artists.getMember().length != 0);
                    cReq.setSubscribe(true);
                    subList.add(cReq);
                }
            } else if (type.equals("event")) {
                for (Subscribe s : subIdList) {
                    CommonListReq cReq = eventsRepository.findEventList(s.getObjIdx());
                    if (ObjectUtils.isEmpty(cReq)) {
                        continue;
                    }
                    cReq.setSubscribe(true);
                    subList.add(cReq);
                }
            } else {
                for (Subscribe s : subIdList) {
                    CommonListReq cReq = genreRepository.findGenreById(s.getObjIdx());
                    if (ObjectUtils.isEmpty(cReq)) {
                        continue;
                    }
                    cReq.setSubscribe(true);
                    subList.add(cReq);
                }
            }
            if (subList.size() == 0) {
                return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NO_CONTENT, subList);
            }
            return DefaultRes.res(StatusCode.OK, ResponseMessage.SEARCH_SUCCESS, subList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 구돌하려는 아이디가 해당 타입의 컬렉션이 실제하는 아이디인지 확인
     *
     * @param type
     * @param objIdx
     * @return
     */
    public boolean isRealObj(final String type, final String objIdx) {
        switch (type) {
            case "event":
                Events events = eventsRepository.findEventsBy_id(objIdx);
                if (events != null) {
                    return true;
                }
                break;
            case "artist" :
                Artists artists = artistsRepository.findArtistsBy_id(objIdx);
                if (artists != null) {
                    return true;
                }
                break;
            case "genre" :
                Genre genre = genreRepository.findGenreBy_idEquals(objIdx);
                if (genre != null) {
                    return true;
                }
                break;
        }
        return false;

    }
}

