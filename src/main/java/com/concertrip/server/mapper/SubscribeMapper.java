package com.concertrip.server.mapper;

import com.concertrip.server.dto.Subscribe;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created hyunjk on 2018-12-29.
 * Github : https://github.com/hyunjkluz
 */
@Mapper
public interface SubscribeMapper {
    @Select("SELECT * FROM subscribe WHERE userIdx = #{userIdx}")
    List<Subscribe> getUserAllSubscribe(@Param("userIdx") final int userIdx);

    @Select("SELECT * FROM subscribe WHERE userIdx = #{userIdx} AND type = #{type}")
    List<Subscribe> getUserSubscribe(@Param("userIdx") final int userIdx, @Param("type") final String type);

    @Select("SELECT COUNT(id) FROM subscribe WHERE type = #{type} AND objIdx = #{objIdx}")
    Integer subscribeNum(@Param("type") final String type, @Param("objIdx") final String objIdx);

    @Select("SELECT COUNT(id) FROM subscribe WHERE userIdx = #{userIdx} AND type = #{type} AND objIdx = #{objIdx}")
    Integer isSubscribe(@Param("userIdx") final int userIdx, @Param("type") final String type, @Param("objIdx") final String objIdx);

    @Insert("INSERT INTO subscribe(userIdx, type, objIdx) VALUES (#{userIdx}, #{type}, #{objIdx})")
    void subscribe(@Param("userIdx") final int userIdx, @Param("type") final String type, @Param("objIdx") final String objIdx);

    @Delete("DELETE FROM subscribe WHERE userIdx = #{userIdx} AND type = #{type} AND objIdx = #{objIdx}")
    void unSubscribe(@Param("userIdx") final int userIdx, @Param("type") final String type, @Param("objIdx") final String objIdx);

}
