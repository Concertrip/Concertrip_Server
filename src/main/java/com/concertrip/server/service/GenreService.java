package com.concertrip.server.service;

import com.concertrip.server.dao.GenreRepository;
import com.concertrip.server.domain.Genre;
import com.concertrip.server.mapper.GenreSubscribeMapper;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Slf4j
@Service
public class GenreService {
    private final GenreRepository genreRepository;
    private final GenreSubscribeMapper genreSubscribeMapper;

    public GenreService(GenreRepository genreRepository, GenreSubscribeMapper genreSubscribeMapper) {
        this.genreRepository = genreRepository;
        this.genreSubscribeMapper = genreSubscribeMapper;
    }

    public DefaultRes findAll() {
        try {
            List<Genre> genreList = genreRepository.findAll();
            if (genreList.size() == 0) {
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_ARTISTS);
            } else {
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ARTISTS, genreList);
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

    public DefaultRes findById(final String _id) {
        try {
            Genre ret = genreRepository.findBy_idEquals(_id);
            if (ObjectUtils.isEmpty(ret)) {
                return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_ARTISTS);
            }
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ARTISTS, ret);
        } catch (Exception e) {
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    public DefaultRes subscribe(final String _id, final String token) {
        try {
            genreSubscribeMapper.subscribe(_id, token);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.SUBSCRIBE_EVENT);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}
