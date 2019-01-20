
package com.concertrip.server.api;

import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.model.SubscribeReq;
import com.concertrip.server.service.JwtService;
import com.concertrip.server.service.RenewSubscribeService;
import com.concertrip.server.service.SubscribeService;
import com.concertrip.server.service.UserService;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import com.concertrip.server.utils.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.concertrip.server.model.DefaultRes.FAIL_DEFAULT_RES;


/**
 * Created hyunjk on 2018-12-29.
 * Github : https://github.com/hyunjkluz
 */

@Slf4j
@RestController
@RequestMapping("api/subscribe")
public class SubscribeController {
    private final SubscribeService subscribeService;
    private final JwtService jwtService;

    public SubscribeController(final SubscribeService subscribeService, final JwtService jwtService) {
        this.subscribeService = subscribeService;
        this.jwtService = jwtService;
    }

    @Auth
    @GetMapping("artist")
    public ResponseEntity subscribeArtistList(@RequestHeader(value = "Authorization") final String token) {
        try {
            JwtService.Token decodedToken = jwtService.decode(token);
            return new ResponseEntity<>(subscribeService.subscribeList(decodedToken.getUser_idx(), "artist"), HttpStatus.OK);
        } catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.OK);
        }
    }

    @Auth
    @GetMapping("event")
    public ResponseEntity subscribeEventList(@RequestHeader(value = "Authorization") final String token) {
        try {
            JwtService.Token decodedToken = jwtService.decode(token);
            return new ResponseEntity<>(subscribeService.subscribeList(decodedToken.getUser_idx(), "event"), HttpStatus.OK);
        } catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.OK);
        }
    }

    @Auth
    @GetMapping("genre")
    public ResponseEntity subscribeGenreList(@RequestHeader(value = "Authorization") final String token) {
        try {
            JwtService.Token decodedToken = jwtService.decode(token);
            return new ResponseEntity<>(subscribeService.subscribeList(decodedToken.getUser_idx(), "genre"), HttpStatus.OK);
        } catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.OK);
        }
    }

    @Auth
    @PostMapping("artist")
    public ResponseEntity subscribeArtist(@RequestHeader(value = "Authorization") final String token,
                                       @RequestBody final SubscribeReq subscribeReq) {
        try {
            if (subscribeReq.getId().equals("")) {
                return new ResponseEntity<>(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_ARTISTS), HttpStatus.OK);
            }
            JwtService.Token decodedToken = jwtService.decode(token);
            return new ResponseEntity<>(subscribeService.subscribe(decodedToken.getUser_idx(), "artist", subscribeReq.getId()), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.OK);
        }
    }

    @Auth
    @PostMapping("event")
    public ResponseEntity subscribeEvent(@RequestHeader(value = "Authorization") final String token,
                                         @RequestBody final SubscribeReq subscribeReq) {
        try {
            if (subscribeReq.getId().equals("")) {
                return new ResponseEntity<>(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_EVENT), HttpStatus.OK);
            }
            JwtService.Token decodedToken = jwtService.decode(token);
            return new ResponseEntity<>(subscribeService.subscribe(decodedToken.getUser_idx(), "event", subscribeReq.getId()), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.OK);
        }
    }

    @Auth
    @PostMapping("genre")
    public ResponseEntity subscribeGenre(@RequestHeader(value = "Authorization") final String token,
                                         @RequestBody final SubscribeReq subscribeReq) {
        try {
            if (subscribeReq.getId().equals("")) {
                return new ResponseEntity<>(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_GENRE), HttpStatus.OK);
            }
            JwtService.Token decodedToken = jwtService.decode(token);
            return new ResponseEntity<>(subscribeService.subscribe(decodedToken.getUser_idx(), "genre", subscribeReq.getId()), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.OK);
        }
    }

    @GetMapping("fcm")
    public ResponseEntity pushMessage(@RequestParam(value = "type", defaultValue = "") final String type,
                                      @RequestParam(value = "objIdx", defaultValue = "") final String objIdx) {
        try {
                return new ResponseEntity<>(subscribeService.pushList(type, objIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("artist/test")
    public ResponseEntity subscribeArtistTest(@RequestHeader(value = "Authorization") final Integer token,
                                          @RequestBody final SubscribeReq subscribeReq) {
        try {
            return new ResponseEntity<>(subscribeService.subscribe(token, "artist", subscribeReq.getId()), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.OK);
        }
    }
    @PostMapping("event/test")
    public ResponseEntity subscribeEvent(@RequestHeader(value = "Authorization") final Integer token,
                                         @RequestBody final SubscribeReq subscribeReq) {
        try {
            return new ResponseEntity<>(subscribeService.subscribe(token, "event", subscribeReq.getId()), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.OK);
        }
    }

    @PostMapping("genre/test")
    public ResponseEntity subscribeGenre(@RequestHeader(value = "Authorization") final Integer token,
                                         @RequestBody final SubscribeReq subscribeReq) {
        try {
            return new ResponseEntity<>(subscribeService.subscribe(token, "genre", subscribeReq.getId()), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.OK);
        }
    }

    @GetMapping("issub/test")
    public ResponseEntity isSubTest(
            @RequestHeader(value = "Authorization") final Integer token,
            @RequestParam(value = "type") final String type,
            @RequestParam(value = "id") final String id)
    {
        try {
            return new ResponseEntity<>(DefaultRes.res(StatusCode.OK, ResponseMessage.TEST_OK, subscribeService.isSubscribe(token, type, id)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.OK);
        }
    }

    @GetMapping("subnum/test")
    public ResponseEntity subNum(
            @RequestParam(value = "type") final String type,
            @RequestParam(value = "id") final String id)
    {
        try {
            return new ResponseEntity<>(DefaultRes.res(StatusCode.OK, ResponseMessage.TEST_OK, subscribeService.subscribeNum(type, id)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.OK);
        }
    }
}

