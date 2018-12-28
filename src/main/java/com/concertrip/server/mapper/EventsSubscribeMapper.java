package com.concertrip.server.mapper;

import com.concertrip.server.model.EventsSubscribeReq;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EventsSubscribeMapper {
    @Insert("INSERT INTO eventSubscribe(eventIdx, userIdx) VALUES (#{eventIdx}, (select userIdx from user where id=#{userId}));")
    void subscribe(
            @Param("eventIdx") final String eventIdx,
            @Param("userId") final String userId
    );

    @Select("SELECT * FROM eventSubscribe WHERE userIdx = #{userIdx}")
    EventsSubscribeReq[] findSubscribe(@Param("userIdx") final String userIdx);
}
