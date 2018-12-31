package com.concertrip.server.service;

import com.concertrip.server.dao.EventsRepository;
import com.concertrip.server.dao.GenreRepository;
import com.concertrip.server.domain.Events;
import com.concertrip.server.domain.Genre;
import com.concertrip.server.mapper.GenreSubscribeMapper;
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
    private final GenreSubscribeMapper genreSubscribeMapper;
    private final EventsRepository eventsRepository;

    public GenreService(GenreRepository genreRepository, GenreSubscribeMapper genreSubscribeMapper, EventsRepository eventsRepository) {
        this.genreRepository = genreRepository;
        this.genreSubscribeMapper = genreSubscribeMapper;
        this.eventsRepository = eventsRepository;
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

    public DefaultRes findById(final String _id, final String token) {
        try {
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
            genreDetailReq.setSubscribeNum(genreSubscribeMapper.getSubscribeNum("genre", genre.get_id()));
            genreDetailReq.setIsSubscribe(genreSubscribeMapper.checkSubscribe("genre", genre.get_id(), token) > 0);
            List<Events> eventsList = eventsRepository.findAllByFilterIn("힙합");
            log.info(eventsList.size() + " - events size");
            for (Events e: eventsList) {
                log.info(e.toString());
            }

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ARTISTS, genreDetailReq);
        } catch (Exception e) {
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    public DefaultRes subscribe(final String _id, final String token) {
        try {
            genreSubscribeMapper.subscribe(_id, token);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.SUBSCRIBE);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}
