package com.concertrip.server.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EventsMapper {
    @Insert("INSERT INTO eventSubscribe(eventIdx, userIdx) VALUES (#{eventIdx}, (select userIdx from user where id=#{userId}));")
    void subscribe(
            @Param("eventIdx") final String eventIdx,
            @Param("userId") final String userId
    );
}
