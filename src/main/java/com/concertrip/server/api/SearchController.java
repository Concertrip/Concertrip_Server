package com.concertrip.server.api;

import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.service.SearchService;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created hyunjk on 2018-12-26.
 * Github : https://github.com/hyunjkluz
 */
@Slf4j
@RestController
@RequestMapping("api/search")
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    //TODO
    @GetMapping("")
    public ResponseEntity getEventsById(@RequestHeader(value = "Authorization") final int token,
                                        @RequestParam("tag") final String tag) {
        if (tag.equals("")) return new ResponseEntity<>(DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_TAG), HttpStatus.OK);


        return new ResponseEntity<>(searchService.search(token, tag), HttpStatus.OK);
    }
}