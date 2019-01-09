package com.concertrip.server.api;

import com.concertrip.server.domain.Events;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.service.EventsService;
import com.concertrip.server.service.JwtService;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import com.concertrip.server.utils.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created hyunjk on 2018-12-25.
 * Github : https://github.com/hyunjkluz
 */
@Slf4j
@RestController
@RequestMapping("api/event")
public class EventsController {
    private final EventsService eventsService;
    private final JwtService jwtService;

    public EventsController(final EventsService eventsService, final JwtService jwtService) {
        this.eventsService = eventsService;
        this.jwtService = jwtService;
    }


    @GetMapping("")
    public ResponseEntity getAllEvents() {
        return new ResponseEntity<>(eventsService.selectAll(), HttpStatus.OK);
    }

    @Auth
    @GetMapping("detail")
    public ResponseEntity getEventsById(
            @RequestHeader(value = "Authorization") final String token,
            @RequestParam(value = "id", defaultValue = "") final String _id) {
        if (_id.equals("") ) {
            return new ResponseEntity<>(DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_EVENT), HttpStatus.OK);
        }
        JwtService.Token decodedToken = jwtService.decode(token);
        return new ResponseEntity<>(eventsService.findEventsById(decodedToken.getUser_idx(), _id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity addEvents(@RequestBody final Events events){
        return new ResponseEntity<>(eventsService.insert(events), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity updateEvents(@RequestBody final Events events){
        return new ResponseEntity<>(eventsService.update(events), HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity deleteEvents(@RequestParam(value = "id", defaultValue = "") final String _id){
        if(_id.equals("")) return new ResponseEntity<>(DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_EVENT), HttpStatus.OK);

        return new ResponseEntity<>(eventsService.delete(_id), HttpStatus.OK);
    }
}