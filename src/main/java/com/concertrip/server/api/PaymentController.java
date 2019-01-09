package com.concertrip.server.api;

import com.concertrip.server.dao.EventsRepository;
import com.concertrip.server.domain.Events;
import com.concertrip.server.model.TicketPaymentReq;
import com.concertrip.server.service.JwtService;
import com.concertrip.server.service.TicketService;
import com.concertrip.server.utils.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/payment")
public class PaymentController {
    private final JwtService jwtService;
    private final TicketService ticketService;

    public PaymentController(JwtService jwtService, TicketService ticketService) {
        this.jwtService = jwtService;
        this.ticketService = ticketService;
    }

    @Auth
    @PostMapping
    public ResponseEntity ticketPayment(
            @RequestHeader(value = "Authorization") final String token,
            @RequestBody final TicketPaymentReq ticketPaymentReq)
    {
        JwtService.Token decodedToken = jwtService.decode(token);
        Integer userIdx = decodedToken.getUser_idx();
        return new ResponseEntity<>(ticketService.save_tmp(userIdx, ticketPaymentReq), HttpStatus.OK);
    }
}
