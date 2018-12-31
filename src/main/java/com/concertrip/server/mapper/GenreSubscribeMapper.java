package com.concertrip.server.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GenreSubscribeMapper {
    @Insert("INSERT INTO subscribe(userIdx, type, objIdx) VALUES ((select userIdx from user where id=#{userId}), 'genre', #{genreId});")
    void subscribe(
            @Param("genreId") final String genreId,
            @Param("userId") final String userId
    );

    @Select("Select COUNT(id) FROM subscribe WHERE type=#{type} and objIdx=#{objIdx}")
    int getSubscribeNum(
            @Param("type") final String type,
            @Param("objIdx") final String objIdx
    );

    @Select("Select COUNT(id) FROM subscribe WHERE type=#{type} and objIdx=#{objIdx} and userIdx=#{userIdx}")
    int checkSubscribe(
            @Param("type") final String type,
            @Param("objIdx") final String objIdx,
            @Param("userIdx") final String userIdx
    );
}   
