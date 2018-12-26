package com.concertrip.server.dto;

import com.concertrip.server.domain.Artists;
import com.concertrip.server.domain.Events;
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
    private List<Artists> artists;
    private List<Events> events;
}
