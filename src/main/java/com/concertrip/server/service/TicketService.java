package com.concertrip.server.service;

import com.concertrip.server.dto.Ticket;
import com.concertrip.server.mapper.TicketMapper;
import com.concertrip.server.mapper.UserMapper;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import javafx.scene.input.DataFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by HYEON on 2018-12-28.
 */

@Slf4j
@Service
public class TicketService {
    private final TicketMapper ticketMapper;
    private final UserMapper userMapper;

    public TicketService(TicketMapper ticketMapper, UserMapper userMapper) {
        this.ticketMapper = ticketMapper;
        this.userMapper = userMapper;
    }
    //모든 티켓 조회
    public DefaultRes getAllTicket() {
        final List<Ticket> ticketList = ticketMapper.findByDate();
        if(ticketList.isEmpty()) return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_TICKETS);
        return DefaultRes.res(StatusCode.OK,ResponseMessage.READ_TICKETS,ticketList);
    }

    //userIdx로 티켓 조회
    public DefaultRes findByuserIdx(final int userIdx){
        final List<Ticket> ticketList  = ticketMapper.findByUserIdx(userIdx);
        if(userIdx < 1 || ticketList == null) return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_TICKETS);
        return DefaultRes.res(StatusCode.OK,ResponseMessage.READ_TICKETS, ticketList);
    }

    //token으로 티켓 조회
    public DefaultRes findUserIdxByToken(final String userId){
        final int userIdx = userMapper.findUserIdxByToken(userId);
        final List<Ticket> ticketList  = ticketMapper.findByUserIdx(userIdx);
        if(ticketList == null) return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_TICKETS);
        return DefaultRes.res(StatusCode.OK,ResponseMessage.READ_TICKETS, ticketList);
    }

    //eventId로 티켓 조회
    public DefaultRes findByeventId(final String eventId){
        final Ticket ticket  = ticketMapper.findByEventId(eventId);
        if(ticket == null) return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_TICKETS);
        return DefaultRes.res(StatusCode.OK,ResponseMessage.READ_TICKETS, ticket);
    }

    //티켓 등록
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
        final List<Ticket> ticketList = ticketMapper.findByUserIdx(userIdx);
        if (ticketList == null) return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_TICKETS);
        try {
            ticketMapper.deleteByUserIdx(userIdx);
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.DELETE_TICKETS);
        } catch (Exception e){
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }


}
