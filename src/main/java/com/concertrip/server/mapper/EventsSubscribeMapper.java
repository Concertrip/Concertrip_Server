package com.concertrip.server.mapper;

import com.concertrip.server.model.EventsSubscribeReq;
import org.apache.ibatis.annotations.*;

@Mapper
public interface EventsSubscribeMapper {
    @Insert("INSERT INTO eventSubscribe(eventId, userIdx) VALUES (#{eventId}, (select userIdx from user where id=#{userIdx}));")
    void subscribe(
            @Param("eventId") final String eventId,
            @Param("userIdx") final String userIdx
    );

    @Delete("DELETE FROM eventSubscribe WHERE userIdx = #{userIdx} AND eventIdx = #{eventId}")
    void unSubscribe(@Param("userIdx") final String userIdx, @Param("eventId") final String eventId);

    @Select("SELECT * FROM eventSubscribe WHERE userIdx = #{userId} AND eventIdx = #{eventId}")
    EventsSubscribeReq isSubscribe(@Param("userIdx") final String userIdx, @Param("eventId") final String eventId);

    @Select("SELECT * FROM eventSubscribe WHERE userIdx = #{userIdx}")
    EventsSubscribeReq[] findSubscribe(@Param("userIdx") final String userIdx);
}
