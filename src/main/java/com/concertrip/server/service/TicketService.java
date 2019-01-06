package com.concertrip.server.service;

import com.concertrip.server.dto.Ticket;
import com.concertrip.server.mapper.TicketMapper;
import com.concertrip.server.mapper.UserMapper;
import com.concertrip.server.model.DefaultRes;
import com.concertrip.server.model.TicketListReq;
import com.concertrip.server.utils.ResponseMessage;
import com.concertrip.server.utils.StatusCode;
import javafx.scene.input.DataFormat;
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
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Date now = new Date();

    public TicketService(TicketMapper ticketMapper, UserMapper userMapper) {
        this.ticketMapper = ticketMapper;
        this.userMapper = userMapper;
    }

    //userIdx로 티켓 조회
    public DefaultRes findByUserIdx(final int userIdx) {
        try {
            Ticket ticket = new Ticket();
            if (ObjectUtils.isEmpty(userIdx)) {
                return DefaultRes.res(401, ResponseMessage.EMPTY_TOKEN);
            }
            log.info(df.format(now));
            List<Ticket> ticketList1 = ticketMapper.findByUserIdxAsc(userIdx); //오름차순
            List<Ticket> ticketList2 = ticketMapper.findByUserIdxDesc(userIdx);
            TicketListReq ticketListReq = new TicketListReq(ticketList1,ticketList2);
            log.info(ticketList1.toString());
            log.info(ticketList2.toString());
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_TICKETS, ticketListReq);
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
        final List<Ticket> ticketList = ticketMapper.findByUserIdxDesc(userIdx);
        final Ticket ticket = ticketMapper.findByEventId(_id);
        //ticket = ticketMapper.updateEventId(eventId, userIdx);
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


}
