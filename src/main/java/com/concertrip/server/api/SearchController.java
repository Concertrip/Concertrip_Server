package com.concertrip.server.api;

import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.service.GenreService;
import com.concertrip.server.service.SearchService;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
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
    private final GenreService genreService;

    public SearchController(SearchService searchService, GenreService genreService) {
        this.searchService = searchService;
        this.genreService = genreService;
    }

    //TODO
    @GetMapping("")
    public ResponseEntity getEventsById(@RequestHeader(value = "Authorization") final int token,
                                        @RequestParam("tag") final String tag) {
        if (tag.equals("")) return new ResponseEntity<>(DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_TAG), HttpStatus.OK);


        return new ResponseEntity<>(searchService.search(token, tag), HttpStatus.OK);
    }

    @GetMapping("tab")
    public ResponseEntity searchByTab(
            @RequestHeader(value = "Authorization") final Integer token,
            @RequestParam("name") final String name) {
        if (name.equals("")) return new ResponseEntity<>(DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_TAG), HttpStatus.OK);
        if (ObjectUtils.isEmpty(token)) {
            return new ResponseEntity<>(DefaultRes.res(StatusCode.UNAUTHORIZED, ResponseMessage.EMPTY_TOKEN), HttpStatus.OK);
        }
        return new ResponseEntity<>(searchService.searchByTab(token, name), HttpStatus.OK);
    }
}
