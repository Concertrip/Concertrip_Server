package com.concertrip.server.api;

import com.concertrip.server.domain.Events;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.service.EventsService;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.Optional;

/**
 * Created hyunjk on 2018-12-25.
 * Github : https://github.com/hyunjkluz
 */
@Slf4j
@RestController
@RequestMapping("api/event")
public class EventsController {
    private final EventsService eventsService;

    public EventsController(EventsService eventsService) {
        this.eventsService = eventsService;
    }


    @GetMapping("")
    public ResponseEntity getAllEvents() {
        return new ResponseEntity<>(eventsService.selectAll(), HttpStatus.OK);
    }

    @GetMapping("detail")
    public ResponseEntity getEventsById(
            @RequestHeader(value = "Authorization") final Integer token,
            @RequestParam(value = "id", defaultValue = "") final String _id) {
        if (_id.equals("") ) return new ResponseEntity<>(DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_EVENT), HttpStatus.OK);
        return new ResponseEntity<>(eventsService.findEventsById(token, _id), HttpStatus.OK);
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