package com.concertrip.server.service;

import com.concertrip.server.dal.SubscribeDAL;
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class RenewSubscribeService {
    private final EventsRepository eventsRepository;
    private final ArtistsRepository artistsRepository;
    private final GenreRepository genreRepository;
    private final SubscribeMapper subscribeMapper;
    private final SearchService searchService;
    private final SubscribeDAL subscribeDAL;

    public RenewSubscribeService(EventsRepository eventsRepository, ArtistsRepository artistsRepository, GenreRepository genreRepository, SubscribeMapper subscribeMapper, SearchService searchService, SubscribeDAL subscribeDAL) {
        this.eventsRepository = eventsRepository;
        this.artistsRepository = artistsRepository;
        this.genreRepository = genreRepository;
        this.subscribeMapper = subscribeMapper;
        this.searchService = searchService;
        this.subscribeDAL = subscribeDAL;
    }

    public Boolean isSubscribe(final Integer userIdx, final String type, final String objIdx) {
        Integer[] subscribeList = this.getSubscribeListOfObject(type, objIdx);
        for (Integer sub : subscribeList) {
            if (sub.equals(userIdx)) {
                return true;
            }
        }
        return false;
    }

    public Integer subscribeNum(final String type, final String objIdx) {
        Integer[] subscribeList = this.getSubscribeListOfObject(type, objIdx);
        return subscribeList.length;
    }

    private Integer[] getSubscribeListOfObject(final String type, final String objIdx) {
        Integer[] subscribeList = {};
        if (type.equals("artist")) {
            Artists artists = artistsRepository.findArtistsBy_id(objIdx);
            subscribeList = artists.getSubscriber();
        }
        if (type.equals("genre")) {
            Genre genre = genreRepository.findGenreBy_idEquals(objIdx);
            subscribeList = genre.getSubscriber();
        }
        if (type.equals("event")) {
            Events events = eventsRepository.findEventsBy_id(objIdx);
            subscribeList = events.getSubscriber();
        }
        return subscribeList;
    }

    public DefaultRes subscribe(final Integer userIdx, final String type, final String objIdx) {
        try {
            if (ObjectUtils.isEmpty(userIdx)) {
                return DefaultRes.res(401, ResponseMessage.EMPTY_TOKEN);
            }
            if (isSubscribe(userIdx, type, objIdx)) {
                subscribeMapper.unSubscribe(userIdx, type, objIdx);
                subscribeDAL.subscribe(type, objIdx, userIdx, true);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.UNSUBSCRIBE);
            } else {
                if (isRealObj(type, objIdx)) {
                    subscribeMapper.subscribe(userIdx, type, objIdx);
                    subscribeDAL.subscribe(type, objIdx, userIdx, false);
                    return DefaultRes.res(StatusCode.OK, ResponseMessage.SUBSCRIBE);
                } else {
                    return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.DISMATCH_TYPE_OBJ);
                }
            }
        } catch (Exception e) {
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    private Boolean isRealObj(final String type, final String objIdx) {
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
                    cReq.setHashTag(searchService.makeHashTag(cReq.get_id()));
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
    public DefaultRes pushList(final String type, final String objIdx) {
        try {
            List<Subscribe> subscribeList = subscribeMapper.getSubscribeTypeObj(type, objIdx);
            List<Integer> userIdxList = new ArrayList<>();

            for(Subscribe subscribe : subscribeList) {
                userIdxList.add(subscribe.getUserIdx());
            }

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, subscribeList);
        } catch (Exception e) {
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);

        }
    }
}
