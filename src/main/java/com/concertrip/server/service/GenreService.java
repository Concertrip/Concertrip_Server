package com.concertrip.server.service;

import com.concertrip.server.dao.EventsRepository;
import com.concertrip.server.dao.GenreRepository;
import com.concertrip.server.domain.Genre;
import com.concertrip.server.mapper.SubscribeMapper;
import com.concertrip.server.model.CommonListReq;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.model.GenreDetailReq;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GenreService {
    private final GenreRepository genreRepository;
    private final EventsRepository eventsRepository;
    private final SubscribeMapper subscribeMapper;
    private final SubscribeService subscribeService;
    private final SearchService searchService;

    public GenreService(
            GenreRepository genreRepository,
            EventsRepository eventsRepository,
            SubscribeMapper subscribeMapper,
            SubscribeService subscribeService,
            SearchService searchService) {
        this.genreRepository = genreRepository;
        this.eventsRepository = eventsRepository;
        this.subscribeMapper = subscribeMapper;
        this.subscribeService = subscribeService;
        this.searchService = searchService;
    }

    public DefaultRes findAll() {
        try {
            List<CommonListReq> commonListReqList = new ArrayList<>();
            List<Genre> genreList = genreRepository.findAll();
            for (Genre genre:
                 genreList) {
                CommonListReq commonListReq = new CommonListReq();
                commonListReq.set_id(genre.get_id());
                commonListReq.setProfileImg(genre.getProfileImg());
                commonListReq.setName(genre.getName());
                commonListReqList.add(commonListReq);
            }

            if (genreList.size() == 0) {
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_ARTISTS);
            } else {
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ARTISTS, commonListReqList);
            }
        } catch (Exception e) {
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    public DefaultRes save(final Genre genre) {
        try {
            Genre ret = genreRepository.save(genre);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_ARTISTS, ret);
        } catch (Exception e) {
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    public DefaultRes findById(final String _id, final Integer token) {
        try {
            if (ObjectUtils.isEmpty(token)) {
                return DefaultRes.res(401, ResponseMessage.EMPTY_TOKEN);
            }
            GenreDetailReq genreDetailReq = new GenreDetailReq();
            Genre genre = genreRepository.findBy_idEquals(_id);
            if (ObjectUtils.isEmpty(genre)) {
                return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_ARTISTS);
            }
            genreDetailReq.set_id(genre.get_id());
            genreDetailReq.setName(genre.getName());
            genreDetailReq.setProfileImg(genre.getProfileImg());
            genreDetailReq.setBackImg(genre.getBackImg());
            genreDetailReq.setYoutubeUrl(genre.getYoutubeUrl());
            genreDetailReq.setSubscribeNum(subscribeService.subscribeNum("genre", _id));
            genreDetailReq.setIsSubscribe(subscribeService.isSubscribe(token, "genre", _id));
            List<CommonListReq> eventsList = searchService.searchEvent(token, genre.getCode());
            genreDetailReq.setEventList(eventsList);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ARTISTS, genreDetailReq);
        } catch (Exception e) {
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}
