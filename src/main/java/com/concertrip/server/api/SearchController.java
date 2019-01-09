package com.concertrip.server.api;

import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.service.GenreService;
import com.concertrip.server.service.JwtService;
import com.concertrip.server.service.SearchService;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import com.concertrip.server.utils.auth.Auth;
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
    private final JwtService jwtService;

    public SearchController(SearchService searchService, GenreService genreService, JwtService jwtService) {
        this.searchService = searchService;
        this.genreService = genreService;
        this.jwtService = jwtService;
    }

    @Auth
    @GetMapping("")
    public ResponseEntity getEventsById(@RequestHeader(value = "Authorization") final String token,
                                        @RequestParam("tag") final String tag) {
        try {
            if (tag.equals("")) return new ResponseEntity<>(DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_TAG), HttpStatus.OK);
            JwtService.Token decodedToken = jwtService.decode(token);

            return new ResponseEntity<>(searchService.search(decodedToken.getUser_idx(), tag), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.OK);
        }
    }

    @Auth
    @GetMapping("tab")
    public ResponseEntity searchByTab(
            @RequestHeader(value = "Authorization") final String token,
            @RequestParam("name") final String name) {
        try {
            if (name.equals("")) return new ResponseEntity<>(DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_TAG), HttpStatus.OK);
            JwtService.Token decodedToken = jwtService.decode(token);

            return new ResponseEntity<>(searchService.searchByTab(decodedToken.getUser_idx(), name), HttpStatus.OK);
        } catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.OK);
        }
    }
}
