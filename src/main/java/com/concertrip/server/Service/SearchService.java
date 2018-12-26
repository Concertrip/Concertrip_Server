package com.concertrip.server.service;

import com.concertrip.server.dal.EventsDAL;
import com.concertrip.server.domain.Events;
import com.concertrip.server.dto.Search;
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
    private final EventsDAL eventsDal;

    public SearchService(EventsDAL eventsDAL) {
        this.eventsDal = eventsDAL;
    }

    public DefaultRes search(String tag) {
        try {
            List<Events> events = eventsDal.findByTitle(tag);
            List<Events> eventsTag = eventsDal.findByTag(tag);

            for (Events e : eventsTag) {
                events.add(e);
            }

            Search search = new Search();
            search.setEvents(events);

            if (search.getEvents().size() == 0)
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_TAG);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.SEARCH_SUCCESS, search);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}
