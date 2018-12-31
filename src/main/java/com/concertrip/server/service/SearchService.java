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

    public SearchService(EventsRepository eventsRepository, ArtistsRepository artistsRepository, GenreRepository genreRepository, SubscribeService subscribeService) {
        this.eventsRepository = eventsRepository;
        this.artistsRepository = artistsRepository;
        this.genreRepository = genreRepository;
        this.subscribeService = subscribeService;
    }

    public DefaultRes search(int idx, String tag) {
        try {
            //이벤트에서 찾기
            List<CommonListReq> eventsFilter = eventsRepository.findByFilter(tag);
            setSubscribe(eventsFilter, "event", idx);

            //가수에서 찾기
            List<CommonListReq> artistsFilter = artistsRepository.findByFilter(tag);
            setSubscribe(eventsFilter, "artist", idx);

            //장르에서 찾기
            List<CommonListReq> genresFilter = genreRepository.findByFilter(tag);
            setSubscribe(eventsFilter, "genre", idx);

            if (eventsFilter.size() == 0 && artistsFilter.size() == 0 && genresFilter.size() == 0)
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_TAG);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.SEARCH_SUCCESS, new Search(artistsFilter, eventsFilter, genresFilter));
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
}
