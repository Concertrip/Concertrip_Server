package com.concertrip.server.dto;

import com.concertrip.server.model.CommonListReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created hyunjk on 2018-12-26.
 * Github : https://github.com/hyunjkluz
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Search {
    private List<CommonListReq> artists;
    private List<CommonListReq> events;
    private List<CommonListReq> genres;
}
