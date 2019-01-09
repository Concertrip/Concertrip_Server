package com.concertrip.server.mapper;

import com.concertrip.server.dto.Notice;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by HYEON on 2019-01-09.
 */

@Mapper
public interface NoticeMapper {

    @Select("SELECT * FROM notice WHERE userIdx = #{userIdx}")
    List<Notice> findByUserIdx(@Param("userIdx")final int userIdx);

    @Insert("INSERT INTO notice(userIdx, title, body, createdAt) VALUES (#{notice.userIdx}, #{notice.title}, #{notice.body}, #{notice.createdAt})")
    void save(@Param("notice") final Notice notice);
}
