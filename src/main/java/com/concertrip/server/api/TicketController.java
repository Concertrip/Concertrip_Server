package com.concertrip.server.api;

import com.concertrip.server.dto.Ticket;
import com.concertrip.server.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.concertrip.server.model.DefaultRes.FAIL_DEFAULT_RES;

/**
 * Created by HYEON on 2018-12-28.
 */

//TODO
@Slf4j
@RestController
@RequestMapping("api/ticket")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(final TicketService ticketService) {
        this.ticketService = ticketService;
    }

    //사용자가 가지고 있는 모든 티켓 조회  findByDate
    @GetMapping("")
    public ResponseEntity getUserTicket(@RequestHeader(value = "Authorization") final int userIdx) {
        try{
            return new ResponseEntity<>(ticketService.findByUserIdx(userIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //티켓 상세보기
    @GetMapping("detail")
    public ResponseEntity getUserTicketDetail(@RequestHeader(value = "Authorization") final int userIdx,
                                              @RequestParam(value = "id", defaultValue = "") final String _id) {
        try{
            return new ResponseEntity<>(ticketService.findByEventId(userIdx, _id), HttpStatus.OK);
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
