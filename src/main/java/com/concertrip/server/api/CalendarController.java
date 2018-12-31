package com.concertrip.server.api;

import com.concertrip.server.mapper.SubscribeMapper;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.service.CalendarService;
import com.concertrip.server.service.UserService;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created hyunjk on 2018-12-27.
 * Github : https://github.com/hyunjkluz
 */
@Slf4j
@RestController
@RequestMapping("api/calendar")
public class CalendarController {
    private final CalendarService calendarService;
    private final UserService userService;

    public CalendarController(CalendarService calendarService, UserService userService) {
        this.calendarService = calendarService;
        this.userService = userService;
    }

    @GetMapping("tab")
    public ResponseEntity getCalendarTab(@RequestHeader(value = "Authorization") final String token) {
        try {
            if (token.isEmpty()) {
                return new ResponseEntity<>(DefaultRes.res(StatusCode.UNAUTHORIZED, ResponseMessage.EMPTY_TOKEN), HttpStatus.OK);
            }
            Integer userIdx = userService.getUserIdxByToken(token);
            if (userIdx == null) {
                return new ResponseEntity<>(DefaultRes.res(StatusCode.UNAUTHORIZED, ResponseMessage.INVALID_TOKEN), HttpStatus.OK);
            }
//            return new ResponseEntity<>(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.SUBSCRIBE), HttpStatus.OK);
            return new ResponseEntity<>(calendarService.getCalendarTab(userIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.OK);
        }
    }

    @GetMapping("")
    public ResponseEntity findAll(@RequestHeader(value = "Autorization") final String token) {
        try {
            if (token.isEmpty()) {
                return new ResponseEntity<>(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_USER), HttpStatus.OK);
            }
            return new ResponseEntity<>(calendarService.getTotalCalendar(token), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.OK);
        }

    }

    @GetMapping("/events")
    public ResponseEntity findEventsCalendar(@RequestHeader(value = "Autorization") final String token) {
        try {
            if (token.isEmpty()) {
                return new ResponseEntity<>(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_USER), HttpStatus.OK);
            }
            return new ResponseEntity<>(calendarService.getEventsCalendar(token), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity findArtistCalendar(@PathVariable(value = "id") final String id) {
        try {
            if (id.equals("")) {
                return new ResponseEntity<>(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_TAG), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(calendarService.getArtistsCalendar(id), HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.OK);
        }
    }
}
