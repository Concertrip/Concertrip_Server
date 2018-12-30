package com.concertrip.server.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GenreSubscribeMapper {
    @Insert("INSERT INTO subscribe(userIdx, type, objIdx) VALUES ((select userIdx from user where id=#{userId}), 'genre', #{genreId});")
    void subscribe(
            @Param("genreId") final String genreId,
            @Param("userId") final String userId
    );
}   
