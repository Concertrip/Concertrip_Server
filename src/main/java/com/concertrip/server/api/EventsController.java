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
@RequestMapping("events")
public class EventsController {
    private final EventsService eventsService;

    public EventsController(EventsService eventsService) {
        this.eventsService = eventsService;
    }


    @GetMapping("")
    public ResponseEntity getAllEvents() {
        return new ResponseEntity<>(eventsService.selectAll(), HttpStatus.OK);
    }

    @GetMapping("/{eventsId}")
    public ResponseEntity getEventsById(@PathVariable(value = "eventsId") final String _id) {
        if (_id.equals("")) return new ResponseEntity(DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_EVENT), HttpStatus.OK);
        return new ResponseEntity(eventsService.findEventsById(_id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity addEvents(@RequestBody final Events events){
        return new ResponseEntity(eventsService.insert(events), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity updateEvents(@RequestBody final Events events){
        return new ResponseEntity(eventsService.update(events), HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity deleteEvents(@RequestParam(value = "id", defaultValue = "") final String _id){
        if(_id.equals("")) return new ResponseEntity(DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_EVENT), HttpStatus.OK);

        return new ResponseEntity<>(eventsService.delete(_id), HttpStatus.OK);
    }

    @GetMapping("/{eventIdx}/bell")
    public ResponseEntity subscribeEvents(
            @RequestHeader (value = "Autorization") final String token,
            @PathVariable("eventIdx") final String eventIdx) {
        try {
            if (eventIdx.isEmpty()) {
                return new ResponseEntity<>(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_EVENT), HttpStatus.OK);
            }
            if (token.isEmpty()) {
                return new ResponseEntity<>(DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_USER), HttpStatus.OK);
            }
            return new ResponseEntity<>(eventsService.subscribe(eventIdx, token), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.OK);
        }
    }
}