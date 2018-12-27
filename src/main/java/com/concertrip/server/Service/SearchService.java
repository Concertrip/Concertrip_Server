package com.concertrip.server.service;

import com.concertrip.server.dal.EventsDAL;
import com.concertrip.server.dao.ArtistsRepository;
import com.concertrip.server.dto.Search;
import com.concertrip.server.model.ArtistsReq;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.model.EventsReq;
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
    private final EventsDAL eventsDal;
    private final ArtistsRepository artistsRepository;


    public SearchService(EventsDAL eventsDAL, ArtistsRepository artistsRepository) {
        this.eventsDal = eventsDAL;
        this.artistsRepository = artistsRepository;
    }

    public DefaultRes search(String tag) {
        try {
            List<EventsReq> events = eventsDal.findByTitle(tag);
            List<EventsReq> eventsTag = eventsDal.findByTag(tag);

            for (EventsReq e : eventsTag) {
                events.add(e);
            }

            List<ArtistsReq> artists = artistsRepository.findByName(tag);
            List<ArtistsReq> artistsTag = artistsRepository.findByTag(tag);

            for (ArtistsReq a : artistsTag) {
                artists.add(a);
            }

            Search search = new Search();
            search.setEvents(events);
            search.setArtists(artists);

            if (search.getEvents().size() == 0 && search.getArtists().size() == 0)
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_TAG);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.SEARCH_SUCCESS, search);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}
