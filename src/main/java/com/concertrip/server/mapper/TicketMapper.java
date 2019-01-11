package com.concertrip.server.mapper;

import com.concertrip.server.dto.Ticket;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by HYEON on 2018-12-28.
 */

@Mapper
public interface TicketMapper {

    //회원으로 티켓조회 (날짜 지난 티켓 - 내림차순)
    @Select("SELECT * FROM ticket WHERE userIdx = #{userIdx} AND date Between '2018-12-01' and now() ORDER BY date desc")
    List<Ticket> findByUserIdxDesc(@Param("userIdx") final int userIdx);

    //회원으로 티켓조회 (임박한 티켓 - 오름차순)
    @Select("SELECT * FROM ticket WHERE userIdx = #{userIdx} AND date Between now() and '2019-12-31' ORDER BY date asc")
    List<Ticket> findByUserIdxAsc(@Param("userIdx") final int userIdx);

    //티켓 _id로 조회 내림차순(날짜 지난 티켓)
    @Select("SELECT * FROM ticket WHERE _id = #{_id} ORDER BY date")
    Ticket findByEventId(@Param("_id") final String _id);

    //티켓 _id로 조회 내림차순(임박한 티켓)
    @Select("SELECT * FROM ticket WHERE _id = #{_id} ORDER BY date ASC")
    Ticket findByEventIdAsc(@Param("_id") final String _id);

    @Update("UPDATE ticket SET eventId = #{eventId} WHERE userIdx = #{userIdx}")
    void updateEventId(@Param("eventId") final String eventId, @Param("userIdx") final Integer userIdx);

    //티켓 등록
    @Insert("INSERT INTO ticket(serialNum, seat, barcodeNum, title, date, location, eventId, userIdx)" +
            "VALUES(#{ticket.serialNum}, #{ticket.seat}, #{ticket.barcodeNum}, #{ticket.title}, #{ticket.date}, #{ticket.location}, #{ticket.eventId}, #{ticket.userIdx})")
    Ticket save(@Param("ticket")final Ticket ticket);

    //티켓 삭제
    @Delete("DELETE FROM ticket WHERE userIdx = #{userIdx}")
    void deleteByUserIdx(@Param("userIdx")final int userIdx);

    //티켓 등록 임시
    @Insert("INSERT INTO ticket_tmp(ticketImg, userIdx) VALUES(#{ticketImg}, #{userIdx})")
    void save_tmp(@Param("userIdx") final Integer userIdx, @Param("ticketImg") final String ticketImg);

    @Select("SELECT ticketImg FROM ticket_tmp WHERE userIdx = #{userIdx}")
    List<String> findTicketImg(@Param("userIdx") final Integer userIdx);
}
