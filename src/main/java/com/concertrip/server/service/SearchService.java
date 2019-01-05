package com.concertrip.server.service;

import com.concertrip.server.dao.ArtistsRepository;
import com.concertrip.server.dao.EventsRepository;
import com.concertrip.server.dao.GenreRepository;
import com.concertrip.server.dto.Search;
import com.concertrip.server.mapper.SubscribeMapper;
import com.concertrip.server.model.CommonListReq;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * Created hyunjk on 2018-12-26.
 * Github : https://github.com/hyunjkluz
 */
@Slf4j
@Service
public class SearchService {
    private final EventsRepository eventsRepository;
    private final ArtistsRepository artistsRepository;
    private final GenreRepository genreRepository;
    private final SubscribeService subscribeService;

    public SearchService(EventsRepository eventsRepository, ArtistsRepository artistsRepository, GenreRepository genreRepository, SubscribeService subscribeService1) {
        this.eventsRepository = eventsRepository;
        this.artistsRepository = artistsRepository;
        this.genreRepository = genreRepository;
        this.subscribeService = subscribeService1;
    }

    public DefaultRes search(int idx, String tag) {
        try {
            if (ObjectUtils.isEmpty(idx)) {
                return DefaultRes.res(401, ResponseMessage.EMPTY_TOKEN);
            }
            Search searchResult = new Search();

            //가수에서 찾기
            searchResult.setArtists(searchArtist(idx, tag));

            //이벤트에서 찾기
            searchResult.setEvents(searchEvent(idx, tag));

            //장르에서 찾기
            searchResult.setGenres(searchGenre(idx, tag));

            if (searchResult.getArtists().size() == 0 && searchResult.getEvents().size() == 0 && searchResult.getGenres().size() == 0)
                return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NO_CONTENT, searchResult);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.SEARCH_SUCCESS, searchResult);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    public void setSubscribe(List<CommonListReq> filterList, String type, int idx) {
        for (CommonListReq cReq : filterList) {
            cReq.setSubscribe(subscribeService.isSubscribe(idx, type, cReq.get_id()));
        }

    }

    public void setGroup(List<CommonListReq> filterList) {
        for (CommonListReq commonListReq : filterList) {
            commonListReq.setGroup(artistsRepository.findArtistsBy_id(commonListReq.get_id()).getMember().length != 0);
        }
    }

    public List<CommonListReq> searchEvent(int idx, String tag) {
        List<CommonListReq> eventsFilter = eventsRepository.findByFilter(tag);
        setSubscribe(eventsFilter, "event",idx);

        return eventsFilter;
    }

    public List<CommonListReq> searchArtist(int idx, String tag) {
        List<CommonListReq> artistsFilter = artistsRepository.findByFilter(tag);
        setSubscribe(artistsFilter, "artist", idx);
        setGroup(artistsFilter);

        return artistsFilter;
    }

    public List<CommonListReq> searchGenre(int idx, String tag) {
        List<CommonListReq> genresFilter = genreRepository.findByFilter(tag);
        setSubscribe(genresFilter, "genre", idx);

        return genresFilter;
    }






}
