package com.concertrip.server.dto;

import com.concertrip.server.domain.Artists;
import com.concertrip.server.domain.Events;
import com.concertrip.server.model.ArtistsReq;
import com.concertrip.server.model.EventsReq;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created hyunjk on 2018-12-26.
 * Github : https://github.com/hyunjkluz
 */
@Data
@NoArgsConstructor
public class Search {
    private List<ArtistsReq> artists;
    private List<EventsReq> events;
}
