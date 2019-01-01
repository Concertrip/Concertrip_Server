
package com.concertrip.server.api;

import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.model.SubscribeReq;
import com.concertrip.server.service.SubscribeService;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Created hyunjk on 2018-12-29.
 * Github : https://github.com/hyunjkluz
 */

@Slf4j
@RestController
@RequestMapping("api/subscribe")
public class SubscribeController {
    private final SubscribeService subscribeService;

    public SubscribeController(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }

    @GetMapping("artist")
    public ResponseEntity subscribeArtistList(@RequestHeader(value = "Authorization") final int token) {
        return new ResponseEntity<>(subscribeService.subscribeList(token, "artist"), HttpStatus.OK);
    }

    @GetMapping("event")
    public ResponseEntity subscribeEventList(@RequestHeader(value = "Authorization") final int token) {
        return new ResponseEntity<>(subscribeService.subscribeList(token, "event"), HttpStatus.OK);
    }

    @GetMapping("genre")
    public ResponseEntity subscribeGenreList(@RequestHeader(value = "Authorization") final int token) {
        return new ResponseEntity<>(subscribeService.subscribeList(token, "genre"), HttpStatus.OK);
    }

    @PostMapping("artist")
    public ResponseEntity subscribeArtist(@RequestHeader(value = "Authorization") final int token,
                                       @RequestBody final SubscribeReq subscribeReq) {
        try {
            if (subscribeReq.getId().equals("")) {
                return new ResponseEntity<>(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_ARTISTS), HttpStatus.OK);
            }
            if (token == -1) {
                return new ResponseEntity<>(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_USER), HttpStatus.OK);
            }
            return new ResponseEntity<>(subscribeService.subscribe(token, "artist", subscribeReq.getId()), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.OK);
        }
    }

    @PostMapping("event")
    public ResponseEntity subscribeEvent(@RequestHeader(value = "Authorization") final int token,
                                         @RequestBody final SubscribeReq subscribeReq) {
        try {
            if (subscribeReq.getId().equals("")) {
                return new ResponseEntity<>(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_EVENT), HttpStatus.OK);
            }
            if (token == -1) {
                return new ResponseEntity<>(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_USER), HttpStatus.OK);
            }
            return new ResponseEntity<>(subscribeService.subscribe(token, "event", subscribeReq.getId()), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.OK);
        }
    }

    @PostMapping("genre")
    public ResponseEntity subscribeGenre(@RequestHeader(value = "Authorization") final int token,
                                         @RequestBody final SubscribeReq subscribeReq) {
        try {
            if (subscribeReq.getId().equals("")) {
                return new ResponseEntity<>(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_GENRE), HttpStatus.OK);
            }
            if (token == -1) {
                return new ResponseEntity<>(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_USER), HttpStatus.OK);
            }
            return new ResponseEntity<>(subscribeService.subscribe(token, "genre", subscribeReq.getId()), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.OK);
        }
    }
}

