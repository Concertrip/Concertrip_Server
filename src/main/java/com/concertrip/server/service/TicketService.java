package com.concertrip.server.service;

import com.concertrip.server.dto.Ticket;
import com.concertrip.server.mapper.TicketMapper;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * Created by HYEON on 2018-12-28.
 */

@Slf4j
@Service
public class TicketService {
    private final TicketMapper ticketMapper;

    public TicketService(TicketMapper ticketMapper) {
        this.ticketMapper = ticketMapper;
    }
    //모든 티켓 조회
    public DefaultRes getAllTicket() {
        final List<Ticket> ticketList = ticketMapper.findAll();
        if(ticketList.isEmpty()) return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_TICKETS);
        return DefaultRes.res(StatusCode.OK,ResponseMessage.READ_TICKETS,ticketList);
    }

    //userIdx로 티켓 조회
    public DefaultRes findByuserIdx(final int userIdx){
        final Ticket ticket  = ticketMapper.findByUserIdx(userIdx);
        if(ticket == null) return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_TICKETS);
        return DefaultRes.res(StatusCode.OK,ResponseMessage.READ_TICKETS, ticket);
    }

    //eventId로 티켓 조회
    public DefaultRes findByeventId(final String eventId){
        final Ticket ticket  = ticketMapper.findByEventId(eventId);
        if(ticket == null) return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_TICKETS);
        return DefaultRes.res(StatusCode.OK,ResponseMessage.READ_TICKETS, ticket);
    }

    @Transactional
    public DefaultRes saveTicket(final Ticket ticket){
        try {
            ticketMapper.save(ticket);
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_TICKETS);
        } catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
    //티켓 삭제
    public DefaultRes deleteByuserIdx(final int userIdx) {
        final Ticket ticket = ticketMapper.findByUserIdx(userIdx);
        if (ticket == null) return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_TICKETS);
        try {
            ticketMapper.deleteByUserIdx(userIdx);
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.DELETE_TICKETS);
        } catch (Exception e){
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }


}
