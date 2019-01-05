package com.concertrip.server.api;

import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.service.CalendarService;
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

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }


    @GetMapping("tab")
    public ResponseEntity getCalendarTab(@RequestHeader(value = "Authorization") final Integer token) {
        try {
            if (token == null) {
                return new ResponseEntity<>(DefaultRes.res(StatusCode.UNAUTHORIZED, ResponseMessage.EMPTY_TOKEN), HttpStatus.OK);
            }
            return new ResponseEntity<>(calendarService.getCalendarTab(token), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.OK);
        }
    }

    @GetMapping("type")
    public ResponseEntity getTypeCalendar(@RequestHeader(value = "Authorization") final Integer token,
                                          @RequestParam(value = "type", defaultValue = "") final String type,
                                          @RequestParam(value = "id", defaultValue = "") final String id,
                                          @RequestParam(value = "year") final Integer year,
                                          @RequestParam(value = "month") final Integer month) {
        if (token.equals("") || type.equals("") || year == null || month == null) {
            return new ResponseEntity<>(DefaultRes.res(StatusCode.UNAUTHORIZED, ResponseMessage.EMPTY_TOKEN), HttpStatus.OK);
        } else {
            if (type.equals("all")) {
                return  new ResponseEntity<>(calendarService.getAllCalendar(token, year, month, -1), HttpStatus.OK);
            }
            if (type.equals("mvp")) {
                return  new ResponseEntity<>(calendarService.getMvpCalendar(token, year, month, -1) , HttpStatus.OK);
            }

            if (id.equals("")) {
                return  new ResponseEntity<>(DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NULL_VALUE), HttpStatus.OK);
            }

            if (type.equals("artist")) {
                return  new ResponseEntity<>(calendarService.getArtistCalendar(token, id, year, month, -1), HttpStatus.OK);
            }
            if (type.equals("genre")) {
                return  new ResponseEntity<>(calendarService.getGenreCalendar(token, id, year, month, -1), HttpStatus.OK);
            } else {
                return  new ResponseEntity<>(DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NO_CALENDAR), HttpStatus.OK);
            }
        }
    }

    @GetMapping("day")
    public ResponseEntity getTypeDayCalendar(@RequestHeader(value = "Authorization") final Integer token,
                                          @RequestParam(value = "type", defaultValue = "") final String type,
                                          @RequestParam(value = "id", defaultValue = "") final String id,
                                          @RequestParam(value = "year") final Integer year,
                                          @RequestParam(value = "month") final Integer month,
                                          @RequestParam(value = "day") final Integer day) {
        if (token.equals("") || type.equals("") || year == null || month == null || day == null) {
            return new ResponseEntity<>(DefaultRes.res(StatusCode.UNAUTHORIZED, ResponseMessage.EMPTY_TOKEN), HttpStatus.OK);
        } else {
            if (type.equals("all")) {
                return  new ResponseEntity<>(calendarService.getAllCalendar(token, year, month, day), HttpStatus.OK);
            }
            if (type.equals("mvp")) {
                return  new ResponseEntity<>(calendarService.getMvpCalendar(token, year, month, day) , HttpStatus.OK);
            }

            if (id.equals("")) {
                return  new ResponseEntity<>(DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NULL_VALUE), HttpStatus.OK);
            }

            if (type.equals("artist")) {
                return  new ResponseEntity<>(calendarService.getArtistCalendar(token, id, year, month, day), HttpStatus.OK);
            }

            if (type.equals("genre")) {
                return  new ResponseEntity<>(calendarService.getGenreCalendar(token, id, year, month, day), HttpStatus.OK);
            } else {
                return  new ResponseEntity<>(DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NO_CALENDAR), HttpStatus.OK);
            }
        }
    }


}
