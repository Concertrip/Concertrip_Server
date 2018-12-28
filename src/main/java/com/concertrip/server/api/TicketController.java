package com.concertrip.server.api;

import com.concertrip.server.dto.Ticket;
import com.concertrip.server.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.concertrip.server.model.DefaultRes.FAIL_DEFAULT_RES;

/**
 * Created by HYEON on 2018-12-28.
 */

@Slf4j
@RestController
@RequestMapping("tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(final TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("")
    public ResponseEntity getTicket(@RequestParam("eventId") final String eventId) {
        try{
            if (eventId.isEmpty()) return new ResponseEntity<>(ticketService.getAllTicket(), HttpStatus.OK);
            return new ResponseEntity<>(ticketService.findByeventId(eventId), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("")
    public ResponseEntity saveTicket(@RequestBody final Ticket ticket) {
        try {
            return new ResponseEntity<>(ticketService.saveTicket(ticket), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{userIdx}")
    public ResponseEntity deleteTicket(@PathVariable(value = "userIdx") final int userIdx) {
        try {
            return new ResponseEntity<>(ticketService.deleteByuserIdx(userIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
