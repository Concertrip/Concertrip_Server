package com.concertrip.server.mapper;

import com.concertrip.server.model.ArtistsSubscribeReq;
import org.apache.ibatis.annotations.*;

/**
 * Created hyunjk on 2018-12-28.
 * Github : https://github.com/hyunjkluz
 */
@Mapper
public interface ArtistsSubscribeMapper {
    @Insert("INSERT INTO artistSubscribe(artistId, userIdx) VALUES (#{artistId}, (select userIdx from user where id=#{userIdx}));")
    void subscribe(
            @Param("artistId") final String artistId,
            @Param("userIdx") final String userIdx
    );

    @Delete("DELETE FROM artistSubscribe WHERE userIdx = #{userIdx} AND artistId = #{artistId}")
    void unSubscribe(@Param("userIdx") final String userIdx, @Param("artistId") final String artistId);

    @Select("SELECT * FROM artistSubscribe WHERE userIdx = #{userId} AND artistId = #{artistId}")
    ArtistsSubscribeReq isSubscribe(@Param("userIdx") final String userIdx, @Param("artistId") final String artistId);

    @Select("SELECT * FROM artistSubscribe WHERE userIdx = #{userIdx}")
    ArtistsSubscribeReq[] findSubscribe(@Param("userIdx") final String userIdx);
}
