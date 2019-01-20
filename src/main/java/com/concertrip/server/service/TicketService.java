package com.concertrip.server.service;

import com.concertrip.server.dao.EventsRepository;
import com.concertrip.server.domain.Events;
import com.concertrip.server.dto.Ticket;
import com.concertrip.server.mapper.TicketMapper;
import com.concertrip.server.mapper.UserMapper;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.model.TicketListReq;
import com.concertrip.server.model.TicketPaymentReq;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by HYEON on 2018-12-28.
 */

@Slf4j
@Service
public class TicketService {
    private final TicketMapper ticketMapper;
    private final UserMapper userMapper;
    private final EventsRepository eventsRepository;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Date now = new Date();

    public TicketService(TicketMapper ticketMapper, UserMapper userMapper, EventsRepository eventsRepository) {
        this.ticketMapper = ticketMapper;
        this.userMapper = userMapper;
        this.eventsRepository = eventsRepository;
    }

    //userIdx로 티켓 조회
    @Transactional
    public DefaultRes findByUserIdx(final int userIdx) {
        try {
            //Ticket ticket = new Ticket();
            if (ObjectUtils.isEmpty(userIdx)) {
                return DefaultRes.res(401, ResponseMessage.EMPTY_TOKEN);
            }
            List<Ticket> ticketList1 = ticketMapper.findByUserIdxAsc(userIdx); //오름차순
            List<Ticket> ticketList2 = ticketMapper.findByUserIdxDesc(userIdx);
            TicketListReq ticketListReq = new TicketListReq(ticketList1, ticketList2);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_TICKETS, ticketListReq);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
    @Transactional
    public DefaultRes findTicketImg(final int userIdx) {
        try {
            List<String> ticketList = ticketMapper.findTicketImg(userIdx);
            Collections.reverse(ticketList);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_TICKETS, ticketList);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    //1개의 티켓 상세 조회
    public DefaultRes findByEventId(final int userIdx, final String _id) {
        if (ObjectUtils.isEmpty(userIdx)) {
            return DefaultRes.res(401, ResponseMessage.EMPTY_TOKEN);
        }
        //final List<Ticket> ticketList = ticketMapper.findByUserIdxDesc(userIdx);
        final Ticket ticket = ticketMapper.findByEventId(_id);
        if (ticket == null) return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_TICKETS);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_TICKETS, ticket);
    }

    //티켓 등록
    public DefaultRes saveTicket(final Ticket ticket) {
        try {
            ticketMapper.save(ticket);
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_TICKETS);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    //티켓 삭제
    public DefaultRes deleteByuserIdx(final int userIdx) {
        if (ObjectUtils.isEmpty(userIdx)) {
            return DefaultRes.res(401, ResponseMessage.EMPTY_TOKEN);
        }
        final List<Ticket> ticketList = ticketMapper.findByUserIdxDesc(userIdx);
        if (ticketList == null) return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_TICKETS);
        try {
            ticketMapper.deleteByUserIdx(userIdx);
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.DELETE_TICKETS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    //FIXME: delete later
    public DefaultRes save_tmp(final Integer userIdx, final TicketPaymentReq ticketPaymentReq) {
        try {
            String eventId = ticketPaymentReq.getEventId();
            Events events = eventsRepository.findEventsBy_id(eventId);
            String ticketImg = events.getTicketImg();
            ticketMapper.save_tmp(userIdx, ticketImg);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.BUY_SUCCESS);
        } catch (Exception e) {
            return DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
