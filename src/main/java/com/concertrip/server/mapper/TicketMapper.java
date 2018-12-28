package com.concertrip.server.mapper;

import com.concertrip.server.dto.Ticket;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

/**
 * Created by HYEON on 2018-12-28.
 */

@Mapper
public interface TicketMapper {
    //모든 티켓 조회
    @Select("SELECT * FROM ticket")
    List<Ticket> findAll();

    //회원 이름으로 조회
    @Select("SELECT * FROM ticket WHERE userIdx = #{userIdx}")
    Ticket findByUserIdx(@Param("userIdx") final int userIdx);

    //티켓 이름으로 조회
    @Select("SELECT * FROM ticket WHERE eventId = #{eventId}")
    Ticket findByEventId(@Param("eventId") final String eventId);

    @Insert("INSERT INTO ticket(serialNum, seat, barcodeNum, title, date, location, eventId, userIdx)" +
            "VALUES(#{ticket.serialNum}, #{ticket.seat}, #{ticket.barcodeNum}, #{ticket.title}, #{ticket.date}, #{ticket.location}, #{ticket.eventId}, #{ticket.userIdx})")
    void save(@Param("ticket")final Ticket ticket);

    @Delete("DELETE FROM ticket WHERE userIdx = #{userIdx}")
    void deleteByUserIdx(@Param("userIdx")final int userIdx);
}
